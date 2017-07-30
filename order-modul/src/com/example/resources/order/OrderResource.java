package com.example.resources.order;

import com.google.inject.Inject;
import com.sun.jersey.server.impl.application.WebApplicationContext;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Path("order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    private OrderDao<Order> orderDao;

    @Inject
    private ExtendedUserDao extendedUserDao;

    @Inject
    private ConstantDao constantDao;

    @Inject
    private OrderLogDao orderLogDao;

    @Inject
    private UserCartCountDao userCartCountDao;

    @Inject
    private LogEntryDao logEntryDao;

    @Inject
    private SessionFactory sessionFactory;

    @Path("all")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public List<Order> getAll(@Auth Credentials credentials) {
        List<Order> orders = orderDao.findAll(Order.class);

        for (Order order : orders) {
            order.initialize();
        }
        return orders;
    }

    @Path("allManagement")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public List<OrderManagementDTO> getAllManagement(@Auth Credentials credentials) {
        List<OrderManagementDTO> orders = orderDao.findAllOrderManagement();
        return orders;
    }

    @Path("mycart")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public List<OrderCartDTO> getMyOrdersInCart(@Auth Credentials credentials) {
        List<OrderCartDTO> orders = orderDao.findUsersOrdersInCartDto(Order.class, credentials.getUsername());
        return orders;
    }

    @Path("cartInfo")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public Map<String, Integer> getMyCartInfo(@Auth Credentials credentials) {
        List<Order> orders = orderDao.findUsersOrdersInCart(Order.class, credentials.getUsername());
        Map<String, Integer> cartInfo = new HashMap<>();
        double sum = 0;
        int orderCount = orders.size();
        for (Order order : orders) {
            sum += order.getNetPrice();
        }
        cartInfo.put(String.format(Locale.US, "%.2f", sum), orderCount);
        return cartInfo;
    }

    @Path("myOrders")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public List<OrderManagementDTO> getMyOrders(@Auth Credentials credentials) {
        List<OrderManagementDTO> orders = orderDao.findUsersFinishedOrders(Order.class, credentials.getUsername());
        return orders;
    }

    @Path("{oid}/showDownload")
    @POST
    @UnitOfWork(readOnly = false)
    public boolean showDownload(@PathParam("oid") String oid, @Auth Credentials credentials) {
//        Optional<ExtendedUser> extendedUserOptional = extendedUserDao.findByUsername(credentials.getUsername());
        Order order = orderDao.findById(oid);
        order.setDownloadHidden(false);

        orderLogDao.create(new OrderLog(
                order.getOrderNumber(),
                order.getSqlQuery(),
                OrderLog.LogType.ORDER,
                credentials.getUsername(),
                order.getOrderNumber() + " siparişin indirimi aktifleştirdi",
                true));

        return true;
    }

    @Path("{oid}/download")
    @GET
    @UnitOfWork(readOnly = false, transactional = true)
    @Permit
//    @Produces("*/*")
    public Response downloadDeliverable(@PathParam("oid") String oid, @Context HttpServletResponse response, @Context UriInfo allUri, @Auth Credentials credentials) throws IOException {
        Order order = orderDao.findById(oid);

        if (order.isDownloadHidden()) {
            throw new RRuntimeException("İndirim limiti", "İndirme limiti geçtiniz");
        }

        //TODO some users may have the permission to download others files
        if (!order.getOwner().getOid().equals(credentials.getUserId())) {


            throw new RRuntimeException("İzin hatası", "Dosya indirme izniniz yoktur");

        }

        OutputStream outputStream = response.getOutputStream();

        String orderFileName = order.getOrderNumber() + ".zip";
        UserAgent userAgent = UserAgent.parseUserAgentString(((WebApplicationContext) allUri).getRequest().getHeaderValue("User-Agent"));
        String attachmentName = MimeUtility.encodeText(orderFileName, "utf-8", "B");
        if (userAgent.getBrowser().getGroup() == Browser.IE) {
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(orderFileName, "UTF-8"));
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachmentName + "\"");
        }
        String path = order.getOrderNumber().substring(0, 8);
        FTPManager.download(outputStream, orderFileName, path);

        order.setDownloadCount(order.getDownloadCount() + 1);

        if (order.getDownloadCount() >= ((ExtendedRole) order.getOwner().getRole()).getMaksimumIndirmeSayisi()) {
            order.setDownloadHidden(true);
            order.setFinishedDownloadsDate(new Date());
        }

        orderLogDao.create(new OrderLog(
                order.getOrderNumber(),
                order.getSqlQuery(),
                OrderLog.LogType.ORDER,
                credentials.getUsername(),
                order.getOrderNumber() + " siparişi indirdi. Toplam indirim: " + order.getDownloadCount(),
                true));

        return Response.ok().build();
    }


    @Path("{orderNumber}/downloadFileForAdmin")
    @GET
    @UnitOfWork(readOnly = true, transactional = false)
    @Permit
