package com.ADNService.SWP391.dto;

public class ServiceDTO {
    private Long serviceID;
    private String serviceName;
    private String servicePurpose;
    private int timeTest;
    private String serviceBlog;
    private double price;
    private int quantity;
    private int numberOfSample;

    public ServiceDTO() {
    }

    public ServiceDTO(Long serviceID, String serviceName, String servicePurpose, int quantity, double price, int timeTest, int numberOfSample, String serviceBlog) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.servicePurpose = servicePurpose;
        this.quantity = quantity;
        this.price = price;
        this.timeTest = timeTest;
        this.numberOfSample = numberOfSample;
        this.serviceBlog = serviceBlog;
    }

    public int getNumberOfSample() {
        return numberOfSample;
    }

    public void setNumberOfSample(int numberOfSample) {
        this.numberOfSample = numberOfSample;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getServiceBlog() {
        return serviceBlog;
    }

    public void setServiceBlog(String serviceBlog) {
        this.serviceBlog = serviceBlog;
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

    public String getServicePurpose() {
        return servicePurpose;
    }

    public void setServicePurpose(String servicePurpose) {
        this.servicePurpose = servicePurpose;
    }

    public int getTimeTest() {
        return timeTest;
    }

    public void setTimeTest(int timeTest) {
        this.timeTest = timeTest;
    }
}
