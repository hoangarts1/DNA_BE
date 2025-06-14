package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentService {
        String createVNPayPayment(int amount, Long orderId, Long customerId);
        String handleVNPayReturn(HttpServletRequest request);
}
