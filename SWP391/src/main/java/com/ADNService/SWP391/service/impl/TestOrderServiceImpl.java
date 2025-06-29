package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.dto.CustomerDTO;
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
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
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

//                PdfFont font = PdfFontFactory.createFont("fonts/arial.ttf", PdfEncodings.IDENTITY_H, true);
//                document.setFont(font);

                document.setMargins(20, 20, 20, 20);

                String serviceType = order.getServices().getServiceType();

                if (serviceType.equalsIgnoreCase("Dân Sự")) {
                    generateFormDanSu(document, order);
                } else if (serviceType.equalsIgnoreCase("Hành Chính")) {
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

        // Tiêu đề
        document.add(new Paragraph("ĐƠN YÊU CẦU PHÂN TÍCH ADN")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16).setBold());
        document.add(new Paragraph("Kính gửi: Viện Công Nghệ ADN và Phân Tích Di Truyền").setItalic());

        document.add(new Paragraph(" "));

        // Phần form thông tin khách hàng
        document.add(new Paragraph()
                .add("Tôi tên là (viết hoa): ")
                .add(new Text(acc.getFullName()).setBold().setUnderline())
                .add("      Giới tính: [X] Nam [ ] Nữ [ ] Khác"));

        document.add(new Paragraph()
                .add("Địa chỉ: ")
                .add(new Text(customer.getAddress()).setUnderline()));

        document.add(new Paragraph()
                .add("CMND/CCCD/Passport: ")
                .add(new Text(customer.getCccd()).setUnderline())
                .add("    ngày cấp: ")
                .add(new Text(customer.getDateOfIssue() != null ? customer.getDateOfIssue().toString() : "").setUnderline())
                .add("    nơi cấp: ")
                .add(new Text(customer.getPlaceOfIssue()).setUnderline()));

        document.add(new Paragraph()
                .add("Số điện thoại: ")
                .add(new Text(acc.getPhone()).setUnderline())
                .add("     Email/zalo: ")
                .add(new Text(acc.getEmail()).setUnderline()));

        document.add(new Paragraph(
                "Đề nghị Viện phân tích ADN và xác định mối quan hệ huyết thống cho những người cung cấp mẫu dưới đây:"));

        // bảng danh sách mẫu
        Table table = new Table(new float[]{1, 3, 2, 2, 2, 2, 2, 3}).useAllAvailableWidth();
        table.addHeaderCell("STT");
        table.addHeaderCell("Họ tên");
        table.addHeaderCell("Năm sinh");
        table.addHeaderCell("Giới tính");
        table.addHeaderCell("Mối quan hệ");
        table.addHeaderCell("Loại mẫu");
        table.addHeaderCell("Ngày lấy mẫu");
        table.addHeaderCell("Ghi chú");

        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(order.getOrderId());
        int stt = 1;
        for (TestSample s : samples) {
            table.addCell(String.valueOf(stt++));
            table.addCell(s.getName());
            table.addCell(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "");
            table.addCell(s.getGender());
            table.addCell(s.getRelationship());
            table.addCell(s.getSampleType());
            table.addCell(s.getDateOfIssue() != null ? s.getDateOfIssue().toString() : "");
            table.addCell(""); // ghi chú nếu cần
        }
        document.add(table);

        // phần cam kết (tóm lược)
        document.add(new Paragraph("\nTôi xin cam kết:").setBold());
        document.add(new Paragraph("1. Tôi tự nguyện đề nghị xét nghiệm ADN... (ghi đầy đủ cam kết như mẫu scan)"));
        // bạn có thể bổ sung tiếp các điều khoản cam kết giống hình nếu muốn

        // phần chữ ký
        LocalDate today = LocalDate.now();
        String dateStr = today.getDayOfMonth() + "/" + today.getMonthValue() + "/" + today.getYear();
        document.add(new Paragraph("\nTP. HCM, ngày " + dateStr)
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("Người yêu cầu").setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph(acc.getFullName()).setTextAlignment(TextAlignment.RIGHT).setItalic());
    }

    private void generateFormHanhChinh(Document document, TestOrder order) {
        Customer customer = order.getCustomer();
        Account acc = customer.getAccount();

        // Tiêu đề
        document.add(new Paragraph("CỘNG HOÀ XÃ HỘI CHỦ NGHĨA VIỆT NAM")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold());
        document.add(new Paragraph("Độc lập - Tự do - Hạnh phúc").setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\nBIÊN BẢN LẤY MẪU XÉT NGHIỆM")
                .setBold().setTextAlignment(TextAlignment.CENTER));

        // ngày, địa điểm
        LocalDate today = LocalDate.now();
        document.add(new Paragraph("Hôm nay, ngày " + today.getDayOfMonth() + " tháng " + today.getMonthValue() + " năm " + today.getYear()
                + ", tại địa chỉ: " + customer.getAddress()));

        // Người thu mẫu
        document.add(new Paragraph("Người thu mẫu: TRẦN TRUNG TÂM")); // ví dụ tên người thu mẫu cố định
        document.add(new Paragraph("Người yêu cầu xét nghiệm: " + acc.getFullName()));

        document.add(new Paragraph("\nChúng tôi tiến hành lấy mẫu của những người liên quan như sau:"));

        // Bảng danh sách mẫu chi tiết
        Table table = new Table(new float[]{1, 3, 3, 3, 3, 3, 3}).useAllAvailableWidth();
        table.addHeaderCell("STT");
        table.addHeaderCell("Họ tên");
        table.addHeaderCell("Loại giấy tờ");
        table.addHeaderCell("Ngày hết hạn");
        table.addHeaderCell("Địa chỉ");
        table.addHeaderCell("Loại mẫu");
        table.addHeaderCell("Mối quan hệ");

        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(order.getOrderId());
        int stt = 1;
        for (TestSample s : samples) {
            table.addCell(String.valueOf(stt++));
            table.addCell(s.getName());
            table.addCell(s.getDocumentType() + " - " + s.getDocumentNumber());
            table.addCell(s.getExpirationDate() != null ? s.getExpirationDate().toString() : "");
            table.addCell(s.getAddress());
            table.addCell(s.getSampleType());
            table.addCell(s.getRelationship());
        }
        document.add(table);

        // checkbox bệnh truyền nhiễm
        document.add(new Paragraph("\nCó tiền sử bệnh máu, truyền máu hoặc ghép tạng trong 6 tháng: [ ] Có   [X] Không"));

        // phần chữ ký
        document.add(new Paragraph("\nNgười thu mẫu: __________________"));
        document.add(new Paragraph("Người yêu cầu xét nghiệm: __________________"));

        // có thể thêm dấu vân tay (scan) nếu có file ảnh PNG rồi vẽ Image iText
    }

