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

    @Column(name = "customer_id") // chỉ lưu ID, KHÔNG liên kết
    private Long customerId;

    private int amount;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String paymentMethod;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String paymentStatus;

    private LocalDate paymentTime;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String qrUrl;

    private String transactionId;

    // --- Getters & Setters ---

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public TestOrder getTestOrder() {
        return order;
    }

    public void setTestOrder(TestOrder testOrder) {
        this.order = testOrder;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
