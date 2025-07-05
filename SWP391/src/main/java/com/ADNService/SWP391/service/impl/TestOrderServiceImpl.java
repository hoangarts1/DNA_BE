package com.ADNService.SWP391.service.impl;


import java.text.SimpleDateFormat;
import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.dto.CustomerDTO;
import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.entity.*;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestOrderService;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.function.BiConsumer;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn xét nghiệm"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont(
                    "src/main/resources/fonts/DejaVuSans.ttf",
                    "Identity-H",
                    PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
            );
            document.setFont(font);


            document.setMargins(20, 20, 20, 20);

            String serviceType = order.getServices().getServiceType();

            if (serviceType.equalsIgnoreCase("Dân sự")) {
                generateFormDanSu(document, order);
            } else if (serviceType.equalsIgnoreCase("Hành chính")) {
                generateFormHanhChinh(document, order);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loại dịch vụ không hợp lệ");
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi tạo PDF: " + e.getMessage());
        }


    }
    private void generateFormDanSu(Document document, TestOrder order) {
        Customer customer = order.getCustomer();
        Account acc = customer.getAccount();

        // === TIÊU ĐỀ ===
        document.add(new Paragraph("ĐƠN YÊU CẦU PHÂN TÍCH ADN")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16)
                .setBold());

        document.add(new Paragraph("Kính gửi: Viện Công Nghệ ADN và Phân Tích Di Truyền")
                .setItalic()
                .setFontSize(11)
                .setMarginTop(10));

        // === THÔNG TIN KHÁCH HÀNG ===
        document.add(new Paragraph(" ").setMarginTop(10));

        String fullName = acc.getFullName();
        String gender = customer.getGender();
        String phone = acc.getPhone();
        String email = acc.getEmail();
        String address = customer.getAddress();
        String cccd = customer.getCccd();
        String placeOfIssue = customer.getPlaceOfIssue();
        String dateOfIssue = customer.getDateOfIssue() != null ? customer.getDateOfIssue().toString() : "";

        document.add(new Paragraph()
                .add("Tôi tên là: ")
                .add(new Text(fullName).setBold().setUnderline())
                .add("            Giới tính: ")
                .add(new Text(gender).setBold().setUnderline())
                .setMarginTop(5));

        document.add(new Paragraph()
                .add("Địa chỉ: ")
                .add(new Text(address).setUnderline()));

        document.add(new Paragraph()
                .add("CMND/CCCD/Passport: ")
                .add(new Text(cccd).setUnderline())
                .add("     ngày cấp: ")
                .add(new Text(dateOfIssue).setUnderline())
                .add("     nơi cấp: ")
                .add(new Text(placeOfIssue).setUnderline()));

        document.add(new Paragraph()
                .add("Số điện thoại: ")
                .add(new Text(phone).setUnderline())
                .add("     Email/Zalo: ")
                .add(new Text(email).setUnderline()));

        // === LỜI ĐỀ NGHỊ ===
        document.add(new Paragraph("Đề nghị Viện phân tích ADN và xác định mối quan hệ huyết thống cho những người cung cấp mẫu dưới đây:")
                .setMarginTop(10));

        // === BẢNG MẪU ===
        Table sampleTable = new Table(new float[]{1, 3, 2, 2, 2, 2, 3}).useAllAvailableWidth();
        sampleTable.addHeaderCell("STT");
        sampleTable.addHeaderCell("Họ tên");
        sampleTable.addHeaderCell("Năm sinh");
        sampleTable.addHeaderCell("Giới tính");
        sampleTable.addHeaderCell("Mối quan hệ");
        sampleTable.addHeaderCell("Loại mẫu");
        sampleTable.addHeaderCell("Ghi chú");

        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(order.getOrderId());
        int stt = 1;
        for (TestSample s : samples) {
            sampleTable.addCell(String.valueOf(stt++));
            sampleTable.addCell(s.getName());
            sampleTable.addCell(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "");
            sampleTable.addCell(s.getGender());
            sampleTable.addCell(s.getRelationship());
            sampleTable.addCell(s.getSampleType() != null ? s.getSampleType().getSampleType() : "");
            sampleTable.addCell("");
        }
        document.add(sampleTable.setMarginTop(10));

        // === CAM KẾT ===
        document.add(new Paragraph("Cam kết:").setBold().setMarginTop(10));
        document.add(new Paragraph("""
            1. Tôi tự nguyện đề nghị xét nghiệm ADN và chấp nhận chi phí xét nghiệm.
            2. Tôi xác nhận thông tin tôi cung cấp là trung thực.
            3. Tôi đã được giải thích rõ mục đích, ý nghĩa, phạm vi và tính chất pháp lý của kết quả xét nghiệm ADN.
            4. Tôi đồng ý ký vào phiếu này và chịu trách nhiệm về mọi thông tin kê khai.
            """));

        document.add(new Paragraph("[ ] Tôi đã đọc và đồng ý")
                .setFontColor(ColorConstants.BLUE)
                .setMarginTop(5));

        // === HÌNH THỨC NHẬN KẾT QUẢ ===
        document.add(new Paragraph("Hình thức nhận kết quả:")
                .setBold()
                .setMarginTop(10));
        String deliveryMethod = order.getResultDeliveryMethod();
        document.add(new Paragraph(deliveryMethod));

        // === NGÀY THÁNG VÀ CHỮ KÝ ===
        LocalDate today = LocalDate.now();
        document.add(new Paragraph(String.format("TP. HCM, ngày %02d tháng %02d năm %d", today.getDayOfMonth(), today.getMonthValue(), today.getYear()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10)
                .setMarginTop(10));

        Table signTable = new Table(new float[]{3, 3, 3}).useAllAvailableWidth().setMarginTop(10);
        signTable.addCell(new Cell().add(new Paragraph("NGƯỜI TIẾP NHẬN MẪU\n(ký và ghi rõ họ tên)").setFontSize(9).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

        Cell fingerprintCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        fingerprintCell.add(new Paragraph("NGÓN TRỎ PHẢI\n(Đối với hành chính)").setFontSize(9));

//        String fingerprintBase64 = getFingerprintBase64(order.getOrderId());
//        if (fingerprintBase64 != null) {
//            try {
//                byte[] fingerprintBytes = Base64.getDecoder().decode(fingerprintBase64);
//                ImageData fingerprintData = ImageDataFactory.create(fingerprintBytes);
//                Image fingerprintImage = new Image(fingerprintData);
//                fingerprintImage.setAutoScale(true).setMaxHeight(60).setMaxWidth(60);
//                fingerprintCell.add(fingerprintImage);
//            } catch (Exception e) {
//                fingerprintCell.add(new Paragraph("(Vân tay lỗi)").setFontColor(ColorConstants.RED));
//            }
//        } else {
//            fingerprintCell.add(new Paragraph("(Chưa có)").setFontColor(ColorConstants.GRAY));
//        }
//        signTable.addCell(fingerprintCell);

        signTable.addCell(new Cell()
                .add(new Paragraph("NGƯỜI YÊU CẦU PHÂN TÍCH\n(ký và ghi rõ họ tên)").setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));

        document.add(signTable);
        document.add(new Paragraph("Trang 01/02")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(8)
                .setMarginTop(10));

        // === TRANG 2 ===
        document.getPdfDocument().addNewPage();
        document.setMargins(20, 20, 20, 20);

        document.add(new Paragraph("ĐIỀU KHOẢN")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10));

        document.add(new Paragraph(getRulesText()).setFontSize(9).setMarginTop(10));

        document.add(new Paragraph("NGƯỜI YÊU CẦU PHÂN TÍCH ĐÃ ĐỌC KỸ VÀ ĐỒNG Ý VỚI CÁC ĐIỀU KHOẢN TRÊN")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));

        document.add(new Paragraph("(ký và ghi rõ họ tên)")
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(" ").setHeight(20));
        document.add(new Paragraph("Trang 02/02")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(8)
                .setMarginTop(10));
    }



    private void generateFormHanhChinh(Document document, TestOrder order) {
        Customer customer = order.getCustomer();
        Account acc = customer.getAccount();

        // === TIÊU ĐỀ ===
        document.add(new Paragraph("ĐƠN YÊU CẦU PHÂN TÍCH ADN")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16)
                .setBold());

        document.add(new Paragraph("Kính gửi: Viện Công Nghệ ADN và Phân Tích Di Truyền")
                .setItalic()
                .setFontSize(11)
                .setMarginTop(10));

        // === THÔNG TIN KHÁCH HÀNG ===
        document.add(new Paragraph(" ").setMarginTop(10));

        String fullName = acc.getFullName();
        String gender = customer.getGender();
        String phone = acc.getPhone();
        String email = acc.getEmail();
        String address = customer.getAddress();
        String cccd = customer.getCccd();
        String placeOfIssue = customer.getPlaceOfIssue();
        String dateOfIssue = customer.getDateOfIssue() != null ? customer.getDateOfIssue().toString() : "";

        document.add(new Paragraph()
                .add("Tôi tên là: ")
                .add(new Text(fullName).setBold().setUnderline())
                .add("            Giới tính: ")
                .add(new Text(gender).setBold().setUnderline())
                .setMarginTop(5));

        document.add(new Paragraph()
                .add("Địa chỉ: ")
                .add(new Text(address).setUnderline()));

        document.add(new Paragraph()
                .add("CMND/CCCD/Passport: ")
                .add(new Text(cccd).setUnderline())
                .add("     ngày cấp: ")
                .add(new Text(dateOfIssue).setUnderline())
                .add("     nơi cấp: ")
                .add(new Text(placeOfIssue).setUnderline()));

        document.add(new Paragraph()
                .add("Số điện thoại: ")
                .add(new Text(phone).setUnderline())
                .add("     Email/Zalo: ")
                .add(new Text(email).setUnderline()));

        // === LỜI ĐỀ NGHỊ ===
        document.add(new Paragraph("Đề nghị Viện phân tích ADN và xác định mối quan hệ huyết thống cho những người cung cấp mẫu dưới đây:")
                .setMarginTop(10));

        // === BẢNG MẪU ===
        Table sampleTable = new Table(new float[]{1, 3, 2, 2, 2, 2, 3}).useAllAvailableWidth();
        sampleTable.addHeaderCell("STT");
        sampleTable.addHeaderCell("Họ tên");
        sampleTable.addHeaderCell("Năm sinh");
        sampleTable.addHeaderCell("Giới tính");
        sampleTable.addHeaderCell("Mối quan hệ");
        sampleTable.addHeaderCell("Loại mẫu");
        sampleTable.addHeaderCell("Ghi chú");

        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(order.getOrderId());
        int stt = 1;
        for (TestSample s : samples) {
            sampleTable.addCell(String.valueOf(stt++));
            sampleTable.addCell(s.getName());
            sampleTable.addCell(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "");
            sampleTable.addCell(s.getGender());
            sampleTable.addCell(s.getRelationship());
            sampleTable.addCell(s.getSampleType() != null ? s.getSampleType().getSampleType() : "");
            sampleTable.addCell("");
        }
        document.add(sampleTable.setMarginTop(10));

        // === CAM KẾT ===
        document.add(new Paragraph("Cam kết:").setBold().setMarginTop(10));
        document.add(new Paragraph("""
            1. Tôi tự nguyện đề nghị xét nghiệm ADN và chấp nhận chi phí xét nghiệm.
            2. Tôi xác nhận thông tin tôi cung cấp là trung thực.
            3. Tôi đã được giải thích rõ mục đích, ý nghĩa, phạm vi và tính chất pháp lý của kết quả xét nghiệm ADN.
            4. Tôi đồng ý ký vào phiếu này và chịu trách nhiệm về mọi thông tin kê khai.
            """));

        document.add(new Paragraph("[ ] Tôi đã đọc và đồng ý")
                .setFontColor(ColorConstants.BLUE)
                .setMarginTop(5));

        // === HÌNH THỨC NHẬN KẾT QUẢ ===
        document.add(new Paragraph("Hình thức nhận kết quả:")
                .setBold()
                .setMarginTop(10));
        String deliveryMethod = order.getResultDeliveryMethod();
        document.add(new Paragraph(deliveryMethod));

        // === NGÀY THÁNG VÀ CHỮ KÝ ===
        LocalDate today = LocalDate.now();
        document.add(new Paragraph(String.format("TP. HCM, ngày %02d tháng %02d năm %d", today.getDayOfMonth(), today.getMonthValue(), today.getYear()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10)
                .setMarginTop(10));

        Table signTable = new Table(new float[]{3, 3, 3}).useAllAvailableWidth().setMarginTop(10);
        signTable.addCell(new Cell().add(new Paragraph("NGƯỜI TIẾP NHẬN MẪU\n(ký và ghi rõ họ tên)").setFontSize(9).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));

        Cell fingerprintCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        fingerprintCell.add(new Paragraph("NGÓN TRỎ PHẢI\n(Đối với hành chính)").setFontSize(9));

        // Lấy fingerprint từ nhân viên tiếp nhận mẫu (registrationStaff)
        Staff staff = order.getRegistrationStaff();  // lấy từ TestOrder

        if (staff != null && staff.getFingerprint() != null) {
            try {
                byte[] fingerprintBytes = Base64.getDecoder().decode(staff.getFingerprint());
                ImageData fingerprintData = ImageDataFactory.create(fingerprintBytes);
                Image fingerprintImage = new Image(fingerprintData);
                fingerprintImage.setAutoScale(true).setMaxHeight(60).setMaxWidth(60);
                fingerprintCell.add(fingerprintImage);
            } catch (Exception e) {
                fingerprintCell.add(new Paragraph("(Vân tay lỗi)").setFontColor(ColorConstants.RED));
            }
        } else {
            fingerprintCell.add(new Paragraph("(Chưa có)").setFontColor(ColorConstants.GRAY));
        }

        signTable.addCell(fingerprintCell);

        signTable.addCell(new Cell()
                .add(new Paragraph("NGƯỜI YÊU CẦU PHÂN TÍCH\n(ký và ghi rõ họ tên)").setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));

        document.add(signTable);
        document.add(new Paragraph("Trang 01/03")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(8)
                .setMarginTop(10));

        // === TRANG 2 ===
        document.getPdfDocument().addNewPage();
        document.setMargins(20, 20, 20, 20);

        document.add(new Paragraph("ĐIỀU KHOẢN")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10));

        document.add(new Paragraph(getRulesText()).setFontSize(9).setMarginTop(10));

        document.add(new Paragraph("NGƯỜI YÊU CẦU PHÂN TÍCH ĐÃ ĐỌC KỸ VÀ ĐỒNG Ý VỚI CÁC ĐIỀU KHOẢN TRÊN")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));

        document.add(new Paragraph("(ký và ghi rõ họ tên)")
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(" ").setHeight(20));
        document.add(new Paragraph("Trang 02/03")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(8)
                .setMarginTop(10));

        // === Trang 3: Biên bản lấy mẫu ===
        document.add(new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM")
                .setTextAlignment(TextAlignment.CENTER).setBold());
        document.add(new Paragraph("Độc lập - Tự do - Hạnh phúc")
                .setTextAlignment(TextAlignment.CENTER).setItalic().setMarginBottom(10));
        document.add(new Paragraph("BIÊN BẢN LẤY MẪU XÉT NGHIỆM")
                .setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER).setMarginBottom(15));

        String diaChi = customer.getAddress();

        Account staffAcc = order.getRegistrationStaff() != null ? order.getRegistrationStaff().getAccount() : null;
        String staffName = staffAcc != null ? staffAcc.getFullName() : "...........";
        String requesterName = acc.getFullName();

        document.add(new Paragraph(String.format("Hôm nay, ngày %02d tháng %02d năm %d, tại TP.Hồ Chí Minh",
                today.getDayOfMonth(), today.getMonthValue(), today.getYear())));

        document.add(new Paragraph("Chúng tôi gồm có:"));
        document.add(new Paragraph("1. Nhân viên thu mẫu: " + staffName));
        document.add(new Paragraph("2. Người yêu cầu xét nghiệm: " + requesterName + "    Địa chỉ: " + diaChi));
        document.add(new Paragraph("Tiến hành lấy mẫu các đối tượng:"));


        int quantity = order.getSampleQuantity();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < quantity; i++) {
            TestSample sample = (i < samples.size()) ? samples.get(i) : new TestSample();
            document.getPdfDocument().addNewPage();

            Table mainTable = new Table(UnitValue.createPercentArray(new float[]{75, 25}))
                    .useAllAvailableWidth()
                    .setMarginTop(10);

            // === CỘT TRÁI: THÔNG TIN ===
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{25, 25, 25, 25}))
                    .useAllAvailableWidth()
                    .setFontSize(10)
                    .setMarginTop(10);

            infoTable.addCell(new Cell().add(new Paragraph("Họ và tên:").setBold()).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph(sample.getName() != null ? sample.getName() : "")).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph("Ngày sinh:").setBold()).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph(sample.getDateOfBirth() != null ? dateFormat.format(sample.getDateOfBirth()) : "")).setPadding(5).setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell().add(new Paragraph("Loại giấy tờ:").setBold()).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph(sample.getDocumentType() != null ? sample.getDocumentType() : "")).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph("Số giấy tờ:").setBold()).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph(sample.getDocumentNumber() != null ? sample.getDocumentNumber() : "")).setPadding(5).setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell(1, 4)
                    .add(new Paragraph()
                            .add("Ngày cấp: ").add(new Text(sample.getDateOfIssue() != null ? dateFormat.format(sample.getDateOfIssue()) : "").setBold())
                            .add("    Ngày hết hạn: ").add(new Text(sample.getExpirationDate() != null ? dateFormat.format(sample.getExpirationDate()) : "").setBold())
                            .add("    Nơi cấp: ").add(new Text(sample.getPlaceOfIssue() != null ? sample.getPlaceOfIssue() : "").setBold()))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell(1, 4)
                    .add(new Paragraph("Địa chỉ: ").add(new Text(sample.getAddress() != null ? sample.getAddress() : "").setBold()))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell().add(new Paragraph("Loại mẫu:").setBold()).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph(sample.getSampleType() != null ? sample.getSampleType().getSampleType() : "")).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph("Số lượng mẫu:").setBold()).setPadding(5).setBorder(new SolidBorder(0.5f)));
            infoTable.addCell(new Cell().add(new Paragraph(sample.getNumberOfSample() != null ? String.valueOf(sample.getNumberOfSample()) : "")).setPadding(5).setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell(1, 4)
                    .add(new Paragraph("Mối quan hệ: ").add(new Text(sample.getRelationship() != null ? sample.getRelationship() : "").setBold()))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell(1, 4)
                    .add(new Paragraph("Người cho mẫu hoặc giám hộ ký tên:"))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f)));

            infoTable.addCell(new Cell(1, 4)
                    .add(new Paragraph()
                            .add("Tiểu sử bệnh về máu, truyền máu hoặc ghép tạng trong 6 tháng: ")
                            .add(new Text(sample.getMedicalHistory() != null ? sample.getMedicalHistory() : "Không").setBold()))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f)));

            mainTable.addCell(new Cell()
                    .add(infoTable)
                    .setBorder(Border.NO_BORDER)
                    .setMinHeight(250f));

