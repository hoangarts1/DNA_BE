package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.config.VNPayConfig;
import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.Payment;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.repository.PaymentRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.PaymentService;
import com.ADNService.SWP391.util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;

    @Override
    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomer_Id(customerId);
    }

    @Override
    public void create(PaymentDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setCustomer(customer);
        payment.setTestOrder(order);
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaymentTime(dto.getPaymentTime() != null ? dto.getPaymentTime() : LocalDate.now());
        payment.setQrUrl(dto.getQrUrl());
        payment.setTransactionId(dto.getTransactionId());

        paymentRepository.save(payment);
    }

}