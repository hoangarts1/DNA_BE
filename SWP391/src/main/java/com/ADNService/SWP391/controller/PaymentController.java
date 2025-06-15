package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create-vnpay")
    public ResponseEntity<?> createVNPayPayment(@RequestParam("amount") int amount,
                                                @RequestParam("orderId") Long orderId,
                                                @RequestParam("customerId") Long customerId) {
        String redirectUrl = paymentService.createVNPayPayment(amount, orderId, customerId);
        return ResponseEntity.ok(redirectUrl);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(HttpServletRequest request) {
        String result = paymentService.handleVNPayReturn(request);
        return ResponseEntity.ok(result);
    }
}
