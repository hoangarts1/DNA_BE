package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.entity.TestSample;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestOrderService;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestOrderServiceImpl implements TestOrderService {
    @Autowired
    private TestSampleRepository testSampleRepository;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn xét nghiệm"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);

            // Font mặc định hỗ trợ UTF-8 (tốt trên máy có sẵn Helvetica hoặc tương đương)
            document.setFontSize(11);
            document.setMargins(30, 30, 30, 30);

            // === Tiêu đề
            Paragraph title = new Paragraph("ĐƠN ĐĂNG KÝ PHÂN TÍCH ADN")
                    .setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // === Thông tin khách hàng
            Customer customer = order.getCustomer();
            Account acc = customer.getAccount();

            document.add(new Paragraph("I. Thông tin khách hàng").setBold());

            Table customerTable = new Table(UnitValue.createPercentArray(new float[]{25, 75})).useAllAvailableWidth();
            customerTable.addCell("Họ tên:");
            customerTable.addCell(acc.getFullName());
            customerTable.addCell("Email:");
            customerTable.addCell(acc.getEmail());
            customerTable.addCell("Số điện thoại:");
            customerTable.addCell(acc.getPhone());
            customerTable.addCell("Địa chỉ:");
            customerTable.addCell(customer.getAddress());
            customerTable.addCell("Giới tính:");
            customerTable.addCell(customer.getGender());
            customerTable.addCell("CCCD:");
            customerTable.addCell(customer.getCccd());
            customerTable.addCell("Ngày cấp:");
            customerTable.addCell(customer.getDateOfIssue() != null ? customer.getDateOfIssue().toString() : "");
            customerTable.addCell("Nơi cấp:");
            customerTable.addCell(customer.getPlaceOfIssue());
            document.add(customerTable);
            document.add(new Paragraph(" "));

            // === Thông tin xét nghiệm
            document.add(new Paragraph("II. Thông tin xét nghiệm").setBold());

            Table orderTable = new Table(UnitValue.createPercentArray(new float[]{35, 65})).useAllAvailableWidth();
            orderTable.addCell("Mã đơn:");
            orderTable.addCell(order.getOrderId().toString());
            orderTable.addCell("Ngày đăng ký:");
            orderTable.addCell(order.getOrderDate() != null ? order.getOrderDate().toString() : "");
            orderTable.addCell("Dịch vụ:");
            orderTable.addCell(order.getServices().getServiceName());
            orderTable.addCell("Số mẫu:");
            orderTable.addCell(String.valueOf(order.getSampleQuantity()));
            orderTable.addCell("Phương thức lấy mẫu:");
            orderTable.addCell(order.getSampleMethod());
            orderTable.addCell("Phương thức trả kết quả:");
            orderTable.addCell(order.getResultDeliveryMethod());
            orderTable.addCell("Địa chỉ trả kết quả:");
            orderTable.addCell(order.getResultDeliverAddress());
            orderTable.addCell("Tổng tiền:");
            orderTable.addCell(order.getAmount() + " VND");
            document.add(orderTable);
            document.add(new Paragraph(" "));

            // === Danh sách mẫu xét nghiệm
            List<TestSample> samples = testSampleRepository.findByOrder_OrderId(order.getOrderId());
            document.add(new Paragraph("III. Danh sách mẫu xét nghiệm").setBold());

            Table sampleTable = new Table(new float[]{1, 3, 2, 2, 2, 2, 2, 3}).useAllAvailableWidth();
            sampleTable.addHeaderCell("STT");
            sampleTable.addHeaderCell("Họ tên");
            sampleTable.addHeaderCell("Năm sinh");
            sampleTable.addHeaderCell("Giới tính");
            sampleTable.addHeaderCell("Mối quan hệ");
            sampleTable.addHeaderCell("Loại mẫu");
            sampleTable.addHeaderCell("Ngày thu mẫu");
            sampleTable.addHeaderCell("Ghi chú");

            int index = 1;
            for (TestSample sample : samples) {
                sampleTable.addCell(String.valueOf(index++));
                sampleTable.addCell(sample.getName());
                sampleTable.addCell(sample.getDateOfBirth() != null ? sample.getDateOfBirth().toString() : "");
                sampleTable.addCell(sample.getGender());
                sampleTable.addCell(sample.getRelationship());
                sampleTable.addCell(sample.getSampleType());
                sampleTable.addCell(sample.getNumberOfSample() != null ? sample.getNumberOfSample().toString() : "");
                sampleTable.addCell(sample.getKitCode() != null ? sample.getKitCode() : "");
            }
            document.add(sampleTable);
            document.add(new Paragraph(" "));

            // === Chữ ký
            LocalDate today = LocalDate.now();
            String dateStr = "......, ngày " + today.getDayOfMonth() + " tháng " + today.getMonthValue() + " năm " + today.getYear();
            document.add(new Paragraph(dateStr).setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Người đăng ký").setTextAlignment(TextAlignment.RIGHT).setBold());
            document.add(new Paragraph(acc.getFullName()).setTextAlignment(TextAlignment.RIGHT).setItalic());

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi tạo PDF: " + e.getMessage());
        }
    }

}
