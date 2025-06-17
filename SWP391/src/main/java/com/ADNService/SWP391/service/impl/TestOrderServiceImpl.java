package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
public class TestOrderServiceImpl implements TestOrderService {

    @Autowired
    private TestOrderRepository testOrderRepository;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private ServiceRepository serviceRepo;

    private TestOrderDTO convertToDTO(TestOrder order) {
        TestOrderDTO dto = new TestOrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setStaffId(order.getStaff() != null ? order.getStaff().getId() : null);
        dto.setServiceId(order.getServices().getServiceID());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setSampleType(order.getSampleType()); // Hình thức lấy mẫu (center/home)
        dto.setResultDeliveryMethod(order.getResultDeliveryMethod());
        dto.setResultDeliverAddress(order.getResultDeliverAddress());
        dto.setKitCode(order.getKitCode());
        dto.setSampleQuantity(order.getSampleQuantity());
        dto.setAmount(order.getAmount());
        return dto;
    }

    private TestOrder convertToEntity(TestOrderDTO dto) {
        if (dto.getCustomerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID is required");
        }
        if (dto.getServiceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ID is required");
        }
        if (dto.getSampleType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sample type (method) is required");
        }

        TestOrder order = new TestOrder();

        if (dto.getOrderId() != null) {
            order.setOrderId(dto.getOrderId());
        }

        order.setCustomer(customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")));

        if (dto.getStaffId() != null) {
            order.setStaff(staffRepo.findById(dto.getStaffId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found")));
        } else {
            order.setStaff(null);
        }

        order.setServices(serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found")));

        order.setOrderDate(dto.getOrderDate());
        order.setSampleType(dto.getSampleType()); // Gán phương thức lấy mẫu
        order.setResultDeliveryMethod(dto.getResultDeliveryMethod());
        order.setResultDeliverAddress(dto.getResultDeliverAddress());
        // Random kitCode chỉ khi sampleType là "home"
        order.setKitCode(dto.getSampleType().equals("home") && dto.getKitCode() == null
                ? "KIT-" + new Random().nextInt(1000000)
                : dto.getKitCode());
        order.setSampleQuantity(dto.getSampleQuantity());
        order.setAmount(dto.getAmount());
        return order;
    }

    @Override
    public List<TestOrderDTO> getAllOrders() {
        return testOrderRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public TestOrderDTO getOrderById(String orderId) {
        TestOrder order = testOrderRepository.findById(Long.valueOf(orderId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return convertToDTO(order);
    }

    @Override
    public TestOrderDTO createOrder(TestOrderDTO dto) {
        dto.setOrderId(null); // Đảm bảo tạo mới
        TestOrder entity = convertToEntity(dto);
        try {
            return convertToDTO(testOrderRepository.save(entity));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create Test Order: Duplicate data or constraint violation: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create Test Order: " + e.getMessage());
        }
    }

    @Override
    public TestOrderDTO updateOrder(String id, TestOrderDTO dto) {
        TestOrder existing = testOrderRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        TestOrder updated = convertToEntity(dto);
        updated.setOrderId(existing.getOrderId());
        return convertToDTO(testOrderRepository.save(updated));
    }

    @Override
    public void deleteOrder(String id) {
        if (!testOrderRepository.existsById(Long.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        testOrderRepository.deleteById(Long.valueOf(id));
    }
}