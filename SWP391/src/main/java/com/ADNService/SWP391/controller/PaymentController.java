package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.config.VNPayConfig;
import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.entity.Payment;
import com.ADNService.SWP391.repository.PaymentRepository;
import com.ADNService.SWP391.service.PaymentService;
import com.ADNService.SWP391.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create-payment")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(vnp_IpAddr)) {
            vnp_IpAddr = "127.0.0.1";
        }

        int amount = paymentDTO.getAmount() * 100;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.VNP_VERSION);
        vnp_Params.put("vnp_Command", VNPayConfig.VNP_COMMAND);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.VNP_TMNCODE);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", VNPayConfig.VNP_CURRENCY_CODE);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Payment for order: " + paymentDTO.getOrderId());
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", VNPayConfig.VNP_LOCALE);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.VNP_RETURN_URL);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", VNPayUtil.getCurrentDate());

        try {
            String paymentUrl = VNPayUtil.getPaymentUrl(
                    VNPayConfig.VNP_PAY_URL, vnp_Params, VNPayConfig.VNP_HASH_SECRET);

            PaymentDTO newPayment = new PaymentDTO();
            newPayment.setAmount(paymentDTO.getAmount());
            newPayment.setCustomerId(paymentDTO.getCustomerId());
            newPayment.setOrderId(paymentDTO.getOrderId());
            newPayment.setPaymentMethod("VNPAY");
            newPayment.setPaymentStatus("PENDING");
            newPayment.setPaymentTime(LocalDate.now());
            newPayment.setQrUrl(paymentUrl);
            newPayment.setTransactionId(vnp_TxnRef);

            paymentService.create(newPayment);

            Map<String, String> response = new HashMap<>();
            response.put("paymentUrl", paymentUrl);
            return ResponseEntity.ok(response);

        } catch (UnsupportedEncodingException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create payment URL");
            return ResponseEntity.status(500).body(error);
        }
    }
}