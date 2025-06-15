package com.ADNService.SWP391.config;

import org.springframework.stereotype.Component;

@Component
public class VNPayConfig {
    public static final String vnp_Version = "2.1.0";
    public static final String vnp_Command = "pay";
    public static final String vnp_TmnCode = "YOUR_TMNCODE";
    public static final String vnp_HashSecret = "YOUR_SECRET";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String vnp_ReturnUrl = "http://localhost:8080/api/payment/vnpay-return";
}