// === CỘT PHẢI: VÂN TAY ===
            Table thumbTable = new Table(1).useAllAvailableWidth();
            thumbTable.setFontSize(9);

// Dòng 1: tiêu đề người cho mẫu
            thumbTable.addCell(new Cell()
                    .add(new Paragraph("Người cho mẫu thứ " + (i + 1))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f))); // ✅ Bo viền

// Dòng 2: Ảnh vân tay (ô xám)
            Cell thumbCell = new Cell()
                    .setHeight(130f)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE); // ✅ Chuyển căn chỉnh dọc sang Cell
            String fingerprint = sample.getFingerprint();
            if (fingerprint != null && !fingerprint.isBlank()) {
                try {
                    if (fingerprint.startsWith("data:image")) {
                        fingerprint = fingerprint.split(",")[1];
                    }
                    byte[] fpBytes = Base64.getDecoder().decode(fingerprint);
                    ImageData fpData = ImageDataFactory.create(fpBytes);
                    Image fpImage = new Image(fpData)
                            .setWidth(UnitValue.createPercentValue(100)) // Kéo giãn chiều rộng
                            .setHeight(130f) // Khớp chiều cao ô
                            .setHorizontalAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
                    thumbCell.add(fpImage);
                } catch (Exception e) {
                    thumbCell.add(new Paragraph("(Vân tay lỗi)").setFontColor(ColorConstants.RED).setTextAlignment(TextAlignment.CENTER));
                }
            } else {
                thumbCell.add(new Paragraph("(Chưa có vân tay)").setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER));
            }
            thumbTable.addCell(thumbCell);

