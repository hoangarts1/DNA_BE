package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestOrderDTO;

import java.util.List;

public interface TestOrderService {
    List<TestOrderDTO> getAllOrders();
    TestOrderDTO getOrderById(String id);
    TestOrderDTO createOrder(TestOrderDTO dto);
    TestOrderDTO updateOrder(String id, TestOrderDTO dto);
    void deleteOrder(String id);
    List<TestOrderDTO> getOrdersByCustomerId(Long customerId);
    byte[] generateTestOrderPdf(Long id);

    TestOrderDTO getTestOrderById(Long orderId);
}
