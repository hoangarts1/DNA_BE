package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.dto.CustomerDTO;
import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestOrderService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestOrderServiceImpl implements TestOrderService {
    private final TestOrderRepository testOrderRepository;
    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;
    private final ServiceRepository serviceRepo;

    @Autowired
    public TestOrderServiceImpl(
            TestOrderRepository testOrderRepository,
            CustomerRepository customerRepo,
            StaffRepository staffRepo,
            ServiceRepository serviceRepo
    ) {
        this.testOrderRepository = testOrderRepository;
        this.customerRepo = customerRepo;
        this.staffRepo = staffRepo;
        this.serviceRepo = serviceRepo;
    }

    public TestOrderDTO getTestOrderById(Long orderId) {
        TestOrder order = testOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Customer customer = order.getCustomer();
        Account account = customer.getAccount();  // Truy xuất Account từ Customer

        TestOrderDTO dto = new TestOrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(customer.getId());
        dto.setOrderDate(order.getOrderDate());
        // ... các field khác

        // Set CustomerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAddress(customer.getAddress());
        // ... các field khác
        dto.setCustomer(customerDTO);

        // Set AccountDTO
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setFullName(account.getFullName());
        // ... các field khác
        dto.setAccount(accountDTO);

        return dto;
    }




    private TestOrderDTO convertToDTO(TestOrder order) {
        TestOrderDTO dto = new TestOrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setServiceId(order.getServices().getServiceID());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setSampleMethod(order.getSampleMethod());
        dto.setResultDeliveryMethod(order.getResultDeliveryMethod());
        dto.setResultDeliverAddress(order.getResultDeliverAddress());
        dto.setSampleQuantity(order.getSampleQuantity());
        dto.setAmount(order.getAmount());

        dto.setRegistrationStaffId(order.getRegistrationStaff() != null ? order.getRegistrationStaff().getId() : null);
        dto.setTestingStaffId(order.getTestingStaff() != null ? order.getTestingStaff().getId() : null);

        return dto;
    }

    private TestOrder convertToEntity(TestOrderDTO dto, TestOrder existing, boolean isUpdate) {
        TestOrder order = existing != null ? existing : new TestOrder();

        if (!isUpdate) {
            if (dto.getCustomerId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID is required");
            }
            if (dto.getServiceId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ID is required");
            }
            if (dto.getSampleMethod() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sample type is required");
            }

            order.setCustomer(customerRepo.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")));

            order.setServices(serviceRepo.findById(dto.getServiceId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found")));
        }

        if (dto.getOrderId() != null) {
            order.setOrderId(dto.getOrderId());
        }

        if (dto.getRegistrationStaffId() != null) {
            order.setRegistrationStaff(
                    staffRepo.findById(dto.getRegistrationStaffId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration staff not found")));
        } else if (isUpdate && existing.getRegistrationStaff() != null) {
            order.setRegistrationStaff(existing.getRegistrationStaff());
        }

        if (dto.getTestingStaffId() != null) {
            order.setTestingStaff(
                    staffRepo.findById(dto.getTestingStaffId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Testing staff not found")));
        } else if (isUpdate && existing.getTestingStaff() != null) {
            order.setTestingStaff(existing.getTestingStaff());
        }

        if (dto.getOrderDate() != null) {
            order.setOrderDate(dto.getOrderDate());
        }

        if (dto.getSampleMethod() != null) {
            order.setSampleMethod(dto.getSampleMethod());
        }

        if (dto.getResultDeliveryMethod() != null) {
            order.setResultDeliveryMethod(dto.getResultDeliveryMethod());
        }

        if (dto.getResultDeliverAddress() != null) {
            order.setResultDeliverAddress(dto.getResultDeliverAddress());
        }

        if (isUpdate && dto.getSampleQuantity() == 0) {
            order.setSampleQuantity(existing.getSampleQuantity());
        } else {
            if (dto.getSampleQuantity() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sample quantity must be non-negative");
            }
            order.setSampleQuantity(dto.getSampleQuantity());
        }

        if (isUpdate && dto.getAmount() == 0) {
            order.setAmount(existing.getAmount());
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
        return testOrderRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
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

    @Override
    public byte[] generateTestOrderPdf(Long id) {
        TestOrder order = testOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Tiêu đề
            document.add(new Paragraph("ĐƠN YÊU CẦU PHÂN TÍCH ADN").setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Mã đơn: " + order.getOrderId()).setTextAlignment(TextAlignment.RIGHT));

            // Thông tin khách hàng
            document.add(new Paragraph("1. Thông tin khách hàng"));
            document.add(new Paragraph("Họ tên: " + order.getCustomer().getAccount().getFullName()));
            document.add(new Paragraph("Email: " + order.getCustomer().getAccount().getEmail()));
            document.add(new Paragraph("Số điện thoại: " + order.getCustomer().getAccount().getPhone()));

            // Thông tin dịch vụ
            document.add(new Paragraph("\n2. Thông tin xét nghiệm"));
            document.add(new Paragraph("Dịch vụ: " + order.getServices().getServiceName()));
            document.add(new Paragraph("Số mẫu: " + order.getSampleQuantity()));
            document.add(new Paragraph("Ngày đăng ký: " + order.getOrderDate()));
            document.add(new Paragraph("Phương thức lấy mẫu: " + order.getSampleMethod()));
            document.add(new Paragraph("Phương thức trả kết quả: " + order.getResultDeliveryMethod()));
            document.add(new Paragraph("Địa chỉ trả kết quả: " + order.getResultDeliverAddress()));
            document.add(new Paragraph("Tổng tiền: " + order.getAmount() + " VND"));

            document.add(new Paragraph("\nChữ ký khách hàng: ________________________"));

            document.close();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating PDF: " + e.getMessage());
        }

        return out.toByteArray();
    }

}
