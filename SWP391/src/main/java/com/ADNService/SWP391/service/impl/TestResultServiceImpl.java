package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.entity.*;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        if (testResultRepository.findAll().stream().anyMatch(existingResult ->
                result.getTestOrder().getOrderId().equals(dto.getOrderId()))) {
            throw new RuntimeException("OrderID is exist in TestResult");
        }
        if (testResultRepository.findAll().stream().anyMatch(existingResult ->
                result.getTestResultSample().getId().equals(dto.getTestResultSampleId()))){
            throw new RuntimeException("TestResultSampleID is exist in TestResult");
        }
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("AccountID "+ dto.getAccountId() + " does not exist." ));

        result.setTestOrder(order);
        result.setTestResultSample(sample);
        result.setAccount(account);
        result.setResult(dto.getResult());
        result.setResultUrl(dto.getResultUrl());

        TestResult updated = testResultRepository.save(result);
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
}
