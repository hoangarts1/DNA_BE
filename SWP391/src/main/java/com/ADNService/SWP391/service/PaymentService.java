package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface PaymentService {
        String createVNPayPayment(int amount, Long orderId, Long customerId) throws UnsupportedEncodingException;
        String handleVNPayReturn(HttpServletRequest request);
}