//    @Produces("*/*")
    public Response downloadDeliverableForAdmin(@PathParam("orderNumber") String orderNumber, @Context HttpServletResponse response, @Context UriInfo allUri, @Auth Credentials credentials) throws IOException {

        OutputStream outputStream = response.getOutputStream();

        String orderFileName = orderNumber + ".zip";
        UserAgent userAgent = UserAgent.parseUserAgentString(((WebApplicationContext) allUri).getRequest().getHeaderValue("User-Agent"));
        String attachmentName = MimeUtility.encodeText(orderFileName, "utf-8", "B");
        if (userAgent.getBrowser().getGroup() == Browser.IE) {
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(orderFileName, "UTF-8"));
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachmentName + "\"");
        }
        String path = orderNumber.substring(0, 8);
        if (FTPManager.download(outputStream, orderFileName, path))
            return Response.ok().build();
        else {
            throw new RRuntimeException("Dosya Bulunamadı", "Sipariş Silinmiş ya da daha hazırlanmamış.");
        }
    }


    @Path("{oid}/cancel")
    @POST
    @UnitOfWork(readOnly = false, transactional = true)
    @Permit
    public boolean cancelOrder(@PathParam("oid") final String oid, @Context HttpServletResponse response, @Auth final Credentials credentials) throws IOException {


        TransactionalOperation transactionalOperation = new TransactionalOperation(
                new TransactionalExecution() {
                    @Override
                    public Object execute() throws Exception {
                        Order order = orderDao.findById(oid);

                        if (order.getDownloadCount() > 0) {
                            throw new RRuntimeException("İptal hatası", "Siparişi indirdikten sonra iptal edemezsiniz");
                        }

                        //TODO some users may have the permission to cancel others files
                        if (!order.getOwner().getOid().equals(credentials.getUserId())) {
                            throw new RRuntimeException("İptal hatası", "İptal etme izniniz yoktur");

                        }


                        int dayLimit = constantDao.getConstant().getOrderCancelTimeAfterFinished();
                        int daysAfterCreation = Days.daysBetween(new DateTime(order.getOrderDate()), DateTime.now())
                                .getDays();

                        if (daysAfterCreation > dayLimit) {
                            throw new RRuntimeException("İptal hatası", "İptal etme süresi geçti");
                        }


                        orderLogDao.create(new OrderLog(
                                order.getOrderNumber(),
                                order.getSqlQuery(),
                                OrderLog.LogType.ORDER,
                                credentials.getUsername(),
                                order.getOrderNumber() + " siparişi iptal etti",
                                true));

                        order.setDownloadHidden(true);
                        order.setOrderState(Order.OrderState.ORDERCANCEL);
                        return order.getOid();
                    }
                }
        );

        try {
            String orderOid = (String) transactionalOperation.executeWithRetry();
            OrderUtil.refundOrder(orderOid);
        } catch (RRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }

        return true;
    }

    @Path("{oid}/deleteOrder")
    @POST
    @UnitOfWork(readOnly = false, transactional = true)
    @Permit
    public boolean deleteOrder(@PathParam("oid") final String oid, @Context HttpServletResponse response, @Auth final Credentials credentials) throws IOException {

        TransactionalOperation transactionalOperation = new TransactionalOperation(
                new TransactionalExecution() {
                    @Override
                    public Object execute() throws Exception {
                        Order order = orderDao.findById(oid);

                        /*
                        //TODO some users may have the permission to cancel others files
                        if (!order.getOwner().getOid().equals(credentials.getUserId())) {
                            throw new RRuntimeException("Silme hatası", "Silme izniniz yoktur");
                        }
                        */
                        orderLogDao.create(new OrderLog(
                                order.getOrderNumber(),
                                order.getSqlQuery(),
                                OrderLog.LogType.ORDER,
                                credentials.getUsername(),
                                order.getOrderNumber() + " sipariş silindi",
                                true));

                        //order.setLifeState(Order.LifeState.ALIVE);
                        orderDao.delete(order);
                        //order.setDeleted(true);
                        return order.getOid();
                    }
                }
        );

        try {
            String orderOid = (String) transactionalOperation.executeWithRetry();
            //OrderUtil.refundOrder(orderOid);
        } catch (RRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }


//        orderDao.delete(order);
        return true;
    }


    @DELETE
    @UnitOfWork
    @Permit
    public boolean removeOrder(String orderOid, @Auth Credentials credentials) {

        Order order = orderDao.findById(orderOid);
        if (order.getOrderState().equals(Order.OrderState.INCART)) {
            order.getOrderProcessor().removeFromCart(order);
            Optional<UserCartCount> userCartCountOptional = userCartCountDao.findByUsername(credentials.getUsername());
            userCartCountOptional.get().setOrderCount(userCartCountOptional.get().getOrderCount() - 1);
//            if(userCartCountOptional.get().getOrderCount()==0){
//                userCartCountDao.delete(userCartCountOptional.get());
//            }
            //userCartCountDao.update(userCartCountOptional.get());
            orderLogDao.create(new OrderLog(
                    order.getOrderNumber(),
                    order.getSqlQuery(),
                    OrderLog.LogType.ORDER,
                    credentials.getUsername(),
                    order.getOrderNumber() + " siparişi sepetten çıkardı",
                    true));
        } else {
            orderLogDao.create(new OrderLog(
                    order.getOrderNumber(),
                    order.getSqlQuery(),
                    OrderLog.LogType.ORDER,
                    credentials.getUsername(),
                    order.getOrderNumber() + " siparişi sildi",
                    true));
        }
        order.setLifeState(Order.LifeState.DELETEDFROMCART);
        orderDao.delete(order);

        return true;
    }

    @Path("{orderNumber}/getSummary")
    @GET
    @UnitOfWork
    @Permit
    public OrderSummary getOrderSummary(@PathParam("orderNumber") String orderNumber, @Auth Credentials credentials) {
        Order order = orderDao.findByOrderNumber(orderNumber);
        return order.getOrderProcessor().getSummary(order);
    }

    @Path("{orderNumber}/getSqlQuery")
    @GET
    @UnitOfWork
    @Permit
    public Map getOrderSqlQuery(@PathParam("orderNumber") String orderNumber, @Auth Credentials credentials) {
        String query = orderLogDao.findSqlQueryByOrderNumber(orderNumber);
        HashMap sqlHashMap = new HashMap();
        sqlHashMap.put("query", query);
        return sqlHashMap;
    }

    /**
     * @param credentials
     * @return
     */
    @Path("categorize")
    @GET
    @UnitOfWork
    @Permit
    public Map getCategorizedCartOrders(@Auth Credentials credentials) {
        Map<String, List<OrderCartDTO>> map = new HashMap<>();
        List<OrderCartDTO> pricedOrders;
        List<OrderCartDTO> freeOrders;
        List<OrderCartDTO> documentedOrders = null;


        pricedOrders = orderDao.findPricedOrdersInCartByUser(credentials.getUserId(), false);
        freeOrders = orderDao.findPricedOrdersInCartByUser(credentials.getUserId(), true);
        documentedOrders = orderDao.findDocumentedOrdersInCartByUser(credentials.getUserId());

        map.put("free", freeOrders);
        map.put("priced", pricedOrders);
        map.put("documented", documentedOrders);

        return map;
    }

    @Path("{oid}/getOrderClass")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public Map getOrderClass(@PathParam("oid") String oid, @Auth Credentials credentials) {
        Order order = orderDao.findById(oid);
        order.initialize();
        String orderClass = order.getClass().toString();
        orderClass = orderClass.substring(orderClass.lastIndexOf(".") + 1);
        HashMap orderClassHashMap = new HashMap();
        orderClassHashMap.put("orderClass", orderClass);
        orderClassHashMap.put("order", order);
        return orderClassHashMap;
    }

    @Path("{oid}/getOrderById")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public Order getOrderById(@PathParam("oid") String oid, @Auth Credentials credentials) {
        Order order = orderDao.findById(oid);
        order.initialize();
        return order;
    }

    @Path("recalculatePrice")
    @POST
    @UnitOfWork(transactional = false)
    public Order recalculatePrice(final String orderOid, @Auth Credentials credentials) {


        TransactionalOperation transactionalOperation = new TransactionalOperation(new TransactionalExecution() {
            @Override
            public Object execute() throws Exception {
                Order order = orderDao.findById(orderOid);
                order.setOrderState(Order.OrderState.CALCULATINGPRICE);
                order.initialize();
                return order;
            }
        });

        Order order = null;
        try {
            order = (Order) transactionalOperation.execute();
            PriceQueueManager.addOrderItem(new PriceItem(new OrderDTO(order), order.getPriority(), new PriceEventImpl()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    @Path("errorOrder")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public List<Order> getErrorAll(@Auth Credentials credentials) {
        List<Order> orders = orderDao.findOrderStateError(Order.class);

        for (Order order : orders) {
            order.initialize();
        }
        return orders;
    }

    @Path("{orderNumber}/reactive")
    @POST
    @UnitOfWork(readOnly = false)
    public Order reActive(@PathParam("orderNumber") String orderNumber, @Auth Credentials credentials) {
//        Optional<ExtendedUser> extendedUserOptional = extendedUserDao.findByUsername(credentials.getUsername());
        Order order = orderDao.findByOrderNumber(orderNumber);
        order.setOrderState(Order.OrderState.EVALUATINGORDER);
        order.setActive(true);
        OrderQueueManager.addOrderItem(new OrderItem(new OrderDTO(order), new OrderEventImpl(), constantDao.getConstant().getRetryCount()));

        orderLogDao.create(new OrderLog(
                order.getOrderNumber(),
                order.getSqlQuery(),
                OrderLog.LogType.ORDER,
                order.getOwner().getEmail(),
                order.getOrderNumber() + " siparişin tekrar hazırlanması için kuyruğa geri alındı.",
                true));

        return order;
    }


    @Path("{orderNumber}/byAdmin")
    @DELETE
    @UnitOfWork
    @Permit
    public boolean removeOrderByAdmin(@PathParam("orderNumber") String orderNumber, @Auth Credentials credentials) {

        ExtendedUser extendedUser = extendedUserDao.findById(credentials.getUserId());
        ExtendedRole extendedRole = (ExtendedRole) extendedUser.getRole();

        if (!extendedRole.isKuyrukDurumunuDegistirmeYetkisi()) {
            throw new RRuntimeException("Yetki Hatası", "Sipariş kuyruğunu değiştirmeye yetkiniz yoktur.");
        }

        Order order = orderDao.findByOrderNumber(orderNumber);

        orderLogDao.create(new OrderLog(
                order.getOrderNumber(),
                order.getSqlQuery(),
                OrderLog.LogType.ORDER,
                credentials.getUsername(),
                order.getOrderNumber() + " siparişi sildi",
                true));

        order.setLifeState(Order.LifeState.DELETEDBYADMIN);
        order.setOrderState(Order.OrderState.ORDERCANCEL);
        orderDao.delete(order);

        return true;

    }

    @Path("forecastingModel")
    @GET
    @UnitOfWork(readOnly = true)
    @Permit
    public List<Order> getForecastingModel(@Auth Credentials credentials) {
        List<Order> orders = orderDao.findForecastingModel(Order.class);

        for (Order order : orders) {
            order.initialize();
        }
        return orders;
    }

    @Path("{orderNumber}/forecastingModelReady")
    @POST
    @UnitOfWork(readOnly = false)
    public Order forecastingModelReady(@PathParam("orderNumber") String orderNumber, @Auth Credentials credentials) {
//        Optional<ExtendedUser> extendedUserOptional = extendedUserDao.findByUsername(credentials.getUsername());
        Order order = orderDao.findByOrderNumber(orderNumber);
        order.setOrderState(Order.OrderState.READY);
        order.setActive(true);
        return order;
    }


    @Path("deleteSelectedOrders")
    @POST
    @UnitOfWork
    public boolean getDeleteSelectedOrders(@Valid LinkedHashMap rfv, @Auth final Credentials credentials) {
        rfv.getClass();
        System.out.println(rfv.getClass());
        final List<String> keys = new ArrayList<>(rfv.keySet());

        TransactionalOperation transactionalOperation = new TransactionalOperation(
                new TransactionalExecution() {
                    @Override
                    public Object execute() throws Exception {

                        for (String key : keys) {
                            Order order = orderDao.findById(key);


                            if (order.isDownloadHidden() || order.getDownloadCount() > 0) {


                                orderLogDao.create(new OrderLog(
                                        order.getOrderNumber(),
                                        order.getSqlQuery(),
                                        OrderLog.LogType.ORDER,
                                        credentials.getUsername(),
                                        order.getOrderNumber() + " sipariş silindi",
                                        true));

                                //order.setLifeState(Order.LifeState.ALIVE);
                                orderDao.delete(order);
                                //order.setDeleted(true);
                            }
                        }
                        return null;
                    }
                }
        );

        try {
            transactionalOperation.executeWithRetry();
        } catch (RRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }


        return true;
    }


}
