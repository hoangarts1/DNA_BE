package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.entity.Payment;

import java.util.List;

public interface PaymentService {
        void create(PaymentDTO dto);
        List<Payment> getPaymentsByCustomerId(Long customerId);
        void markPaymentSuccess(String transactionId);
        void markPaymentFailed(String transactionId, String responseCode);

}