//        TestOrder order = testOrderRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn xét nghiệm"));
//
//        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            PdfWriter writer = new PdfWriter(out);
//            PdfDocument pdfDoc = new PdfDocument(writer);
//            Document document = new Document(pdfDoc, PageSize.A4);
//
//
//            document.setMargins(20, 20, 20, 20);
//
//            // === Tiêu đề ===
//            Paragraph title = new Paragraph("ĐƠN YÊU CẦU PHÂN TÍCH ADN")
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setFontSize(16).setBold();
//            document.add(title);
//
//            document.add(new Paragraph("Kính gửi: Viện Công Nghệ ADN và Phân Tích Di Truyền").setItalic().setFontSize(11));
//
//            document.add(new Paragraph(" "));
//
//            Customer customer = order.getCustomer();
//            Account acc = customer.getAccount();
//
//            // === Phần form với dòng chấm ===
//            String fullName = acc.getFullName();
//            String gender = customer.getGender();
//            String phone = acc.getPhone();
//            String email = acc.getEmail();
//            String address = customer.getAddress();
//            String cccd = customer.getCccd();
//            String placeOfIssue = customer.getPlaceOfIssue();
//            String dateOfIssue = customer.getDateOfIssue() != null ? customer.getDateOfIssue().toString() : "";
//
//            document.add(new Paragraph()
//                    .add("Tôi tên là (viết hoa): ")
//                    .add(new Text(fullName).setBold().setUnderline())
//                    .add("            Giới tính: ")
//                    .add("[X] Nam [ ] Nữ [ ] Không xác định")
//            );
//
//            document.add(new Paragraph()
//                    .add("Địa chỉ: ")
//                    .add(new Text(address).setUnderline())
//            );
//
//            document.add(new Paragraph()
//                    .add("CMND/CCCD/Passport: ")
//                    .add(new Text(cccd).setUnderline())
//                    .add("     ngày cấp: ")
//                    .add(new Text(dateOfIssue).setUnderline())
//                    .add("     nơi cấp: ")
//                    .add(new Text(placeOfIssue).setUnderline())
//            );
//
//            document.add(new Paragraph()
//                    .add("Số điện thoại: ")
//                    .add(new Text(phone).setUnderline())
//                    .add("     Email/zalo: ")
//                    .add(new Text(email).setUnderline())
//            );
//
//            document.add(new Paragraph(
//                    "Đề nghị Viện phân tích ADN và xác định mối quan hệ huyết thống cho những người cung cấp mẫu dưới đây:"
//            ));
//
//            // === Bảng danh sách mẫu xét nghiệm ===
//            document.add(new Paragraph("Bệnh viện phân tích ADN và xác định quan hệ huyết thống cho những người sau:"));
//
//            Table sampleTable = new Table(new float[]{1, 3, 2, 2, 2, 2, 2, 3}).useAllAvailableWidth();
//            sampleTable.addHeaderCell("STT");
//            sampleTable.addHeaderCell("Họ tên");
//            sampleTable.addHeaderCell("Năm sinh");
//            sampleTable.addHeaderCell("Giới tính");
//            sampleTable.addHeaderCell("Mối quan hệ");
//            sampleTable.addHeaderCell("Loại mẫu");
//            sampleTable.addHeaderCell("Ngày lấy mẫu");
//            sampleTable.addHeaderCell("Ghi chú");
//
//            List<TestSample> samples = testSampleRepository.findByOrder_OrderId(order.getOrderId());
//            int stt = 1;
//            for (TestSample s : samples) {
//                sampleTable.addCell(String.valueOf(stt++));
//                sampleTable.addCell(s.getName());
//                sampleTable.addCell(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : "");
//                sampleTable.addCell(s.getGender());
//                sampleTable.addCell(s.getRelationship());
//                sampleTable.addCell(s.getSampleType());
//                sampleTable.addCell(
//                        s.getDateOfIssue() != null ? s.getDateOfIssue().toString() : ""
//                );
//                sampleTable.addCell(""); // ghi chú nếu cần
//            }
//            document.add(sampleTable);
//
//            document.add(new Paragraph(" "));
//
//            // === checkbox cam kết ===
//            document.add(new Paragraph("Cam kết:").setBold());
//            document.add(new Paragraph("""
//1. Tôi tự nguyện đề nghị xét nghiệm ADN và chấp nhận chi phí xét nghiệm.
//2. Tôi xác nhận thông tin tôi cung cấp là trung thực.
//3. Tôi đã được giải thích rõ mục đích, ý nghĩa, phạm vi và tính chất pháp lý của kết quả xét nghiệm ADN.
//4. Tôi đồng ý ký vào phiếu này và chịu trách nhiệm về mọi thông tin kê khai.
//        """));
//
//            document.add(new Paragraph("[X] Tôi đã đọc và đồng ý").setFontColor(ColorConstants.BLUE));
//
//            // === Hình thức nhận kết quả ===
//            document.add(new Paragraph("Hình thức nhận kết quả:").setBold());
//            document.add(new Paragraph("[ ] Nhận tại văn phòng   [X] Nhận qua Email/Zalo"));
//
//            // === Chữ ký
//            LocalDate today = LocalDate.now();
//            document.add(new Paragraph("TP. HCM, ngày " + today.getDayOfMonth() + " tháng " + today.getMonthValue() + " năm " + today.getYear())
//                    .setTextAlignment(TextAlignment.RIGHT));
//            document.add(new Paragraph("Người yêu cầu phân tích").setTextAlignment(TextAlignment.RIGHT).setBold());
//            document.add(new Paragraph(acc.getFullName()).setTextAlignment(TextAlignment.RIGHT).setItalic());
//
//            // === TRANG 2 điều khoản ===
//            pdfDoc.addNewPage();
//            document.add(new Paragraph("ĐIỀU KHOẢN").setBold().setTextAlignment(TextAlignment.CENTER));
//
//            document.add(new Paragraph("""
//1. Viện Công nghệ ADN và Phân tích di truyền được Bộ Khoa học và Công nghệ cấp phép hoạt động.
//2. Người yêu cầu xét nghiệm phải khai báo trung thực mọi thông tin liên quan.
//3. Mẫu này chỉ có giá trị tham khảo và không có giá trị pháp lý.
//4. Viện có quyền từ chối nếu phát hiện giả mạo giấy tờ hoặc vi phạm pháp luật.
//...
//(ghi đầy đủ 18 điều khoản theo file gốc của bạn)
//        """).setFontSize(9));
//
//            document.close();
//            return out.toByteArray();
//
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi tạo PDF: " + e.getMessage());
//        }
//    }
//
//

}
