package com.example.hibernate.dao.order;


import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;

import java.util.*;

public class OrderDao<T extends Order> extends BaseDao<T> {

    @Inject
    public OrderDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public List<T> findAll(Class<T> t) {
        Criteria criteria = this.currentSession().createCriteria(t);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));
        criteria.addOrder(org.hibernate.criterion.Order.desc("addedCartDate"));

        return this.list(criteria);
    }

    public List<OrderManagementDTO> findAllOrderManagement() {
        Criteria criteria = this.currentSession().createCriteria(Order.class,"order");
        criteria.createAlias("order.owner", "owner");
        criteria.createAlias("order.successfulTransaction", "transaction");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));
        criteria.addOrder(org.hibernate.criterion.Order.desc("addedCartDate"));

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("order.oid"));
        projectionList.add(Projections.property("order.lastUpdated"));
        projectionList.add(Projections.property("order.orderNumber"));
        projectionList.add(Projections.property("order.orderName"));
        projectionList.add(Projections.property("owner.email"));
        projectionList.add(Projections.property("order.orderDate"));
        projectionList.add(Projections.property("order.addedCartDate"));
        projectionList.add(Projections.property("order.grossPrice"));
        projectionList.add(Projections.property("order.netPrice"));
        projectionList.add(Projections.property("order.active"));
        projectionList.add(Projections.property("order.deliverable"));
        projectionList.add(Projections.property("order.downloadHidden"));
        projectionList.add(Projections.property("order.deliverableDeleted"));
        projectionList.add(Projections.property("order.downloadCount"));
        projectionList.add(Projections.property("order.checkOutDate"));
        projectionList.add(Projections.property("order.orderState"));
        projectionList.add(Projections.property("order.documentNumber"));
        projectionList.add(Projections.property("order.receiptNumber"));
        projectionList.add(Projections.property("transaction.approvedByE"));
        criteria.setProjection(projectionList);
        List<OrderManagementDTO> resultList = new ArrayList<OrderManagementDTO>();

        List<Order> list = (List<Order>) this.list(criteria);
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Object[] obj = (Object[]) itr.next();
            resultList.add(
                    new OrderManagementDTO(
                            String.valueOf(obj[0]),
                            Long.toString((Long) obj[1]),
                            String.valueOf(obj[2]),
                            String.valueOf(obj[3]),
                            String.valueOf(obj[4]),
                            (Date)obj[5],
                            (Date)obj[6],
                            (double) obj[7],
                            (double) obj[8],
                            (boolean) obj[9],
                            (boolean) obj[10],
                            (boolean) obj[11],
                            (boolean) obj[12],
                            (int) obj[13],
                            (Date)obj[14],
                            (Order.OrderState) obj[15],
                            String.valueOf(obj[16]),
                            String.valueOf(obj[17]),
                            String.valueOf(obj[18])
                    )
            );
        }
        return resultList;
    }



    public T findByIdAndClass(Class<T> t, String id){
        Criteria criteria = this.currentSession().createCriteria(t);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));
        criteria.add(Restrictions.eq("oid",id));

        return uniqueResult(criteria);
    }


    public List<T> findByOrderState(Class<T> t, Collection<Order.OrderState> order) {
        Criteria criteria = this.currentSession().createCriteria(t);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.eq("orderState", order));
        return this.list(criteria);
    }

    public Order findByOrderNumber(String orderNumber){
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");

        criteria.add(Restrictions.eq("Order.orderNumber",orderNumber));

        return uniqueResult(criteria);
    }

    public List<T> findUsersOrdersInCart(Class<T> orderClass, String username) {
        Criteria criteria = this.currentSession().createCriteria(orderClass, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.or(
                Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICE")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICEFAILED"))
        ));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));
        criteria.addOrder(org.hibernate.criterion.Order.desc("addedCartDate"));
        return this.list(criteria);
    }

    public List<OrderCartDTO> findUsersOrdersInCartDto(Class<T> orderClass, String username) {
        Criteria criteria = this.currentSession().createCriteria(orderClass, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.or(
                Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICE")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICEFAILED"))
        ));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));
        criteria.addOrder(org.hibernate.criterion.Order.desc("addedCartDate"));

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("order.oid"));
        projectionList.add(Projections.property("order.lastUpdated"));
        projectionList.add(Projections.property("order.orderNumber"));
        projectionList.add(Projections.property("order.orderName"));
        projectionList.add(Projections.property("order.summary"));
        projectionList.add(Projections.property("order.addedCartDate"));
        projectionList.add(Projections.property("order.grossPrice"));
        projectionList.add(Projections.property("order.netPrice"));
        projectionList.add(Projections.property("order.orderState"));
        criteria.setProjection(projectionList);
        List<OrderCartDTO> resultList = new ArrayList<OrderCartDTO>();

        List<Order> list = (List<Order>) this.list(criteria);
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Object[] obj = (Object[]) itr.next();
            resultList.add(
                    new OrderCartDTO(
                            String.valueOf(obj[0]),
                            Long.toString((Long) obj[1]),
                            String.valueOf(obj[2]),
                            String.valueOf(obj[3]),
                            (OrderSummary) obj[4],
                            (Date)obj[5],
                            (double) obj[6],
                            (double) obj[7],
                            (Order.OrderState) obj[8]
                    )
            );
        }

        return resultList;

    }

    public T findUsersOrdersInCartAndOid(Class<T> orderClass, String username, String oid) {
        Criteria criteria = this.currentSession().createCriteria(orderClass, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.or(
                Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICE"))
        ));
        criteria.add(Restrictions.eq("oid", oid));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));
        return this.uniqueResult(criteria);
    }

    public List<T> findUsersOrdersInCartAndOrderNumber(Class<Order> orderClass, String username, List<String> orderNumberList) {
        Criteria criteria = this.currentSession().createCriteria(orderClass, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.or(
                Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICE"))
        ));
        criteria.add(Restrictions.in("orderNumber", orderNumberList));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));
        return this.list(criteria);
    }


    public List<T> findUsersOrdersInCartAndOrderOids(Class<Order> orderClass, String username, List<String> orderOidList) {
        Criteria criteria = this.currentSession().createCriteria(orderClass, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.or(
                Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICE"))
        ));
        criteria.add(Restrictions.in("oid", orderOidList));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));
        return this.list(criteria);
    }


    public List<T> findUsersReadyOrdersInCart(Class<T> orderClass, String username) {
        Criteria criteria = this.currentSession().createCriteria(orderClass, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));
        return this.list(criteria);
    }

    public long findOrderCountInCart(String userEmail) {
        Criteria criteria = this.currentSession().createCriteria(Order.class, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.or(
                Restrictions.eq("orderState", Order.OrderState.valueOf("INCART")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICE")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("CALCULATINGPRICEFAILED")),
                Restrictions.eq("orderState", Order.OrderState.valueOf("ERRORWAITADMIN"))
        ));
        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", userEmail));
        criteria.setProjection(Projections.rowCount());

        return (Long) criteria.uniqueResult();
    }

    public List<OrderManagementDTO> findUsersFinishedOrders(Class<T> t, String username) {
        Criteria criteria = this.currentSession().createCriteria(t, "order");
        criteria.add(Restrictions.disjunction()
                                .add(Restrictions.eq("lifeState",Order.LifeState.ALIVE))
                                .add(Restrictions.eq("lifeState",Order.LifeState.DELETEDBYADMIN)));


        //TODO fix this
        // criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.gt("orderState", Order.OrderState.INCART));


        criteria.createAlias("order.owner", "owner");
        criteria.add(Restrictions.eq("owner.email", username));

        criteria.addOrder(org.hibernate.criterion.Order.desc("checkOutDate"));

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("order.oid"));
        projectionList.add(Projections.property("order.lastUpdated"));
        projectionList.add(Projections.property("order.orderNumber"));
        projectionList.add(Projections.property("order.orderName"));
        projectionList.add(Projections.property("owner.email"));
        projectionList.add(Projections.property("order.orderDate"));
        projectionList.add(Projections.property("order.addedCartDate"));
        projectionList.add(Projections.property("order.grossPrice"));
        projectionList.add(Projections.property("order.netPrice"));
        projectionList.add(Projections.property("order.active"));
        projectionList.add(Projections.property("order.deliverable"));
        projectionList.add(Projections.property("order.downloadHidden"));
        projectionList.add(Projections.property("order.deliverableDeleted"));
        projectionList.add(Projections.property("order.downloadCount"));
        projectionList.add(Projections.property("order.checkOutDate"));
        projectionList.add(Projections.property("order.orderState"));
        criteria.setProjection(projectionList);
        List<OrderManagementDTO> resultList = new ArrayList<OrderManagementDTO>();

        List<Order> list = (List<Order>) this.list(criteria);
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Object[] obj = (Object[]) itr.next();
            resultList.add(
                    new OrderManagementDTO(
                            String.valueOf(obj[0]),
                            Long.toString((Long) obj[1]),
                            String.valueOf(obj[2]),
                            String.valueOf(obj[3]),
                            String.valueOf(obj[4]),
                            (Date)obj[5],
                            (Date)obj[6],
                            (double) obj[7],
                            (double) obj[8],
                            (boolean) obj[9],
                            (boolean) obj[10],
                            (boolean) obj[11],
                            (boolean) obj[12],
                            (int) obj[13],
                            (Date)obj[14],
                            (Order.OrderState) obj[15]
                    )
            );
        }

        return resultList;

    }

    public List<Order> findOrdersFinishedDownloads(Date time) {
        Criteria criteria = currentSession().createCriteria(Order.class);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));


