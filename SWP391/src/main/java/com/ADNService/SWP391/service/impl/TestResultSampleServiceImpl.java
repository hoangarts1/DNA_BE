package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.entity.TestResult;
import com.ADNService.SWP391.entity.TestResultSample;
import com.ADNService.SWP391.entity.TestSample;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestResultSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestResultSampleServiceImpl implements TestResultSampleService {

    @Autowired
    private TestResultSampleRepository testResultSampleRepository;

    @Autowired
    private TestSampleRepository testSampleRepository;
    @Autowired
    private TestOrderRepository testOrderRepository;
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<TestResultSampleDTO> getTestResultSamplesByTestSampleId(Long testSampleId) {
        List<TestResultSample> samples = testResultSampleRepository.findByTestSampleId(testSampleId);
        return samples.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TestResultSampleDTO getTestResultSampleById(Long id) {
        TestResultSample sample = testResultSampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResultSample not found with id: " + id));
        return convertToDTO(sample);
    }

    @Override
    public TestResultSampleDTO createTestResultSample(TestResultSampleDTO dto) {
        TestSample testSample = testSampleRepository.findById(dto.getTestSampleId())
                .orElseThrow(() -> new RuntimeException("TestSample not found with id: " + dto.getTestSampleId()));

        TestResultSample sample = new TestResultSample(testSample, dto.getLocusName(), dto.getAllele1(), dto.getAllele2());
        TestResultSample saved = testResultSampleRepository.save(sample);

        return convertToDTO(saved);
    }
    @Override
    public List<TestResultSampleDTO> createTestResultSamples(List<TestResultSampleDTO> dtoList) {

        // Validate trùng locus
        validateUniqueLocus(dtoList);

        List<TestResultSample> samples = dtoList.stream().map(dto -> {
            TestSample testSample = testSampleRepository.findById(dto.getTestSampleId())
                    .orElseThrow(() -> new RuntimeException("TestSample not found with id: " + dto.getTestSampleId()));
            return new TestResultSample(testSample, dto.getLocusName(), dto.getAllele1(), dto.getAllele2());
        }).collect(Collectors.toList());

        List<TestResultSample> savedSamples = testResultSampleRepository.saveAll(samples);

        return savedSamples.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    @Override
    public TestResultSampleDTO updateTestResultSample(Long id, TestResultSampleDTO dto) {
        TestResultSample sample = testResultSampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResultSample not found with id: " + id));

        sample.setLocusName(dto.getLocusName());
        sample.setAllele1(dto.getAllele1());
        sample.setAllele2(dto.getAllele2());

        TestResultSample updated = testResultSampleRepository.save(sample);

        return convertToDTO(updated);
    }

    @Override
    public void deleteTestResultSample(Long id) {
        if (!testResultSampleRepository.existsById(id)) {
            throw new RuntimeException("TestResultSample not found with id: " + id);
        }
        testResultSampleRepository.deleteById(id);
    }

    private TestResultSampleDTO convertToDTO(TestResultSample sample) {
        return new TestResultSampleDTO(
                sample.getId(),
                sample.getTestSample().getId(),
                sample.getLocusName(),
                sample.getAllele1(),
                sample.getAllele2()
        );
    }

    private void validateUniqueLocus(List<TestResultSampleDTO> dtoList) {
        Set<String> uniqueKeys = new HashSet<>();

        for (TestResultSampleDTO dto : dtoList) {
            String key = dto.getTestSampleId() + "-" + dto.getLocusName();

            if (!uniqueKeys.add(key)) {
                throw new RuntimeException("Duplicate locus: " + dto.getLocusName() + " for TestSampleId: " + dto.getTestSampleId());
            }
        }
    }

    // Hàm so sánh 2 locus
    public boolean isLocusMatched(TestResultSample sample1, TestResultSample sample2) {
        if (!sample1.getLocusName().equals(sample2.getLocusName())) {
            return false;
        }
        return (sample1.getAllele1().equals(sample2.getAllele1()) || sample1.getAllele1().equals(sample2.getAllele2()) ||
                sample1.getAllele2().equals(sample2.getAllele1()) || sample1.getAllele2().equals(sample2.getAllele2()));
    }

    // Hàm so sánh toàn bộ locus
    public String compareSamples(List<TestResultSample> sampleList1, List<TestResultSample> sampleList2) {
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
                // Nếu đã có 2 locus không trùng thì kết luận luôn
                if (unmatchedCount >= 2) {
                    return "Không có quan hệ huyết thống";
                }
            }
        }

        // Nếu chưa có 2 locus không trùng thì kết luận có quan hệ
        return "Có quan hệ huyết thống";
    }


    public TestResult generateTestResult(Long orderId, Long sampleId1, Long sampleId2, Long customerId) {
        // Lấy danh sách locus của mẫu 1 và mẫu 2
        List<TestResultSample> sampleList1 = testResultSampleRepository.findByTestSampleId(sampleId1);
        List<TestResultSample> sampleList2 = testResultSampleRepository.findByTestSampleId(sampleId2);

        // So sánh và kết luận quan hệ huyết thống
        String result = compareSamples(sampleList1, sampleList2);

        // Tạo đối tượng TestResult
        TestResult testResult = new TestResult();

        // Set TestOrder nếu tìm thấy
        testOrderRepository.findById(orderId).ifPresent(testResult::setTestOrder);

        // Set Customer nếu tìm thấy
        customerRepository.findById(customerId).ifPresent(testResult::setCustomer);

        testResult.setResult(result);
        testResult.setResultUrl("Kết quả sẽ được cập nhật sau");

        // Lưu vào database và trả về kết quả
        return testResultRepository.save(testResult);
    }

}


