package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private TestOrder order;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private int amount;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String paymentMethod;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String paymentStatus;

    private LocalDate paymentTime;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String qrUrl;

    private String transactionId;

    public Payment() {
    }

    public Payment(int amount, Customer customer, Long paymentId, String paymentMethod, String paymentStatus, LocalDate paymentTime, String qrUrl, TestOrder testOrder, String transactionId) {
        this.amount = amount;
        this.customer = customer;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentTime = paymentTime;
        this.qrUrl = qrUrl;
        this.order = testOrder;
        this.transactionId = transactionId;
    }

    public TestOrder getOrder() {
        return order;
    }

    public void setOrder(TestOrder order) {
        this.order = order;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
        return order;
    }

    public void setTestOrder(TestOrder testOrder) {
        this.order = testOrder;
    }
}
