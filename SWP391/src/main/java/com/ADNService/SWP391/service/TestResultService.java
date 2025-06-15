package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestResultDTO;

import java.util.List;

public interface TestResultService {

    TestResultDTO createTestResult(TestResultDTO dto);

    List<TestResultDTO> getAllTestResults();

    TestResultDTO getTestResultById(Long id);

    TestResultDTO updateTestResult(Long id, TestResultDTO dto);

    void deleteTestResult(Long id);
}
