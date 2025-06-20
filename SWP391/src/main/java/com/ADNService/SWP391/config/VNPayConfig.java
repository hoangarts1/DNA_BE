package com.ADNService.SWP391.config;

import org.springframework.stereotype.Component;

@Component
public class VNPayConfig {
    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_RETURN_URL = "http://localhost:8080/api/payments/vnpay-return";
    public static final String VNP_TMNCODE = "C2F1BHSE";
    public static final String VNP_HASH_SECRET = "3DRV4PRUM0HZRFMB9EAAB91TSKRHJ8V6";
    public static final String VNP_VERSION = "2.1.0";
    public static final String VNP_COMMAND = "pay";
    public static final String VNP_CURRENCY_CODE = "VND";
    public static final String VNP_LOCALE = "vn";
}