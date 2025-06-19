package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.dto.TestResultSampleDTO;

import java.util.List;

public interface TestResultService {

    TestResultDTO createTestResult(TestResultDTO dto);

    List<TestResultDTO> getAllTestResults();

    TestResultDTO getTestResultById(Long id);


    TestResultDTO updateTestResult(Long id, TestResultDTO dto);

    void deleteTestResult(Long id);

    void compareAndSaveResult(Long testResultId, Long sampleId1, Long sampleId2);

    List<TestResultSampleDTO> getTestResultSamplesByTestResultId(Long testResultId);

}
