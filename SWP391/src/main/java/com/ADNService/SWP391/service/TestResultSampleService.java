package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestResultSampleDTO;

import java.util.List;

public interface TestResultSampleService {
    List<TestResultSampleDTO> getTestResultSamplesByTestSampleId(Long testSampleId);
    TestResultSampleDTO getTestResultSampleById(Long id);
    TestResultSampleDTO createTestResultSample(TestResultSampleDTO dto);
    TestResultSampleDTO updateTestResultSample(Long id, TestResultSampleDTO dto);
    void deleteTestResultSample(Long id);
    List<TestResultSampleDTO> createTestResultSamples(List<TestResultSampleDTO> dtoList);
    List<TestResultSampleDTO> getTestResultSamplesByOrderId(Long orderId); // Thêm phương thức mới
}