package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.PaymentDTO;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.Payment;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.repository.PaymentRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TestOrderRepository testOrderRepository;

    @Override
    public PaymentDTO create(PaymentDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId()).orElseThrow(() ->
                new RuntimeException("Customer not found"));
        TestOrder order = testOrderRepository.findById(dto.getOrderId()).orElseThrow(() ->
                new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setCustomer(customer);
        payment.setTestOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setQrUrl(dto.getQrUrl());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaymentTime(dto.getPaymentTime());

        return convertToDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentDTO getById(Long id) {
        return paymentRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<PaymentDTO> getAll() {
        return paymentRepository.findAll()
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO update(Long id, PaymentDTO dto) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Payment not found"));

        Customer customer = customerRepository.findById(dto.getCustomerId()).orElseThrow(() ->
                new RuntimeException("Customer not found"));
        TestOrder order = testOrderRepository.findById(dto.getOrderId()).orElseThrow(() ->
                new RuntimeException("Order not found"));

        payment.setCustomer(customer);
        payment.setTestOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setQrUrl(dto.getQrUrl());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaymentTime(dto.getPaymentTime());

        return convertToDTO(paymentRepository.save(payment));
    }

    @Override
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment with ID " + id + " does not exist");
        }
        paymentRepository.deleteById(id);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setCustomerId(payment.getCustomer().getId());
        dto.setOrderId(payment.getTestOrder().getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setQrUrl(payment.getQrUrl());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentTime(payment.getPaymentTime());
        return dto;
    }
}
