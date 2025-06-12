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
        TestOrder order = testOrderRepository.findById(dto.getOrderId()).orElse(null);
        TestResultSample sample = testResultSampleRepository.findById(dto.getTestResultSampleId()).orElse(null);
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);

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
        TestResult result = testResultRepository.findById(id).orElse(null);
        if (result == null) return null;

        TestOrder order = testOrderRepository.findById(dto.getOrderId()).orElse(null);
        TestResultSample sample = testResultSampleRepository.findById(dto.getTestResultSampleId()).orElse(null);
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);

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
