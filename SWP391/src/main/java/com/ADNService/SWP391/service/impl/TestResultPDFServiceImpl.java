package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.*;
import com.ADNService.SWP391.service.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
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

    public void generatePDF(TestResultPDFDTO report, OutputStream out) throws Exception {
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);


        List<Long> sampleIds = report.getTestSamples().stream()
                .map(TestSampleDTO::getId)
                .collect(Collectors.toList());

        // Ghi thông tin TestOrder
        document.add(new Paragraph("Thông tin đơn hàng: " + report.getTestOrder().getOrderId()));

        // Ghi thông tin TestSamples
        float[] columnWidths = {150F, 100F, 100F, 100F, 100F};
        Table table = new Table(columnWidths);
        table.addHeaderCell("Tên mẫu");
        table.addHeaderCell("Quan hệ");
        table.addHeaderCell("Loại mẫu");
        table.addHeaderCell("Ngày thu mẫu");
        table.addHeaderCell("Ký hiệu mẫu");

        for (TestSampleDTO sample : report.getTestSamples()) {
            table.addCell(sample.getName());
            table.addCell(sample.getRelationship());
            table.addCell(sample.getSampleType());
            table.addCell(sample.getOrderId() != null ? sample.getOrderId().toString() : "");
            table.addCell(sample.getKitCode());
        }
        document.add(table);


        Map<Long, String> sampleIdToName = report.getTestSamples().stream()
                .collect(Collectors.toMap(TestSampleDTO::getId, TestSampleDTO::getName));

        // Tổng số cột: 1 (Locus) + 2 * số lượng sample
        float[] columnWidth = new float[1 + 2 * sampleIds.size()];
        columnWidth[0] = 150F; // cột Locus
        for (int i = 1; i < columnWidth.length; i++) {
            columnWidth[i] = 100F; // các cột allele
        }

        Table locusTable = new Table(columnWidth);

// Thêm cột tiêu đề
        locusTable.addHeaderCell("Locus");

        for (Long sampleId : sampleIds) {
            String sampleName = sampleIdToName.getOrDefault(sampleId, "Unknown");
            locusTable.addHeaderCell("Allele 1 (" + sampleName + ")");
            locusTable.addHeaderCell("Allele 2 (" + sampleName + ")");
        }

        Map<String, List<TestResultSampleDTO>> groupedByLocus = report.getTestResultSamples()
                .stream()
                .collect(Collectors.groupingBy(TestResultSampleDTO::getLocusName));

        for (Map.Entry<String, List<TestResultSampleDTO>> entry : groupedByLocus.entrySet()) {
            String locusName = entry.getKey();
            List<TestResultSampleDTO> resultSamples = entry.getValue();

            // Map để lấy dữ liệu theo SampleID
            Map<Long, String[]> alleleMap = resultSamples.stream()
                    .collect(Collectors.toMap(
                            TestResultSampleDTO::getTestSampleId,
                            rs -> new String[]{rs.getAllele1(), rs.getAllele2()}
                    ));

            // In từng hàng
            locusTable.addCell(locusName);

            for (Long sampleId : sampleIds) {
                String[] alleles = alleleMap.getOrDefault(sampleId, new String[]{"", ""});
                locusTable.addCell(alleles[0]); // Allele 1
                locusTable.addCell(alleles[1]); // Allele 2
            }
        }

        document.add(locusTable);


// Ghi thông tin TestResult
        document.add(new Paragraph("\n📌 Kết quả phân tích mẫu:"));

        for (TestResultDTO rs : report.getTestResult()) {
            Long sample1Id = rs.getSampleId1();
            Long sample2Id = rs.getSampleId2();

            String sample1Name = sampleIdToName.getOrDefault(sample1Id, "Mẫu " + sample1Id);
            String sample2Name = sampleIdToName.getOrDefault(sample2Id, "Mẫu " + sample2Id);

            document.add(new Paragraph("- Mẫu: " + sample1Name + " (ID: " + sample1Id + ")"));
            document.add(new Paragraph("- Mẫu: " + sample2Name + " (ID: " + sample2Id + ")"));
            document.add(new Paragraph("- Kết luận: " + rs.getResult()));
            document.add(new Paragraph("- Tỉ lệ: " + rs.getResultPercent()));
            document.add(new Paragraph("")); // dòng trắng ngăn cách
        }

        document.close();
    }
}
