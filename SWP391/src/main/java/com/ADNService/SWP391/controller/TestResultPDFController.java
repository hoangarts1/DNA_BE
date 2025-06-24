package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultPDFDTO;
import com.ADNService.SWP391.service.TestResultPDFService;
import com.ADNService.SWP391.service.impl.TestResultPDFServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/pdf")
public class TestResultPDFController {

    @Autowired
    private TestResultPDFServiceImpl pdfExportService;

    @Autowired
    private TestResultPDFService reportService;

    @GetMapping("/export/{orderId}")
    public void exportPDF(@PathVariable Long orderId, HttpServletResponse response) throws Exception {
        TestResultPDFDTO report = reportService.getFullTestResultReport(orderId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=result_" + orderId + ".pdf");

        OutputStream out = response.getOutputStream();
        pdfExportService.generatePDF(report, out);
    }
}