// Dòng 3: ghi chú
            thumbTable.addCell(new Cell()
                    .add(new Paragraph("Vân tay ngón trỏ phải").setTextAlignment(TextAlignment.CENTER))
                    .setPadding(5)
                    .setBorder(new SolidBorder(0.5f))); // ✅ Bo viền

            mainTable.addCell(new Cell()
                    .add(thumbTable)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMinHeight(250f));

            document.add(mainTable);
        }



        document.add(new Paragraph(" "));

        Table finalSignTable = new Table(new float[]{3, 3, 3})
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(10);

        Cell thuMauCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
                .setMinHeight(100); // Tăng chiều cao tối thiểu của ô

        thuMauCell.add(new Paragraph("NGƯỜI THU MẪU\n(ký, ghi rõ họ tên và lăn tay)")
                .setFontSize(9)
                .setMarginBottom(5)); // Thêm khoảng cách giữa dòng chữ và ảnh

        String thuMauFingerprintBase64 = order.getRegistrationStaff() != null
                ? order.getRegistrationStaff().getFingerprint()
                : null;

        if (thuMauFingerprintBase64 != null && !thuMauFingerprintBase64.isBlank()) {
            try {
                if (thuMauFingerprintBase64.startsWith("data:image")) {
                    thuMauFingerprintBase64 = thuMauFingerprintBase64.split(",")[1];
                }
                byte[] fpBytes = Base64.getDecoder().decode(thuMauFingerprintBase64);
                ImageData fpData = ImageDataFactory.create(fpBytes);
                Image fpImage = new Image(fpData)
                        .setAutoScale(true)
                        .setMaxHeight(80)  // Tăng kích thước ảnh
                        .setMaxWidth(80)
                        .setMarginTop(5);
                thuMauCell.add(fpImage);
            } catch (Exception e) {
                thuMauCell.add(new Paragraph("(Vân tay lỗi)").setFontColor(ColorConstants.RED));
            }
        } else {
            thuMauCell.add(new Paragraph("(Chưa có vân tay)").setFontColor(ColorConstants.GRAY));
        }

        finalSignTable.addCell(thuMauCell);

        Cell duocLayMauCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        duocLayMauCell.add(new Paragraph("NGƯỜI ĐƯỢC LẤY MẪU\n(ký và ghi rõ họ tên)").setFontSize(9));
        finalSignTable.addCell(duocLayMauCell);

        Cell yeuCauCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        yeuCauCell.add(new Paragraph("NGƯỜI YÊU CẦU XÉT NGHIỆM\n(ký và ghi rõ họ tên)").setFontSize(9));
        finalSignTable.addCell(yeuCauCell);

        document.add(finalSignTable);

        document.add(new Paragraph("\n\n\n\n"));
        document.add(new Paragraph("Nhân viên thu mẫu cam kết: Tôi xác nhận việc tư vấn, thu mẫu\nvà thông tin, chỉ lấy của người thân gia xét nghiệm là chính xác.\nTôi cam đoan không có sự gian lận nào đối với mẫu xét nghiệm.")
                .setFontSize(9).setItalic());

        document.add(new Paragraph("Trang 03/03")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(8));
    }

    // Có thể tách nội dung điều khoản ra để dễ đọc:
    private String getRulesText() {
        return """
            1. Viện Công nghệ ADN và Phân tích Di truyền được Bộ Khoa học và Công nghệ cấp phép chính thức đăng ký hoạt động theo giấy chứng nhận số A-1889.
            2. Người yêu cầu xét nghiệm phải là người trực tiếp cung cấp mẫu cho quá trình xét nghiệm, là bố, mẹ hay đứa trẻ; hoặc là người được sự ủy quyền hợp pháp từ bố, mẹ của đứa trẻ. Viện không chịu trách nhiệm về yêu cầu xét nghiệm mà nhân viên, sau đây gọi chung là “khách hàng” cung cấp.
            3. “Mẫu”, mẫu ADN hay “mẫu xét nghiệm” bao gồm: máu lấy từ tĩnh mạch, móng chân, tóc,… được coi là các mẫu chuẩn nếu huyết thống được xác minh là mẫu máu. Tuy nhiên, mẫu móng chân, tóc,… được coi là mẫu phụ. Nếu mẫu phụ không đủ điều kiện thì phải thực hiện lại mẫu máu chuẩn. Viện không chịu trách nhiệm phân tích nếu người cung cấp mẫu tự ý chọn mẫu phụ không đúng quy trình thu mẫu của Viện.
            4. Tất cả các mẫu sinh học nộp xét nghiệm ADN phải được đóng riêng biệt vào các bao phong bị bảo vệ đầy đủ và được ghi đầy đủ thông tin, có chữ ký xác nhận của người lấy mẫu. Viện sẽ sử dụng các thông tin này vào việc đối chiếu và không chịu trách nhiệm nếu có điều chỉnh, sai lệch của các thông tin mà “khách hàng” cung cấp. Các mẫu gửi đến Viện để xét nghiệm ADN không có đầy đủ thông tin thì nghiễm nhiên bị từ chối không được tiếp nhận và/hoặc sẽ xét nghiệm ADN khi Viện có quyền lý do và/hoặc bằng lòng.
            5. “Khách hàng” phải đảm bảo các mẫu cần xét nghiệm ADN cung cấp cho Viện là hợp pháp (không có sự cưỡng chế hay sai phạm khi ghi và ký vào mẫu máu). “Khách hàng” phải chấp hành biện pháp thông báo ký tên tại thời điểm lấy mẫu. Nếu phải chịu ký mẫu thì các mẫu đó được không hợp pháp. Trừ trường hợp lấy mẫu bắt buộc khi có sự chỉ định của tòa án. (Viện/ngoại viện phải phải thu thập và thực hiện trình tự và thủ tục đúng luật pháp, luật định, mới được sự chấp thuận để thực hiện.)
            6. Viện sẽ chỉ tiến hành xét nghiệm ADN khi “Đơn yêu cầu xét nghiệm ADN” đã đầy đủ các thông tin cùng chữ ký của “khách hàng” yêu cầu xét nghiệm và nhân chứng thanh toán đủ 50% chi phí tương ứng với yêu cầu xét nghiệm. Viện có quyền giữ lại các kết quả phân tích cho đến khi khách hàng thanh toán đủ tiền.
            7. Khách hàng phải chịu mọi chi phí phát sinh trong quá trình chuyển phát xét nghiệm và mẫu tới Viện.
            8. Viện không chấp nhận mẫu gửi đến để xét nghiệm ADN không đủ, chất lượng máu kém, mẫu bị nhiễm hoặc cần thêm máu người thân để xét nghiệm thì Viện có quyền yêu cầu thu thêm mẫu hoặc lấy lại mẫu.
            9. Khách hàng cam kết việc tiến hành trong tổ hành lang là đúng sự thật và không được thay đổi. Những trường hợp sai sót do trách nhiệm của bên thứ ba thì không bảo lưu quyền khiếu nại. Viện không chịu trách nhiệm đối mặt bất cứ sai lệch gì kể cả kết quả xét nghiệm theo đúng yêu cầu chỉ vì, nhưng không chấp nhận bất kỳ sự từ chối nào khác do nguyên nhân bị gây ra bởi một bên thứ ba.
            10. Sau khi xét nghiệm không đủ điều kiện, mẫu bị trộn, pha lẫn, nhiễm hoặc không đủ yêu cầu hoàn trả lại mẫu cho khách hàng. Viện có quyền tiêu hủy theo quy trình mẫu của Viện.
            11. Kết quả xét nghiệm có thể được cung cấp bằng bản cứng hoặc được đính kèm theo chữ ký số qua email trong trường hợp khách hàng có yêu cầu rõ ràng. Sẽ là sai nếu mà khách hàng đòi sửa kết quả xét nghiệm, để Viện chịu trách nhiệm.
            12. Khách hàng phải chịu trách nhiệm nếu như các thông tin cá nhân cung cấp sai lệch và chữ ký mẫu không chính xác. Viện không chịu trách nhiệm và được miễn trừ trách nhiệm trong các sai sót do thông tin khách hàng cung cấp.
            13. Viện có quyền từ chối thực hiện nếu phát hiện sai sót trong thông tin, hành vi không đúng quy định, vi phạm pháp luật hoặc sai lệch mẫu gửi đến.
            14. Trong trường hợp Viện không thực hiện được xét nghiệm do lý do khách quan thì Viện có trách nhiệm thông báo trước cho khách hàng trong vòng 3 ngày kể từ ngày nhận mẫu và hoàn lại toàn bộ chi phí.
            15. Khách hàng có quyền khiếu nại trong vòng 3 ngày làm việc kể từ khi nhận kết quả xét nghiệm nếu có sai sót.
            16. Viện không hoàn trả chi phí nếu mẫu xét nghiệm không hợp lệ hoặc khách hàng không yêu cầu kiểm tra mẫu hoặc vi phạm các điều khoản quy định.
            17. Viện có quyền lưu trữ mẫu xét nghiệm tối đa 30 ngày kể từ ngày xét nghiệm xong và hủy theo quy định.
            18. Khi khách hàng đồng ý ký đơn xét nghiệm ADN nghĩa là khách hàng đã hiểu rõ và đồng ý với các điều khoản nêu trên.
            """;
    }

    private String getFingerprintBase64(Long orderId) {
        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(orderId);
        if (samples != null && !samples.isEmpty()) {
            TestSample firstSample = samples.get(0);
            String fingerprint = firstSample.getFingerprint();
            if (fingerprint != null && !fingerprint.isBlank()) {
                // Loại bỏ tiền tố nếu có
                if (fingerprint.startsWith("data:image")) {
                    fingerprint = fingerprint.split(",")[1];
                }
                return fingerprint;
            }
        }
        return null;
    }

}