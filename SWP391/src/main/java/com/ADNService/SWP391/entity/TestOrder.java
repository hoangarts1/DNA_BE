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
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;

    @OneToMany(mappedBy = "order")
    private List<TestSample> testSamples;

    @ManyToOne
    @JoinColumn(name = "registration_staff_id")
    private Staff registrationStaff;

    @ManyToOne
    @JoinColumn(name = "testing_staff_id")
    private Staff testingStaff;

    private LocalDate orderDate;

    private String orderStatus;
    private String sampleMethod;
    private String resultDeliveryMethod;
    private String resultDeliverAddress;

    private int sampleQuantity;
    private int amount;

// Getter & Setter


    public List<TestSample> getTestSamples() {
        return testSamples;
    }

    public void setTestSamples(List<TestSample> testSamples) {
        this.testSamples = testSamples;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public Staff getRegistrationStaff() {
        return registrationStaff;
    }

    public void setRegistrationStaff(Staff registrationStaff) {
        this.registrationStaff = registrationStaff;
    }

    public Staff getTestingStaff() {
        return testingStaff;
    }

    public void setTestingStaff(Staff testingStaff) {
        this.testingStaff = testingStaff;
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

    public String getSampleMethod() {
        return sampleMethod;
    }

    public void setSampleMethod(String sampleType) {
        this.sampleMethod = sampleType;
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
