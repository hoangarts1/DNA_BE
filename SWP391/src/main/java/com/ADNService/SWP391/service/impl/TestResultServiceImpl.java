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
    private CustomerRepository customerRepository;
    @Autowired
    private TestSampleRepository testSampleRepository;


    @Override
    public TestResultDTO createTestResult(TestResultDTO dto) {
        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + dto.getOrderId() + " does not exist."));

        if (testResultRepository.findAll().stream().anyMatch(result ->
                result.getTestOrder().getOrderId().equals(dto.getOrderId()))) {
            throw new RuntimeException("OrderID already exists in TestResult.");
        }

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("CustomerID " + dto.getCustomerId() + " does not exist."));

        // ✅ Lấy TestSample theo OrderId
        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(dto.getOrderId());

        if (samples.size() < 2) {
            throw new RuntimeException("Không đủ 2 mẫu để so sánh trong đơn hàng này.");
        }

        // Lấy sampleId1 và sampleId2
        Long sampleId1 = samples.get(0).getId();
        Long sampleId2 = samples.get(1).getId();

        // Lấy TestResultSample từ 2 mẫu trên
        List<TestResultSample> sampleList1 = testResultSampleRepository.findByTestSampleId(sampleId1);
        List<TestResultSample> sampleList2 = testResultSampleRepository.findByTestSampleId(sampleId2);

        // So sánh kết quả
        String result = compareSamples(sampleList1, sampleList2);

        TestResult testResult = new TestResult();
        testResult.setTestOrder(order);
        testResult.setCustomer(customer);
        testResult.setResult(result);
        testResult.setResultUrl("Kết quả sẽ được cập nhật sau");

        TestResult saved = testResultRepository.save(testResult);

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
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist."));
    }

    @Override
    public TestResultDTO updateTestResult(Long id, TestResultDTO dto) {
        // Kiểm tra TestResult tồn tại
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist."));

        // Kiểm tra TestOrder tồn tại
        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + dto.getOrderId() + " does not exist."));

        // Kiểm tra Customer tồn tại
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("CustomerID " + dto.getCustomerId() + " does not exist."));

        // Lấy TestSample theo orderId
        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(dto.getOrderId());

        if (samples.size() < 2) {
            throw new RuntimeException("Không đủ 2 mẫu để so sánh trong đơn hàng này.");
        }

        // Lấy sampleId1 và sampleId2
        Long sampleId1 = samples.get(0).getId();
        Long sampleId2 = samples.get(1).getId();

        // Lấy danh sách allele để so sánh
        List<TestResultSample> sampleList1 = testResultSampleRepository.findByTestSampleId(sampleId1);
        List<TestResultSample> sampleList2 = testResultSampleRepository.findByTestSampleId(sampleId2);

        // So sánh và kết luận
        String resultText = compareSamples(sampleList1, sampleList2);

        // Cập nhật kết quả
        result.setTestOrder(order);
        result.setCustomer(customer);
        result.setResult(resultText);
        result.setResultUrl(dto.getResultUrl());

        TestResult updated = testResultRepository.save(result);
        return convertToDTO(updated);
    }


    @Override
    public void deleteTestResult(Long id) {
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist."));
        testResultRepository.deleteById(id);
    }

    // Hàm so sánh chính
    private String compareSamples(List<TestResultSample> sampleList1, List<TestResultSample> sampleList2) {
        int unmatchedCount = 0;

        for (TestResultSample s1 : sampleList1) {
            boolean matched = false;

            for (TestResultSample s2 : sampleList2) {
                if (isLocusMatched(s1, s2)) {
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                unmatchedCount++;
                if (unmatchedCount >= 2) {
                    return "Không có quan hệ huyết thống";
                }
            }
        }
        return "Có quan hệ huyết thống";
    }

    // Hàm so sánh locus
    private boolean isLocusMatched(TestResultSample sample1, TestResultSample sample2) {
        if (!sample1.getLocusName().equals(sample2.getLocusName())) {
            return false;
        }
        return (sample1.getAllele1().equals(sample2.getAllele1()) || sample1.getAllele1().equals(sample2.getAllele2())
                || sample1.getAllele2().equals(sample2.getAllele1()) || sample1.getAllele2().equals(sample2.getAllele2()));
    }

    // Chuyển đổi sang DTO
    private TestResultDTO convertToDTO(TestResult result) {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(result.getId());
        dto.setOrderId(result.getTestOrder() != null ? result.getTestOrder().getOrderId() : null);
        dto.setCustomerId(result.getCustomer() != null ? result.getCustomer().getId() : null);
        dto.setResult(result.getResult());
        dto.setResultUrl(result.getResultUrl());
        return dto;
    }
}
