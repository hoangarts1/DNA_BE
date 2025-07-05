package com.ADNService.SWP391.dto;

public class ServiceDTO {
    private Long serviceID;
    private String serviceName;
    private String serviceType;
    private int timeTest;
    private String describe;
    private double price;
    private int numberOfSamples = 2;
    private Double pricePerAdditionalSample;
    private boolean isActive;

    public ServiceDTO() {
    }

    public ServiceDTO(Long serviceID, String serviceName, String serviceType, double price,
                      int timeTest, String describe, int numberOfSamples,
                      Double pricePerAdditionalSample, boolean isActive) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.price = price;
        this.timeTest = timeTest;
        this.describe = describe;
        this.numberOfSamples = numberOfSamples;
        this.pricePerAdditionalSample = pricePerAdditionalSample;
        this.isActive = isActive;
    }

    public Long getServiceID() {
        return serviceID;
    }

    public void setServiceID(Long serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getTimeTest() {
        return timeTest;
    }

    public void setTimeTest(int timeTest) {
        this.timeTest = timeTest;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    public Double getPricePerAdditionalSample() {
        return pricePerAdditionalSample;
    }

    public void setPricePerAdditionalSample(Double pricePerAdditionalSample) {
        this.pricePerAdditionalSample = pricePerAdditionalSample;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
