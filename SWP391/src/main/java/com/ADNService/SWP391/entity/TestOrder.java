package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TestOrder")
public class TestOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "staffID")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "serviceID")
    private Services services;
    @OneToMany(mappedBy = "order")
    private List<TestSample> testSamples;

    private LocalDate orderDate;
    private String orderStatus;
    private String sampleType;
    private String resultDeliveryMethod;
    private String resultDeliverAddress;
    private String kitCode;
    private int sampleQuantity;
    private int amount;

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }
}
