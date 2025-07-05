package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.entity.TestResultSample;
import com.ADNService.SWP391.entity.TestSample;
import com.ADNService.SWP391.repository.TestResultSampleRepository;
import com.ADNService.SWP391.repository.TestSampleRepository;
import com.ADNService.SWP391.service.TestResultSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestResultSampleServiceImpl implements TestResultSampleService {

    @Autowired
    private TestResultSampleRepository testResultSampleRepository;

    @Autowired
    private TestSampleRepository testSampleRepository;

    @Override
    public List<TestResultSampleDTO> getTestResultSamplesByTestSampleId(Long testSampleId) {
        List<TestResultSample> samples = testResultSampleRepository.findByTestSampleId(testSampleId);
        return samples.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TestResultSampleDTO> getTestResultSamplesByOrderId(Long orderId) {
        List<TestResultSample> samples = testResultSampleRepository.findByOrderId(orderId);
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
        validateUniqueLocus(dtoList);

        List<TestResultSample> samples = dtoList.stream().map(dto -> {
            TestSample testSample = testSampleRepository.findById(dto.getTestSampleId())
                    .orElseThrow(() -> new RuntimeException("TestSample not found with id: " + dto.getTestSampleId()));

            // Kiểm tra xem bản ghi đã tồn tại chưa
            Optional<TestResultSample> existingSample = testResultSampleRepository
                    .findByTestSampleIdAndLocusName(dto.getTestSampleId(), dto.getLocusName());

            TestResultSample sample;
            if (existingSample.isPresent()) {
                // Cập nhật bản ghi hiện có
                sample = existingSample.get();
                sample.setAllele1(dto.getAllele1());
                sample.setAllele2(dto.getAllele2());
            } else {
                // Tạo mới bản ghi
                sample = new TestResultSample(testSample, dto.getLocusName(), dto.getAllele1(), dto.getAllele2());
            }

            return sample;
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
}