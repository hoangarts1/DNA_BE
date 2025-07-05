package com.ADNService.SWP391.dto;

import com.ADNService.SWP391.entity.Payment;

import java.time.LocalDate;
public class PaymentDTO {
    private Long paymentId;
    private Long orderId;
    private Long customerId;
    private int amount;
    private String paymentMethod;
    private String qrUrl;
    private String paymentStatus;
    private LocalDate paymentTime;
    private String transactionId;

    public PaymentDTO() {}

    public PaymentDTO(int amount, Long customerId, Long orderId, Long paymentId, String paymentMethod, String paymentStatus, LocalDate paymentTime, String qrUrl, String transactionId) {
        this.amount = amount;
        this.customerId = customerId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentTime = paymentTime;
        this.qrUrl = qrUrl;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getQrUrl() { return qrUrl; }
    public void setQrUrl(String qrUrl) { this.qrUrl = qrUrl; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDate getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDate paymentTime) { this.paymentTime = paymentTime; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public static PaymentDTO fromEntity(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(payment.getTestOrder().getOrderId());
        dto.setCustomerId(payment.getCustomerId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentTime(payment.getPaymentTime());
        dto.setQrUrl(payment.getQrUrl());
        dto.setTransactionId(payment.getTransactionId());
        return dto;
    }

}
