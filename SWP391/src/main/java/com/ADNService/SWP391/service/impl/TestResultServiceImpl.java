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

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("CustomerID " + dto.getCustomerId() + " does not exist."));

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
        List<TestResultSample> sampleList1 = testResultSampleRepository.findByTestSampleId(sampleId1);
        List<TestResultSample> sampleList2 = testResultSampleRepository.findByTestSampleId(sampleId2);


        // So sánh kết quả
        String result =getRelationshipResult(sample1, sample2, sampleList1, sampleList2) ;

        // Tính phần trăm kết quả trùng
        String resultPercent = String.format("%.2f%%", calculateMatchingPercentage(sampleList1, sampleList2));

        TestResult testResult = new TestResult();
        testResult.setTestOrder(order);
        testResult.setCustomer(customer);
        testResult.setResult(result);
        testResult.setResultPercent(resultPercent);
        testResult.setResultUrl(dto.getResultUrl());

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
    public List<TestResultDTO> getTestResultByOrderId(Long orderId) {
        List<TestResult> results = testResultRepository.findByTestOrder_OrderId(orderId);

        return results.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

        // Lấy mẫu từ DB
        TestSample sample1 = testSampleRepository.findById(sampleId1)
                .orElseThrow(() -> new RuntimeException("Sample1 does not exist"));
        TestSample sample2 = testSampleRepository.findById(sampleId2)
                .orElseThrow(() -> new RuntimeException("Sample2 does not exist"));

        // Lấy dữ liệu so sánh
        List<TestResultSample> sampleList1 = testResultSampleRepository.findByTestSampleId(sampleId1);
        List<TestResultSample> sampleList2 = testResultSampleRepository.findByTestSampleId(sampleId2);

        // So sánh và kết luận
        String resultText = getRelationshipResult(sample1, sample2, sampleList1, sampleList2);

        // Tính phần trăm kết quả trùng
        String resultPercentText = String.format("%.2f%%", calculateMatchingPercentage(sampleList1, sampleList2));

        // Cập nhật dữ liệu
        result.setTestOrder(order);
        result.setCustomer(customer);
        result.setResult(resultText);
        result.setResultPercent(resultPercentText);
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

    private String getRelationshipResult(TestSample sample1, TestSample sample2, List<TestResultSample> sampleList1, List<TestResultSample> sampleList2) {
//        double probability = calculateRelationshipProbability(sampleList1, sampleList2, Map.of());
        double probability = calculateMatchingPercentage(sampleList1, sampleList2);
        String relationshipCode = relationship(sample1, sample2);

        if (relationshipCode.equals("Cha-Mẹ-Con") && probability >= 99.0) {
            return "có quan hệ huyết thống";
        } else if (relationshipCode.equals("Ông-Bà-Cháu") && probability >= 25.0) {
            return "có quan hệ huyết thống";
        } else if (relationshipCode.equals("Anh-Chị-Em") && probability >= 50.0) {
            return "có quan hệ huyết thống";
        } else {
            return "không có quan hệ huyết thống";
        }
    }


    private double calculateMatchingPercentage(List<TestResultSample> sampleList1, List<TestResultSample> sampleList2) {
        int totalLocus = sampleList1.size();
        int matchedLocus = 0;

        for (TestResultSample s1 : sampleList1) {
            for (TestResultSample s2 : sampleList2) {
                if (s1.getLocusName().equalsIgnoreCase(s2.getLocusName())) {
                    // Chỉ cần 1 allele trùng là tính
                    if (s1.getAllele1().equals(s2.getAllele1()) || s1.getAllele1().equals(s2.getAllele2())
                            || s1.getAllele2().equals(s2.getAllele1()) || s1.getAllele2().equals(s2.getAllele2())) {
                        matchedLocus++;
                        break; // Nếu trùng rồi thì nhảy qua locus tiếp theo
                    }
                }
            }
        }

        return (double) matchedLocus / totalLocus * 100.0;
    }



    private String relationship(TestSample sample1, TestSample sample2) {
        if ((sample1.getRelationship().equalsIgnoreCase("CHA") && sample2.getRelationship().equalsIgnoreCase("CON")
                || sample1.getRelationship().equalsIgnoreCase("MẸ") && sample2.getRelationship().equalsIgnoreCase("CON"))
            ||(sample1.getRelationship().equalsIgnoreCase("CON") && sample2.getRelationship().equalsIgnoreCase("CHA")
                || sample1.getRelationship().equalsIgnoreCase("CON") && sample2.getRelationship().equalsIgnoreCase("MẸ"))){
            return "Cha-Mẹ-Con";
        }
        else if ((sample1.getRelationship().equalsIgnoreCase("ÔNG") && sample2.getRelationship().equalsIgnoreCase("CHÁU")
                || sample1.getRelationship().equalsIgnoreCase("BÀ") && sample2.getRelationship().equalsIgnoreCase("CHÁU"))
                ||(sample1.getRelationship().equalsIgnoreCase("CHÁU") && sample2.getRelationship().equalsIgnoreCase("ÔNG")
                || sample1.getRelationship().equalsIgnoreCase("CHÁU") && sample2.getRelationship().equalsIgnoreCase("BÀ"))){
            return "Ông-Bà-Cháu";
        }
        else if ((sample1.getRelationship().equalsIgnoreCase("ANH") && sample2.getRelationship().equalsIgnoreCase("EM")
                || sample1.getRelationship().equalsIgnoreCase("CHỊ") && sample2.getRelationship().equalsIgnoreCase("EM"))
                ||(sample1.getRelationship().equalsIgnoreCase("EM") && sample2.getRelationship().equalsIgnoreCase("ANH")
                || sample1.getRelationship().equalsIgnoreCase("EM") && sample2.getRelationship().equalsIgnoreCase("CHỊ"))){
            return "Anh-Chị-Em";
        }
        return "0";
    }

    // Chuyển đổi sang DTO
    private TestResultDTO convertToDTO(TestResult result) {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(result.getId());
        dto.setOrderId(result.getTestOrder() != null ? result.getTestOrder().getOrderId() : null);
        dto.setCustomerId(result.getCustomer() != null ? result.getCustomer().getId() : null);
        dto.setResult(result.getResult());
        dto.setResultPercent(result.getResultPercent());
        dto.setResultUrl(result.getResultUrl());
        return dto;
    }
}
