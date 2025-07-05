package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.entity.Payment;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.repository.PaymentRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;

    @Override
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByOrder_Customer_Id(customerId); // Sửa lại method này nếu cần
    }

    @Override
    public void create(PaymentDTO dto) {
        if (!customerRepository.existsById(dto.getCustomerId())) {
            throw new RuntimeException("Customer with ID " + dto.getCustomerId() + " does not exist");
        }


        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setTestOrder(order);

        payment.setCustomerId(order.getCustomer().getId());

        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaymentTime(dto.getPaymentTime() != null ? dto.getPaymentTime() : LocalDate.now());
        payment.setQrUrl(dto.getQrUrl());
        payment.setTransactionId(dto.getTransactionId());

        paymentRepository.save(payment);
    }

    @Override
    public void markPaymentSuccess(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán"));

        payment.setPaymentStatus("SUCCESS");
        paymentRepository.save(payment);

        TestOrder order = payment.getTestOrder();
        if (order != null) {
            order.setOrderStatus("CONFIRM");
            testOrderRepository.save(order);
        }
    }


    @Override
    public void markPaymentFailed(String transactionId, String responseCode) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán"));

        if ("24".equals(responseCode)) {
            payment.setPaymentStatus("CANCELLED");
        } else {
            payment.setPaymentStatus("FAILED");
        }

        paymentRepository.save(payment);
    }


}
