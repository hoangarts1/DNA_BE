package com.ADNService.SWP391.util;

import com.ADNService.SWP391.config.VNPayConfig;
import com.ADNService.SWP391.dto.PaymentDTO;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class VNPayUtil {

    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    public static String createPaymentUrl(PaymentDTO dto) {
        try {
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", VNPayConfig.VNP_VERSION);
            vnpParams.put("vnp_Command", VNPayConfig.VNP_COMMAND);
            vnpParams.put("vnp_TmnCode", VNPayConfig.VNP_TMNCODE);
            vnpParams.put("vnp_Amount", String.valueOf(dto.getAmount() * 100));
            vnpParams.put("vnp_CreateDate", getCurrentDate());
            vnpParams.put("vnp_CurrCode", VNPayConfig.VNP_CURRENCY_CODE);
            vnpParams.put("vnp_IpAddr", "127.0.0.1");
            vnpParams.put("vnp_Locale", VNPayConfig.VNP_LOCALE);
            vnpParams.put("vnp_OrderInfo", "Thanh toan don hang: " + dto.getOrderId());
            vnpParams.put("vnp_OrderType", "billpayment");
            vnpParams.put("vnp_ReturnUrl", VNPayConfig.VNP_RETURN_URL);
            vnpParams.put("vnp_TxnRef", dto.getTransactionId());

            return getPaymentUrl(VNPayConfig.VNP_PAY_URL, vnpParams, VNPayConfig.VNP_HASH_SECRET);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Cannot encode VNP params", e);
        }
    }

    public static String getPaymentUrl(String baseUrl, Map<String, String> vnpParams, String vnp_HashSecret) throws UnsupportedEncodingException {
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return baseUrl + "?" + query;
    }

    public static String hmacSHA512(String key, String data) {
        try {
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            mac.init(secretKey);
            byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (Exception ex) {
            throw new RuntimeException("Error while calculating HMAC SHA512", ex);
        }
    }

    public static String bytesToHex(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
