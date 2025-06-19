package com.ADNService.SWP391.config;

import org.springframework.stereotype.Component;

@Component
public class VNPayConfig {
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String vnp_ReturnUrl = "/vnpay-payment-return";
    public static final String vnp_TmnCode = "C2F1BHSE";
    public static final String vnp_HashSecret = "4VOGR3RAF5LE2B44XN61RZ3VNTD1TBBL";
}
