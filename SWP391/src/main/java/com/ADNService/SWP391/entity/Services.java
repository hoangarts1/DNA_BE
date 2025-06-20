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

    // Constructors
    public Services() {
    }

    public Services(Long serviceID, String serviceName, String servicePurpose, int timeTest,
                    String describe, double servicePrice) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.serviceType = servicePurpose;
        this.timeTest = timeTest;
        this.describe = describe;
        this.servicePrice = servicePrice;
    }

    // Getters and Setters
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

    public void setServiceType(String servicePurpose) {
        this.serviceType = servicePurpose;
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

}
