package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.TestSampleDTO;

import java.util.List;

public interface TestSampleService {
    TestSampleDTO createTestSample(TestSampleDTO dto);
    List<TestSampleDTO> getAllTestSamples();
    TestSampleDTO getTestSampleById(Long id);
    TestSampleDTO updateTestSample(Long id, TestSampleDTO dto);
    void deleteTestSample(Long id);
    List<TestSampleDTO> getTestSamplesByOrderId(Long orderId);

}
