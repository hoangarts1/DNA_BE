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

        // Lấy TestSample theo OrderId
        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(dto.getOrderId());

        Long sampleId1 = dto.getSampleId1();
        Long sampleId2 = dto.getSampleId2();

        // Check 2 mẫu không được trùng nhau
        if (sampleId1.equals(sampleId2)) {
            throw new RuntimeException("Hai mẫu so sánh không được trùng nhau.");
        }

        TestSample sample1 = testSampleRepository.findById(sampleId1)
                .orElseThrow(() -> new RuntimeException("Sample1 does not exist"));
        TestSample sample2 = testSampleRepository.findById(sampleId2)
                .orElseThrow(() -> new RuntimeException("Sample2 does not exist"));

        // Check 2 mẫu phải thuộc cùng 1 Order
        if (!sample1.getOrder().getOrderId().equals(order.getOrderId()) ||
                !sample2.getOrder().getOrderId().equals(order.getOrderId())) {
            throw new RuntimeException("Hai mẫu phải thuộc cùng một đơn hàng.");
        }
        List<TestResult> existingResults = testResultRepository.findBySampleIdPair(sampleId1, sampleId2);
        boolean isDuplicate = existingResults.stream().anyMatch(r ->
                r.getTestOrder().getOrderId().equals(dto.getOrderId())
        );

        if (isDuplicate) {
            throw new RuntimeException("Đã tồn tại kết quả với cùng đơn hàng và 2 mẫu này.");
        }


        TestResult testResult = new TestResult();
        testResult.setTestOrder(order);
        testResult.setSampleId1(sample1); // Lưu sampleId1
        testResult.setSampleId2(sample2); // Lưu sampleId2
        testResult.setResult(dto.getResult());
        testResult.setResultPercent(dto.getResultPercent());

        TestResult saved = testResultRepository.save(testResult);

        return convertToDTO(saved);
    }

    @Override
    public TestResultDTO updateTestResult(Long id, TestResultDTO dto) {
        // Kiểm tra TestResult tồn tại
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist."));

        // Kiểm tra TestOrder tồn tại
        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + dto.getOrderId() + " does not exist."));

        // Lấy danh sách mẫu thuộc order
        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(dto.getOrderId());

        Long sampleId1 = dto.getSampleId1();
        Long sampleId2 = dto.getSampleId2();

        // Kiểm tra 2 mẫu không được trùng nhau
        if (sampleId1.equals(sampleId2)) {
            throw new RuntimeException("Hai mẫu so sánh không được trùng nhau.");
        }

        // Kiểm tra 2 mẫu phải thuộc cùng đơn hàng
        boolean isSample1InOrder = samples.stream().anyMatch(s -> s.getId().equals(sampleId1));
        boolean isSample2InOrder = samples.stream().anyMatch(s -> s.getId().equals(sampleId2));

        if (!isSample1InOrder || !isSample2InOrder) {
            throw new RuntimeException("Hai mẫu phải thuộc đơn hàng " + order.getOrderId());
        }

        // Kiểm tra 2 mẫu có tồn tại không
        TestSample sample1 = testSampleRepository.findById(sampleId1)
                .orElseThrow(() -> new RuntimeException("Sample1 does not exist"));
        TestSample sample2 = testSampleRepository.findById(sampleId2)
                .orElseThrow(() -> new RuntimeException("Sample2 does not exist"));

        // Cập nhật dữ liệu
        result.setTestOrder(order);
        result.setSampleId1(sample1); // Cập nhật sampleId1
        result.setSampleId2(sample2); // Cập nhật sampleId2
        result.setResult(dto.getResult());
        result.setResultPercent(dto.getResultPercent());

        TestResult updated = testResultRepository.save(result);
        return convertToDTO(updated);
    }

    // Cập nhật convertToDTO
    private TestResultDTO convertToDTO(TestResult result) {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(result.getId());
        dto.setOrderId(result.getTestOrder() != null ? result.getTestOrder().getOrderId() : null);
        dto.setSampleId1(result.getSampleId1() != null ? result.getSampleId1().getId() : null); // Ánh xạ sampleId1
        dto.setSampleId2(result.getSampleId2() != null ? result.getSampleId2().getId() : null); // Ánh xạ sampleId2
        dto.setResult(result.getResult());
        dto.setResultPercent(result.getResultPercent());
        return dto;
    }

    // Các phương thức khác giữ nguyên
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
    public List<TestResultDTO> getTestResultByOrderId(Long orderId) {
        List<TestResult> results = testResultRepository.findByTestOrder_OrderId(orderId);
        return results.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestResultDTO> getTestResultBySampleIds(Long sampleId1, Long sampleId2) {
        return testResultRepository.findBySampleIdPair(sampleId1, sampleId2)
                .stream()
                .map(result -> convertToDTO(result))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteTestResult(Long id) {
        TestResult result = testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResult with ID " + id + " does not exist."));
        testResultRepository.deleteById(id);
    }

}