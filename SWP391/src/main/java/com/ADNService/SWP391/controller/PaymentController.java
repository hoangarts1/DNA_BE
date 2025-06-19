package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create-vnpay")
    public ResponseEntity<?> createVNPayPayment(@RequestParam("amount") int amount,
                                                @RequestParam("orderId") Long orderId,
                                                @RequestParam("customerId") Long customerId) throws UnsupportedEncodingException {
        String redirectUrl = paymentService.createVNPayPayment(amount, orderId, customerId);
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", redirectUrl);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(HttpServletRequest request) {
        String result = paymentService.handleVNPayReturn(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        response.put("success", result.contains("thành công")); // ví dụ đơn giản
        return ResponseEntity.ok(response);
    }

}
