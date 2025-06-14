package com.ADNService.SWP391.dto;

import java.time.LocalDate;

public class TestOrderDTO {
    private Long orderId;
    private Long customerId;
    private Long accountId;
    private Long serviceId;
    private LocalDate orderDate;
    private String sampleType;
    private String orderStatus;
    private String resultDeliveryMethod;
    private String resultDeliverAddress;
    private String kitCode;
    private int sampleQuantity;
    private int amount;

    public TestOrderDTO() {
    }

    public TestOrderDTO(Long orderId, Long customerId, Long accountId, Long serviceId, LocalDate orderDate, String sampleType, String orderStatus, String resultDeliverAddress, String resultDeliveryMethod, String kitCode, int sampleQuantity, int amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.accountId = accountId;
        this.serviceId = serviceId;
        this.orderDate = orderDate;
        this.sampleType = sampleType;
        this.orderStatus = orderStatus;
        this.resultDeliverAddress = resultDeliverAddress;
        this.resultDeliveryMethod = resultDeliveryMethod;
        this.kitCode = kitCode;
        this.sampleQuantity = sampleQuantity;
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getKitCode() {
        return kitCode;
    }

    public void setKitCode(String kitCode) {
        this.kitCode = kitCode;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getResultDeliverAddress() {
        return resultDeliverAddress;
    }

    public void setResultDeliverAddress(String resultDeliverAddress) {
        this.resultDeliverAddress = resultDeliverAddress;
    }

    public String getResultDeliveryMethod() {
        return resultDeliveryMethod;
    }

    public void setResultDeliveryMethod(String resultDeliveryMethod) {
        this.resultDeliveryMethod = resultDeliveryMethod;
    }

    public int getSampleQuantity() {
        return sampleQuantity;
    }

    public void setSampleQuantity(int sampleQuantity) {
        this.sampleQuantity = sampleQuantity;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}

