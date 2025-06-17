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

    @Column(name = "service_purpose", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String servicePurpose;

    @Column(name = "service_blog", nullable = true, columnDefinition = "NVARCHAR(255)")
    private String serviceBlog;

    @Column(name = "time_test", nullable = false)
    private int timeTest;

    @Column(name = "service_price", nullable = false)
    private double servicePrice;

    // Constructors
    public Services() {
    }

    public Services(Long serviceID, String serviceName, String servicePurpose, int timeTest,
        String serviceBlog, double servicePrice) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.servicePurpose = servicePurpose;
        this.timeTest = timeTest;
        this.serviceBlog = serviceBlog;
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

    public String getServicePurpose() {
        return servicePurpose;
    }

    public void setServicePurpose(String servicePurpose) {
        this.servicePurpose = servicePurpose;
    }

    public String getServiceBlog() {
        return serviceBlog;
    }

    public void setServiceBlog(String serviceBlog) {
        this.serviceBlog = serviceBlog;
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
