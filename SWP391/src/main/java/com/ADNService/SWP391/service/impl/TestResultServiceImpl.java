package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.entity.*;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestResultServiceImpl implements TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;

    @Autowired
    private TestResultSampleRepository testResultSampleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TestResultDTO createTestResult(TestResultDTO dto) {
        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order wiht ID " + dto.getOrderId() + " does not exist."));

        TestResultSample sample = testResultSampleRepository.findById(dto.getTestResultSampleId())
                .orElseThrow(() -> new RuntimeException("TestResultSample with ID " + dto.getTestResultSampleId() + " does not exist."));

        if (testResultRepository.findAll().stream().anyMatch(result ->
                result.getTestOrder().getOrderId().equals(dto.getOrderId()))) {
            throw new RuntimeException("OrderID is exist in TestResult");
        }
        if (testResultRepository.findAll().stream().anyMatch(result ->
                result.getTestResultSample().getId().equals(dto.getTestResultSampleId()))){
            throw new RuntimeException("TestResultSampleID is exist in TestResult");
        }
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("AccountID "+ dto.getAccountId() + " does not exist." ));

        TestResult result = new TestResult();
        result.setTestOrder(order);
        result.setTestResultSample(sample);
        result.setAccount(account);
        result.setResult(dto.getResult());
        result.setResultUrl(dto.getResultUrl());

        TestResult saved = testResultRepository.save(result);
//        compareAndSaveResult(saved.getId(), dto.getSampleId1(), dto.getSampleId2());
        return convertToDTO(saved);
    }

    @Override
    public List<TestResultDTO> getAllTestResults() {
        return testResultRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestResultDTO getTestResultById(Long id) {
        return testResultRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public TestResultDTO updateTestResult(Long id, TestResultDTO dto) {
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist. " ));


        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order wiht ID " + dto.getOrderId() + " does not exist."));

        TestResultSample sample = testResultSampleRepository.findById(dto.getTestResultSampleId())
                .orElseThrow(() -> new RuntimeException("TestResultSample with ID " + dto.getTestResultSampleId() + " does not exist."));

//        if (testResultRepository.findAll().stream().anyMatch(existingResult ->
//                result.getTestOrder().getOrderId().equals(dto.getOrderId()))) {
//            throw new RuntimeException("OrderID is exist in TestResult");
//        }
//        if (testResultRepository.findAll().stream().anyMatch(existingResult ->
//                result.getTestResultSample().getId().equals(dto.getTestResultSampleId()))){
//            throw new RuntimeException("TestResultSampleID is exist in TestResult");
//        }
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("AccountID "+ dto.getAccountId() + " does not exist." ));

        result.setTestOrder(order);
        result.setTestResultSample(sample);
        result.setAccount(account);
        result.setResult(dto.getResult());
        result.setResultUrl(dto.getResultUrl());

        TestResult updated = testResultRepository.save(result);
//        compareAndSaveResult(updated.getId(), dto.getSampleId1(), dto.getSampleId2());
        return convertToDTO(updated);
    }

    @Override
    public void deleteTestResult(Long id) {
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist. " ));
        testResultRepository.deleteById(id);
    }

    private TestResultDTO convertToDTO(TestResult result) {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(result.getId());
        dto.setOrderId(result.getTestOrder() != null ? result.getTestOrder().getOrderId() : null);
        dto.setTestResultSampleId(result.getTestResultSample() != null ? result.getTestResultSample().getId() : null);
        dto.setAccountId(result.getAccount() != null ? result.getAccount().getId() : null);
        dto.setResult(result.getResult());
        dto.setResultUrl(result.getResultUrl());
        return dto;
    }

    private Map<String, String> extractLocusMap(TestResultSample sample) {
        Map<String, String> locusMap = new HashMap<>();

        locusMap.put("Amelogenin", sample.getAmelogenin());
        locusMap.put("D3S1358", sample.getD3S1358());
        locusMap.put("D2S441", sample.getD2S441());
        locusMap.put("D10S1248", sample.getD10S1248());
        locusMap.put("D13S317", sample.getD13S317());
        locusMap.put("D16S539", sample.getD16S539());
        locusMap.put("CSF1PO", sample.getCsf1po());
        locusMap.put("TH01", sample.getTh01());
        locusMap.put("VWA", sample.getVwa());
        locusMap.put("D7S820", sample.getD7S820());
        locusMap.put("D21S11", sample.getD21S11());
        locusMap.put("Penta E", sample.getPentaE());
        locusMap.put("FGA", sample.getFga());
        locusMap.put("D22S1045", sample.getD22S1045());
        locusMap.put("D8S1179", sample.getD8S1179());
        locusMap.put("D18S51", sample.getD18S51());
        locusMap.put("Penta D", sample.getPentaD());
        locusMap.put("D2S1339", sample.getD2S1339());
        locusMap.put("D19S433", sample.getD19S433());
        locusMap.put("D5S818", sample.getD5S818());
        locusMap.put("D1S1656", sample.getD1S1656());
        locusMap.put("TPOX", sample.getTpox());

        return locusMap;
    }

    @Override
    public void compareAndSaveResult(Long testResultId, Long sampleId1, Long sampleId2) {
        TestResult testResult = testResultRepository.findById(testResultId)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + testResultId + " does not exist."));

        TestResultSample sample1 = testResultSampleRepository.findById(sampleId1)
                .orElseThrow(() -> new RuntimeException("Sample with ID " + sampleId1 + " does not exist."));

        TestResultSample sample2 = testResultSampleRepository.findById(sampleId2)
                .orElseThrow(() -> new RuntimeException("Sample with ID " + sampleId2 + " does not exist."));

        Map<String, String> sample1Map = extractLocusMap(sample1);
        Map<String, String> sample2Map = extractLocusMap(sample2);

        int differentLocusCount = 0;

        for (String locus : sample1Map.keySet()) {
            String[] alleles1 = sample1Map.get(locus).split(",");
            String[] alleles2 = sample2Map.get(locus).split(",");

            boolean matched = false;
            for (String allele1 : alleles1) {
                for (String allele2 : alleles2) {
                    if (allele1.trim().equals(allele2.trim())) {
                        matched = true;
                        break;
                    }
                }
                if (matched) break;
            }

            if (!matched) {
                differentLocusCount++;
                if (differentLocusCount >= 2) {
                    break; // Nếu đã khác từ 2 locus thì không cần so tiếp
                }
            }
        }

        String result = (differentLocusCount >= 2) ? "Không có quan hệ huyết thống" : "Có quan hệ huyết thống";
        testResult.setResult(result);
        testResultRepository.save(testResult);
    }


}
