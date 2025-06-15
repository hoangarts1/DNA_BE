package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private TestOrder testOrder;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    private int amount;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String paymentMethod;
    private String qrUrl;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String paymentStatus;
    private LocalDate paymentTime;

    public Payment() {
    }

    public Payment(int amount, Customer customer, Long paymentId, String paymentMethod, String paymentStatus, LocalDate paymentTime, String qrUrl, TestOrder testOrder) {
        this.amount = amount;
        this.customer = customer;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentTime = paymentTime;
        this.qrUrl = qrUrl;
        this.testOrder = testOrder;
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

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDate paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public TestOrder getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(TestOrder testOrder) {
        this.testOrder = testOrder;
    }
}
