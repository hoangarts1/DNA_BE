package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.config.VNPayConfig;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.Payment;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.repository.PaymentRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;

    public String createVNPayPayment(int amount, Long orderId, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        TestOrder order = testOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));



        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_OrderInfo = "Thanh toan don hang " + orderId;
        String vnp_IpAddr = "127.0.0.1";
        String vnp_Amount = String.valueOf(amount * 100);
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String value = vnp_Params.get(fieldName);
            hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
            query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
        }

        String queryUrl = query.substring(0, query.length() - 1);
        String secureHash = HmacSHA512(VNPayConfig.vnp_HashSecret, hashData.substring(0, hashData.length() - 1));
        queryUrl += "&vnp_SecureHash=" + secureHash;

        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public String handleVNPayReturn(HttpServletRequest request) {
        Map<String, String[]> fields = request.getParameterMap();
        Map<String, String> vnpData = new HashMap<>();
        for (Map.Entry<String, String[]> entry : fields.entrySet()) {
            vnpData.put(entry.getKey(), entry.getValue()[0]);
        }

        String receivedHash = vnpData.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(vnpData.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String field : fieldNames) {
            hashData.append(field).append('=').append(vnpData.get(field)).append('&');
        }

        String calculatedHash = HmacSHA512(VNPayConfig.vnp_HashSecret, hashData.substring(0, hashData.length() - 1));

        if (calculatedHash.equals(receivedHash)) {
            // Lưu vào DB
            Long orderId = Long.valueOf(vnpData.get("vnp_TxnRef")); // hoặc mapping riêng
            Payment payment = new Payment();
            payment.setAmount(Integer.parseInt(vnpData.get("vnp_Amount")) / 100);
            payment.setPaymentMethod("VNPAY");
            payment.setPaymentStatus("SUCCESS");
            payment.setPaymentTime(LocalDate.now());
            payment.setQrUrl(""); // hoặc lưu link nếu có
            payment.setCustomer(customerRepository.findById(1L).orElse(null)); // xử lý đúng thực tế
            payment.setTestOrder(testOrderRepository.findById(1L).orElse(null));
            paymentRepository.save(payment);

            return "Thanh toán thành công!";
        }

        return "Thanh toán thất bại!";
    }

    private String HmacSHA512(String key, String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo HmacSHA512", e);
        }
    }
}