//        criteria.add(Restrictions.eq("deliverable", true));
        criteria.add(Restrictions.eq("deliverableDeleted", false));

        criteria.add(Restrictions.isNotNull("finishedDownloadsDate"));
        criteria.add(Restrictions.le("finishedDownloadsDate", time));

        return criteria.list();
    }

    public List<Order> findUndeletedOrdersBefore(Date time) {
        Criteria criteria = currentSession().createCriteria(Order.class);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

//        criteria.add(Restrictions.eq("deliverable", true));
        criteria.add(Restrictions.eq("downloadHidden", false));
//        criteria.add(Restrictions.eq("deliverableDeleted", false));
        criteria.add(Restrictions.isNotNull("checkOutDate"));
        criteria.add(Restrictions.le("checkOutDate", time));

        return criteria.list();

    }

    public List<Order> findDownloadedOnceOrdersBefore(Date time) {
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

//        criteria.add(Restrictions.eq("deliverable", true));
        criteria.add(Restrictions.eq("deliverableDeleted", false));
//        criteria.add(Restrictions.ge("downloadCount", 1));
        criteria.add(Restrictions.isNotNull("checkOutDate"));
        criteria.add(Restrictions.le("checkOutDate", time));


        return criteria.list();
    }

    public List<Order> findDownloadedOnceOrdersGreaterThan(long bytes) {
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

//        criteria.add(Restrictions.eq("deliverable", true));
        criteria.add(Restrictions.eq("deliverableDeleted", false));
//        criteria.add(Restrictions.ge("downloadCount", 1));
        criteria.add(Restrictions.ge("deliverableSize", bytes));


        return criteria.list();
    }

    public long getCountByUsagePurpose(OrderSettings.UsagePurpose usagePurpose) {
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.eq("usagePurpose", usagePurpose));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        return count == null ? 0 : count;
    }

    public List<Order> findOrdersByPaymentGuid(String guid){
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.eq("Order.orderGuid",guid));

        return criteria.list();
    }

    @Override
    public T delete(T entity) {

        if (entity.isDeliverable() && !entity.isDeliverableDeleted()) {
            String path = entity.getOrderNumber().substring(0,8);
            FTPManager.delete(path, entity.getOrderNumber() + ".zip");
            FTPManager.delete("", entity.getOrderNumber() + ".zip");
        }
        if(entity.getLifeState()== Order.LifeState.ALIVE){
            entity.setLifeState(Order.LifeState.DELETEDBYUSER);
        }
        if(entity.getLifeState() == Order.LifeState.DELETEDBYADMIN){
            entity.setDownloadHidden(true);
        }

        return update(entity);
    }


    public List<OrderCartDTO> findPricedOrdersInCartByUser(String userId, boolean free) {
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.createAlias("Order.owner","owner");
        criteria.createAlias("owner.role","role");

        criteria.add(Restrictions.eq("owner.oid",userId));
        criteria.add(Restrictions.eq("Order.orderState",Order.OrderState.INCART));
        if(free){

            //zero price orders
            criteria.add(Restrictions.eq("Order.netPrice",0.0));

            //and not documented orders
            DetachedCriteria isFreeUser = DetachedCriteria.forClass(ExtendedRole.class, "extendedRole")
                    .add(Restrictions.eq("extendedRole.islemTuru",ExtendedRole.OperationType.DOCUMENTED)).setProjection(Projections.property("extendedRole.oid"));

            criteria.add(Property.forName("role.oid").notIn( isFreeUser));
        }else{
            criteria.add(Restrictions.gt("Order.netPrice",0.0));

        }
        criteria.addOrder(org.hibernate.criterion.Order.desc("addedCartDate"));

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("Order.oid"));
        projectionList.add(Projections.property("Order.lastUpdated"));
        projectionList.add(Projections.property("Order.orderNumber"));
        projectionList.add(Projections.property("Order.orderName"));
        projectionList.add(Projections.property("Order.summary"));
        projectionList.add(Projections.property("Order.addedCartDate"));
        projectionList.add(Projections.property("Order.grossPrice"));
        projectionList.add(Projections.property("Order.netPrice"));
        projectionList.add(Projections.property("Order.orderState"));
        criteria.setProjection(projectionList);
        List<OrderCartDTO> resultList = new ArrayList<OrderCartDTO>();

        List<Order> list = (List<Order>) this.list(criteria);
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Object[] obj = (Object[]) itr.next();
            resultList.add(
                    new OrderCartDTO(
                            String.valueOf(obj[0]),
                            Long.toString((Long) obj[1]),
                            String.valueOf(obj[2]),
                            String.valueOf(obj[3]),
                            (OrderSummary) obj[4],
                            (Date)obj[5],
                            (double) obj[6],
                            (double) obj[7],
                            (Order.OrderState) obj[8]
                    )
            );
        }

        return resultList;
    }

    public List<OrderCartDTO> findDocumentedOrdersInCartByUser(String userId) {
        Criteria criteria = currentSession().createCriteria(Order.class, "Order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.createAlias("Order.owner","owner");
        criteria.createAlias("owner.role","role");
        criteria.add(Restrictions.eq("owner.oid",userId));
        criteria.add(Restrictions.eq("Order.orderState",Order.OrderState.INCART));

        DetachedCriteria isDocumented = DetachedCriteria.forClass(ExtendedRole.class, "extendedRole")
                .add(Restrictions.eq("extendedRole.islemTuru",ExtendedRole.OperationType.DOCUMENTED)).setProjection(Projections.property("extendedRole.oid"));

        criteria.add(Property.forName("role.oid").in( isDocumented));
        criteria.addOrder(org.hibernate.criterion.Order.desc("addedCartDate"));

        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("Order.oid"));
        projectionList.add(Projections.property("Order.lastUpdated"));
        projectionList.add(Projections.property("Order.orderNumber"));
        projectionList.add(Projections.property("Order.orderName"));
        projectionList.add(Projections.property("Order.summary"));
        projectionList.add(Projections.property("Order.addedCartDate"));
        projectionList.add(Projections.property("Order.grossPrice"));
        projectionList.add(Projections.property("Order.netPrice"));
        projectionList.add(Projections.property("Order.orderState"));
        criteria.setProjection(projectionList);
        List<OrderCartDTO> resultList = new ArrayList<OrderCartDTO>();

        List<Order> list = (List<Order>) this.list(criteria);
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Object[] obj = (Object[]) itr.next();
            resultList.add(
                    new OrderCartDTO(
                            String.valueOf(obj[0]),
                            Long.toString((Long) obj[1]),
                            String.valueOf(obj[2]),
                            String.valueOf(obj[3]),
                            (OrderSummary) obj[4],
                            (Date)obj[5],
                            (double) obj[6],
                            (double) obj[7],
                            (Order.OrderState) obj[8]
                    )
            );
        }

        return resultList;

    }

    public List<T> findOrdersByOids(List<String> orderOidList) {
        Criteria criteria = this.currentSession().createCriteria(Order.class, "order");
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));
        criteria.add(Restrictions.in("oid", orderOidList));

        return this.list(criteria);
    }

    public List<T> findOrderStateError(Class<T> t) {
        Criteria criteria = this.currentSession().createCriteria(t);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.eq("orderState", Order.OrderState.ERRORWAITADMIN));

        criteria.addOrder(org.hibernate.criterion.Order.desc("checkOutDate"));
        return this.list(criteria);
    }

    public List<T> findForecastingModel(Class<T> t) {
        Criteria criteria = this.currentSession().createCriteria(t);
        criteria.add(Restrictions.eq("lifeState",Order.LifeState.ALIVE));

        criteria.add(Restrictions.eq("orderState", Order.OrderState.WAITINGADMINAPPROVAL));
        return this.list(criteria);
    }
}
