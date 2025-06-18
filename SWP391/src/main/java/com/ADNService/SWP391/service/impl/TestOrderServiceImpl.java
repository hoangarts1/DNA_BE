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
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TestOrderServiceImpl implements TestOrderService {

    private final ModelMapper modelMapper;
    private final TestOrderRepository testOrderRepository;
    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;
    private final ServiceRepository serviceRepo;

    @Autowired
    public TestOrderServiceImpl(
            TestOrderRepository testOrderRepository,
            CustomerRepository customerRepo,
            StaffRepository staffRepo,
            ServiceRepository serviceRepo,
            ModelMapper modelMapper
    ) {
        this.testOrderRepository = testOrderRepository;
        this.customerRepo = customerRepo;
        this.staffRepo = staffRepo;
        this.serviceRepo = serviceRepo;
        this.modelMapper = modelMapper;
    }

    private TestOrderDTO convertToDTO(TestOrder order) {
        TestOrderDTO dto = new TestOrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setStaffId(order.getStaff() != null ? order.getStaff().getId() : null);
        dto.setServiceId(order.getServices().getServiceID());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setSampleType(order.getSampleType());
        dto.setResultDeliveryMethod(order.getResultDeliveryMethod());
        dto.setResultDeliverAddress(order.getResultDeliverAddress());
        dto.setKitCode(order.getKitCode());
        dto.setSampleQuantity(order.getSampleQuantity()); // Gán int sang int
        dto.setAmount(order.getAmount()); // Gán int sang int
        return dto;
    }

    private TestOrder convertToEntity(TestOrderDTO dto, TestOrder existing, boolean isUpdate) {
        TestOrder order = existing != null ? existing : new TestOrder();

        // Chỉ kiểm tra các trường bắt buộc khi tạo mới
        if (!isUpdate) {
            if (dto.getCustomerId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID is required");
            }
            if (dto.getServiceId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ID is required");
            }
            if (dto.getSampleType() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sample type (method) is required");
            }

            order.setCustomer(customerRepo.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")));

            order.setServices(serviceRepo.findById(dto.getServiceId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found")));
        }

        // Cập nhật các trường nếu có trong DTO
        if (dto.getOrderId() != null) {
            order.setOrderId(dto.getOrderId());
        }

        if (dto.getStaffId() != null) {
            order.setStaff(staffRepo.findById(dto.getStaffId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found")));
        } else if (isUpdate) {
            order.setStaff(existing.getStaff());
        }

        if (dto.getOrderDate() != null) {
            order.setOrderDate(dto.getOrderDate());
        }

        if (dto.getSampleType() != null) {
            order.setSampleType(dto.getSampleType());
        }

        if (dto.getResultDeliveryMethod() != null) {
            order.setResultDeliveryMethod(dto.getResultDeliveryMethod());
        }

        if (dto.getResultDeliverAddress() != null) {
            order.setResultDeliverAddress(dto.getResultDeliverAddress());
        }

        if (dto.getSampleType() != null && dto.getSampleType().equals("home") && dto.getKitCode() == null) {
            order.setKitCode("KIT-" + new Random().nextInt(1000000));
        } else if (dto.getKitCode() != null) {
            order.setKitCode(dto.getKitCode());
        }

        // Xử lý sampleQuantity
        if (isUpdate && dto.getSampleQuantity() == 0) {
            order.setSampleQuantity(existing.getSampleQuantity()); // Giữ nguyên giá trị hiện tại khi cập nhật và giá trị là 0
        } else {
            if (dto.getSampleQuantity() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sample quantity must be non-negative");
            }
            order.setSampleQuantity(dto.getSampleQuantity());
        }

        // Xử lý amount
        if (isUpdate && dto.getAmount() == 0) {
            order.setAmount(existing.getAmount()); // Giữ nguyên giá trị hiện tại khi cập nhật và giá trị là 0
        } else {
            if (dto.getAmount() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be non-negative");
            }
            order.setAmount(dto.getAmount());
        }

        if (dto.getOrderStatus() != null) {
            order.setOrderStatus(dto.getOrderStatus());
        } else if (!isUpdate) {
            order.setOrderStatus("PENDING");
        }

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
        dto.setOrderId(null);
        TestOrder entity = convertToEntity(dto, null, false);
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
        TestOrder updated = convertToEntity(dto, existing, true);
        return convertToDTO(testOrderRepository.save(updated));
    }

    @Override
    public void deleteOrder(String id) {
        if (!testOrderRepository.existsById(Long.valueOf(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        testOrderRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public List<TestOrderDTO> getOrdersByCustomerId(Long customerId) {
        List<TestOrder> orders = testOrderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}