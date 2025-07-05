package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.dto.TestSampleDTO;
import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.dto.TestResultPDFDTO;

import java.io.OutputStream;
import java.util.List;

public interface TestResultPDFService {
    TestResultPDFDTO getFullTestResultReport(Long orderId);
    TestResultPDFDTO getTestResultReportBySamples(Long sampleId1, Long sampleId2);

    void generatePDF(TestResultPDFDTO report, OutputStream out) throws Exception;
}
