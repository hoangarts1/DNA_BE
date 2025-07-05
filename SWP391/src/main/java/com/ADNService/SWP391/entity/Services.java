package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceID;

    @Column(name = "service_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String serviceName;

    @Column(name = "service_type", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String serviceType;

    @Column(name = "describe", nullable = true, columnDefinition = "NVARCHAR(255)")
    private String describe;

    @Column(name = "time_test", nullable = false)
    private int timeTest;

    @Column(name = "service_price", nullable = false)
    private double servicePrice;

    @Column(name = "number_of_samples", nullable = false, columnDefinition = "INT DEFAULT 2")
    private int numberOfSamples = 2;

    @Column(name = "price_per_additional_sample", nullable = true)
    private Double pricePerAdditionalSample;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Services() {
    }

    public Services(Long serviceID, String serviceName, String serviceType, int timeTest,
                    String describe, double servicePrice, int numberOfSamples, Double pricePerAdditionalSample, boolean isActive) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.timeTest = timeTest;
        this.describe = describe;
        this.servicePrice = servicePrice;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getTimeTest() {
        return timeTest;
    }

    public void setTimeTest(int timeTest) {
        this.timeTest = timeTest;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
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
