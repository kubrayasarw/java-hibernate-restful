package com.example.hibernate.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity(name="SIPARIS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = "class")
public class Order extends Payable implements Comparable<Order>{

    @Override
    public int compareTo(Order o) {
        return priority - o.getPriority();
    }

    public enum LifeState{
        ALIVE,
        DELETEDFROMCART,
        DELETEDBYUSER,
        DELETEDBYADMIN
    }

    @Column(name="col_name", columnDefinition="int(11) default '0'")
    private LifeState lifeState;

    private String orderNumber;

    @Override
    public String getNumber() {
        return orderNumber;
    }

    @JoinColumn(name="guncelleyenKullaniciOid")
    @ManyToOne
    private User owner;

    @Column(name = "siparisTarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = timeZone)
    private Date orderDate;

    @Column(name = "sepeteEklemeTarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = timeZone)
    private Date addedCartDate;


    private double grossPrice;
    private double netPrice;
    private boolean active;


    @JsonIgnore
    private String documentNumber;

    //@JsonIgnore
    @JsonIgnoreProperties(ignoreUnknown=true)
    @Column(name="siparisCesidi",columnDefinition="VARCHAR(255) DEFAULT ''" )
    private String orderName;

    @JsonIgnore
    private String receiptNumber;

    @Enumerated(EnumType.ORDINAL)
    private OrderState orderState;

    @Embedded
    private OrderSettings orderSettings;

    @Transient
    @JsonIgnore
    private PriceCalculator priceCalculator;

    @Transient
    @JsonIgnore
    private OrderProcessor orderProcessor;

    private int priority;


    @Column(name="dosyaIndirimiVar", columnDefinition = "bit DEFAULT 0")
    private boolean deliverable;

    @Column(name="mevcutIndirmeSayisi", columnDefinition = "int(11) DEFAULT 0")
    private int downloadCount;

    @Column(name = "indirimBitirmeTarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = timeZone)
    private Date finishedDownloadsDate;

    @Column(name="dosyaSilindi", columnDefinition = "bit DEFAULT 0")
    private boolean deliverableDeleted;

    @Column(name="datalinkGorunuyor", columnDefinition = "bit DEFAULT 0")
    private boolean downloadHidden;

    @Column(name="dosyaBoyutu", columnDefinition = "bigint DEFAULT 0")
    private long deliverableSize;

    @Column(name="sql_")
    @Lob
    private String sqlQuery;


    @Column(name="siparisZorlugu", columnDefinition = "int(11) DEFAULT 100000")
    private int orderDifficulty;

    @Column(name="ucretsizOdemeli", columnDefinition = "bit DEFAULT 0")
    private boolean orderedWithPayment;

    @Column(name = "siparisOnaylanmaTarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = timeZone)
    private Date checkOutDate;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getGrossPrice() {
        return grossPrice;
    }

    public void setGrossPrice(double grossPrice) {
        this.grossPrice = grossPrice;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OrderSettings getOrderSettings() {
        return orderSettings;
    }

    public void setOrderSettings(OrderSettings orderSettings) {
        this.orderSettings = orderSettings;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public PriceCalculator getPriceCalculator() {
        return priceCalculator;
    }

    public void setPriceCalculator(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }

    public Date getAddedCartDate() {
        return addedCartDate;
    }

    public void setAddedCartDate(Date addedCartDate) {
        this.addedCartDate = addedCartDate;
    }

    public OrderProcessor getOrderProcessor() {
        return orderProcessor;
    }

    public void setOrderProcessor(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    public boolean isDeliverable() {
        return deliverable;
    }

    public void setDeliverable(boolean deliverable) {
        this.deliverable = deliverable;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Date getFinishedDownloadsDate() {
        return finishedDownloadsDate;
    }

    public void setFinishedDownloadsDate(Date finishedDownloadsDate) {this.finishedDownloadsDate = finishedDownloadsDate;}

    public boolean isDeliverableDeleted() {
        return deliverableDeleted;
    }

    public void setDeliverableDeleted(boolean deliverableDeleted) {
        this.deliverableDeleted = deliverableDeleted;
    }

    public boolean isDownloadHidden() {
        return downloadHidden;
    }

    public void setDownloadHidden(boolean downloadHidden) {
        this.downloadHidden = downloadHidden;
    }

    public long getDeliverableSize() {
        return deliverableSize;
    }

    public void setDeliverableSize(long deliverableSize) {
        this.deliverableSize = deliverableSize;
    }

    public void initialize(){}

    public boolean isOrderedWithPayment() {
        return orderedWithPayment;
    }

    public void setOrderedWithPayment(boolean orderedWithPayment) {
        this.orderedWithPayment = orderedWithPayment;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public int getOrderDifficulty() {
        return orderDifficulty;
    }

    public void setOrderDifficulty(int orderDifficulty) {
        this.orderDifficulty = orderDifficulty;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LifeState getLifeState() {
        return lifeState;
    }

    public void setLifeState(LifeState lifeState) {
        this.lifeState = lifeState;
    }

    @Override
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = timeZone)
    public Date getDate() {
        return addedCartDate;
    }
    @JsonProperty(value = "date")
    @JsonIgnore
    public void setDate(){

    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}