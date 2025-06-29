package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.*;
import com.ADNService.SWP391.service.*;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestResultPDFServiceImpl implements TestResultPDFService {

    @Autowired
    private TestOrderService testOrderService;

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private TestSampleService testSampleService;

    @Autowired
    private TestResultSampleService testResultSampleService;


    @Override
    public TestResultPDFDTO getFullTestResultReport(Long orderId) {
        TestOrderDTO order = testOrderService.getTestOrderById(orderId);
        List<TestResultDTO> result = testResultService.getTestResultByOrderId(orderId);
        List<TestSampleDTO> samples = testSampleService.getTestSamplesByOrderId(orderId);
        List<TestResultSampleDTO> resultSamples = testResultSampleService.getTestResultSamplesByOrderId(orderId);

        return new TestResultPDFDTO(order, result, samples, resultSamples);
    }

    @Override
    public TestResultPDFDTO getTestResultReportBySamples(Long sampleId1, Long sampleId2) {
        List<TestResultDTO> results = testResultService.getTestResultBySampleIds(sampleId1, sampleId2);

        if (results.isEmpty()) {
            throw new RuntimeException("Không tìm thấy kết quả giữa hai mẫu này");
        }

        Long orderId = results.get(0).getOrderId();
        TestOrderDTO order = testOrderService.getTestOrderById(orderId);

        List<TestSampleDTO> allSamples = testSampleService.getTestSamplesByOrderId(orderId);
        List<TestSampleDTO> selectedSamples = allSamples.stream()
                .filter(s -> s.getId().equals(sampleId1) || s.getId().equals(sampleId2))
                .collect(Collectors.toList());

        List<TestResultSampleDTO> resultSamples = testResultSampleService.getTestResultSamplesByOrderId(orderId)
                .stream()
                .filter(rs -> rs.getTestSampleId().equals(sampleId1) || rs.getTestSampleId().equals(sampleId2))
                .collect(Collectors.toList());

        return new TestResultPDFDTO(order, results, selectedSamples, resultSamples);
    }

    @Override
    public void generatePDF(TestResultPDFDTO report, OutputStream out) throws Exception {

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.setFontSize(10);
        PdfFont font = PdfFontFactory.createFont(
                "src/main/resources/fonts/DejaVuSans.ttf",
                "Identity-H",
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
        );
        document.setFont(font);


        // === TIÊU ĐỀ QUỐC GIA ===
        document.add(new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12));
        document.add(new Paragraph("Độc lập - Tự do - Hạnh phúc")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(11));
        document.add(new Paragraph("\n")); // Dòng trắng


        // === PHẦN MỞ ĐẦU ===
        document.add(new Paragraph("GENTIS - NIỀM TIN TRỌN VẸN").setBold().setFontSize(12));
        document.add(new Paragraph("PHIẾU KẾT QUẢ PHÂN TÍCH ADN")
                .setBold().setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER));

        TestOrderDTO order = report.getTestOrder();
        String fullName = order.getAccount().getFullName(); // Lấy từ Account
        String address = order.getCustomer().getAddress();  // Lấy từ Customer

        document.add(new Paragraph("Căn cứ vào giấy đề nghị phân tích ADN số: HID" + order.getOrderId()));
        document.add(new Paragraph("Của Ông/Bà: " + fullName));
        document.add(new Paragraph("Địa chỉ: " + address));


        // === THÔNG TIN MẪU ===
        document.add(new Paragraph("\nCông ty Gentis tiến hành phân tích các mẫu ADN sau:"));

        float[] infoWidths = {30F, 150F, 50F, 100F, 80F};
        Table infoTable = new Table(infoWidths);
        infoTable.addHeaderCell("TT");
        infoTable.addHeaderCell("Tên mẫu");
        infoTable.addHeaderCell("Tuổi");
        infoTable.addHeaderCell("Quan hệ");
        infoTable.addHeaderCell("Ký hiệu mẫu");

        int stt = 1;
        for (TestSampleDTO sample : report.getTestSamples()) {
            infoTable.addCell(String.valueOf(stt++));
            infoTable.addCell(sample.getName());
            infoTable.addCell(sample.getDateOfBirth() != null ?
                    String.valueOf(calculateAge(sample.getDateOfBirth())) : "");

            infoTable.addCell(sample.getRelationship());
            infoTable.addCell(sample.getKitCode());
        }
        document.add(infoTable);

        document.add(new Paragraph("\nSau khi phân tích các mẫu ADN... chúng tôi có kết quả sau:"));

        // === BẢNG LOCUS ===
        List<Long> sampleIds = report.getTestSamples().stream()
                .map(TestSampleDTO::getId).collect(Collectors.toList());

        Map<Long, String> sampleIdToName = report.getTestSamples().stream()
                .collect(Collectors.toMap(TestSampleDTO::getId, TestSampleDTO::getName));

        float[] columnWidths = new float[1 + 2 * sampleIds.size()];
        columnWidths[0] = 100F;
        for (int i = 1; i < columnWidths.length; i++) columnWidths[i] = 70F;

        Table locusTable = new Table(columnWidths).setFontSize(9);
        locusTable.addHeaderCell("Locus");

        for (Long id : sampleIds) {
            String name = sampleIdToName.getOrDefault(id, "Unknown");
            locusTable.addHeaderCell("A1 (" + name + ")");
            locusTable.addHeaderCell("A2 (" + name + ")");
        }

        Map<String, List<TestResultSampleDTO>> grouped = report.getTestResultSamples().stream()
                .collect(Collectors.groupingBy(TestResultSampleDTO::getLocusName));

        for (Map.Entry<String, List<TestResultSampleDTO>> entry : grouped.entrySet()) {
            String locus = entry.getKey();
            List<TestResultSampleDTO> results = entry.getValue();

            locusTable.addCell(locus);
            //----------------------------------
            Map<Long, TestResultSampleDTO> alleleMap = results.stream()
                    .collect(Collectors.toMap(
                            TestResultSampleDTO::getTestSampleId,
                            r -> r,
                            (r1, r2) -> r1 // hoặc r2 nếu muốn lấy record mới
                    ));//-------------------------------

            for (Long id : sampleIds) {
                TestResultSampleDTO r = alleleMap.get(id);
                locusTable.addCell(r != null ? r.getAllele1() : "");
                locusTable.addCell(r != null ? r.getAllele2() : "");
            }
        }

        document.add(locusTable);

        // === KẾT LUẬN ===
        document.add(new Paragraph("\nHội đồng khoa học công ty Gentis kết luận:").setBold());

        for (TestResultDTO rs : report.getTestResult()) {
            String name1 = sampleIdToName.getOrDefault(rs.getSampleId1(), "Mẫu " + rs.getSampleId1());
            String name2 = sampleIdToName.getOrDefault(rs.getSampleId2(), "Mẫu " + rs.getSampleId2());

            // Lấy relationship từ danh sách sample
            String rel1 = report.getTestSamples().stream()
                    .filter(s -> s.getId().equals(rs.getSampleId1()))
                    .map(TestSampleDTO::getRelationship)
                    .findFirst()
                    .orElse("");

            String rel2 = report.getTestSamples().stream()
                    .filter(s -> s.getId().equals(rs.getSampleId2()))
                    .map(TestSampleDTO::getRelationship)
                    .findFirst()
                    .orElse("");

            document.add(new Paragraph("- Người có mẫu " + name1 + " và người có mẫu " + name2 +": "+
                    rs.getResult()+ " " + rel1 + " - " + rel2 ));
        }

        // === GHI CHÚ + CHỮ KÝ ===

        String serviceType = report.getTestOrder().getServiceType();

        String note = "";
        if ("DÂN SỰ".equalsIgnoreCase(serviceType)) {
            note = "Ghi chú: Đây là xét nghiệm dân sự.";
        } else if ("HÀNH CHÍNH".equalsIgnoreCase(serviceType)) {
            note = "Ghi chú: Đây là xét nghiệm hành chính.";
        }

        document.add(new Paragraph("\n" + note).setFontSize(9));


        document.add(new Paragraph("\n\nHà Nội, ngày .... tháng .... năm ....")
                .setTextAlignment(TextAlignment.RIGHT));


        Table signature = new Table(3).setWidth(UnitValue.createPercentValue(100));

// Cột 1
        Cell cell1 = new Cell()
                .add(new Paragraph("TT XÉT NGHIỆM\n\n(Ký tên)").setTextAlignment(TextAlignment.CENTER).setBold())
                .setBorder(Border.NO_BORDER);

// Cột 2
        Cell cell2 = new Cell()
                .add(new Paragraph("HỘI ĐỒNG KHOA HỌC\n\n(Ký tên)").setTextAlignment(TextAlignment.CENTER).setBold())
                .setBorder(Border.NO_BORDER);

// Cột 3
        Cell cell3 = new Cell()
                .add(new Paragraph("CÔNG AN ĐIỀN CÔNG TY\n\n(Ký tên, đóng dấu)").setTextAlignment(TextAlignment.CENTER).setBold())
                .setBorder(Border.NO_BORDER);

// Thêm các cell vào bảng
        signature.addCell(cell1);
        signature.addCell(cell2);
        signature.addCell(cell3);

        document.add(signature);

        document.close();

    }

    private int calculateAge(Date dob) {
        LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}

