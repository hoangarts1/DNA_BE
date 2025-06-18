package com.ADNService.SWP391.dto;

import java.time.LocalDate;

public class TestOrderDTO {
    private Long orderId;
    private Long customerId;
    private Long registrationStaffId;
    private Long testingStaffId;
    private Long serviceId;
    private LocalDate orderDate;
    private String orderStatus;
    private String sampleType;
    private String resultDeliveryMethod;
    private String resultDeliverAddress;
    private String kitCode;
    private int sampleQuantity;
    private int amount;

    public TestOrderDTO() {
    }

    public TestOrderDTO(Long orderId, Long customerId, Long registrationStaffId, Long testingStaffId,
                        Long serviceId, LocalDate orderDate, String orderStatus, String sampleType,
                        String resultDeliverAddress, String resultDeliveryMethod, String kitCode,
                        int sampleQuantity, int amount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.registrationStaffId = registrationStaffId;
        this.testingStaffId = testingStaffId;
        this.serviceId = serviceId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.sampleType = sampleType;
        this.resultDeliverAddress = resultDeliverAddress;
        this.resultDeliveryMethod = resultDeliveryMethod;
        this.kitCode = kitCode;
        this.sampleQuantity = sampleQuantity;
        this.amount = amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getRegistrationStaffId() {
        return registrationStaffId;
    }

    public void setRegistrationStaffId(Long registrationStaffId) {
        this.registrationStaffId = registrationStaffId;
    }

    public Long getTestingStaffId() {
        return testingStaffId;
    }

    public void setTestingStaffId(Long testingStaffId) {
        this.testingStaffId = testingStaffId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getResultDeliveryMethod() {
        return resultDeliveryMethod;
    }

    public void setResultDeliveryMethod(String resultDeliveryMethod) {
        this.resultDeliveryMethod = resultDeliveryMethod;
    }

    public String getResultDeliverAddress() {
        return resultDeliverAddress;
    }

    public void setResultDeliverAddress(String resultDeliverAddress) {
        this.resultDeliverAddress = resultDeliverAddress;
    }

    public String getKitCode() {
        return kitCode;
    }

    public void setKitCode(String kitCode) {
        this.kitCode = kitCode;
    }

    public int getSampleQuantity() {
        return sampleQuantity;
    }

    public void setSampleQuantity(int sampleQuantity) {
        this.sampleQuantity = sampleQuantity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}