package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestResultSampleDTO;

import java.util.List;

public interface TestResultSampleService {

    TestResultSampleDTO createTestResultSample(TestResultSampleDTO dto);

    List<TestResultSampleDTO> getAllTestResultSamples();

    TestResultSampleDTO getTestResultSampleById(Long id);

    TestResultSampleDTO updateTestResultSample(Long id, TestResultSampleDTO dto);

    void deleteTestResultSample(Long id);
}
