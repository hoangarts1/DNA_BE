package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestResultSampleDTO;

import java.util.List;

public interface TestResultSampleService {

    TestResultSampleDTO createTestResultSample(TestResultSampleDTO dto);

    List<TestResultSampleDTO> getAllTestResultSamples();

    TestResultSampleDTO getTestResultSampleById(Long id);

    List<TestResultSampleDTO> getTestResultSampleIdByTestSampleId(String testSampleId);


    TestResultSampleDTO updateTestResultSample(Long id, TestResultSampleDTO dto);

    void deleteTestResultSample(Long id);


    List<TestResultSampleDTO> getTestResultSamplesByOrderId(String orderId);
}
