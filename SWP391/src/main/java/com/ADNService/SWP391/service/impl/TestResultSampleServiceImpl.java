package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.entity.TestResultSample;
import com.ADNService.SWP391.entity.TestSample;
import com.ADNService.SWP391.repository.TestResultSampleRepository;
import com.ADNService.SWP391.repository.TestSampleRepository;
import com.ADNService.SWP391.service.TestResultSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestResultSampleServiceImpl implements TestResultSampleService {

    @Autowired
    private TestResultSampleRepository testResultSampleRepository;

    @Autowired
    private TestSampleRepository testSampleRepository;

    @Override
    public TestResultSampleDTO createTestResultSample(TestResultSampleDTO dto) {
        TestSample testSample = testSampleRepository.findById(dto.getTestSampleId())
                .orElseThrow(() -> new RuntimeException("TestSample with ID " + dto.getTestSampleId() + " does not exist."));

        Optional<TestResultSample> existingSample = testResultSampleRepository.findById(dto.getTestSampleId());
        if (existingSample.isPresent()) {
            throw new RuntimeException("TestSample with ID "+ dto.getId() + " already exists in TestResultSample.");
        }

        TestResultSample result = new TestResultSample();
        result.setTestSample(testSample);
        result.setAmelogenin(dto.getAmelogenin());
        result.setD3S1358(dto.getD3S1358());
        result.setD2S441(dto.getD2S441());
        result.setD10S1248(dto.getD10S1248());
        result.setD13S317(dto.getD13S317());
        result.setD16S539(dto.getD16S539());
        result.setCsf1po(dto.getCsf1po());
        result.setTh01(dto.getTh01());
        result.setVwa(dto.getVwa());
        result.setD7S820(dto.getD7S820());
        result.setD21S11(dto.getD21S11());
        result.setPentaE(dto.getPentaE());
        result.setFga(dto.getFga());
        result.setD22S1045(dto.getD22S1045());
        result.setD8S1179(dto.getD8S1179());
        result.setD18S51(dto.getD18S51());
        result.setPentaD(dto.getPentaD());
        result.setD2S1339(dto.getD2S1339());
        result.setD19S433(dto.getD19S433());
        result.setD5S818(dto.getD5S818());
        result.setD1S1656(dto.getD1S1656());
        result.setTpox(dto.getTpox());

        TestResultSample saved = testResultSampleRepository.save(result);
        return convertToDTO(saved);
    }

    @Override
    public List<TestResultSampleDTO> getAllTestResultSamples() {
        return testResultSampleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestResultSampleDTO getTestResultSampleById(Long id) {
        return testResultSampleRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public TestResultSampleDTO updateTestResultSample(Long id, TestResultSampleDTO dto) {
        TestResultSample resultSample = testResultSampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResultSample with ID "+ id + " does not exist."));

        TestResultSample result = new TestResultSample();
        result.setAmelogenin(dto.getAmelogenin());
        result.setD3S1358(dto.getD3S1358());
        result.setD2S441(dto.getD2S441());
        result.setD10S1248(dto.getD10S1248());
        result.setD13S317(dto.getD13S317());
        result.setD16S539(dto.getD16S539());
        result.setCsf1po(dto.getCsf1po());
        result.setTh01(dto.getTh01());
        result.setVwa(dto.getVwa());
        result.setD7S820(dto.getD7S820());
        result.setD21S11(dto.getD21S11());
        result.setPentaE(dto.getPentaE());
        result.setFga(dto.getFga());
        result.setD22S1045(dto.getD22S1045());
        result.setD8S1179(dto.getD8S1179());
        result.setD18S51(dto.getD18S51());
        result.setPentaD(dto.getPentaD());
        result.setD2S1339(dto.getD2S1339());
        result.setD19S433(dto.getD19S433());
        result.setD5S818(dto.getD5S818());
        result.setD1S1656(dto.getD1S1656());
        result.setTpox(dto.getTpox());

        TestResultSample updated = testResultSampleRepository.save(result);
        return convertToDTO(updated);
    }

    @Override
    public void deleteTestResultSample(Long id) {
        TestResultSample resultSample = testResultSampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TestResultSample with ID "+ id + " does not exist."));
        testResultSampleRepository.deleteById(id);
    }

    private TestResultSampleDTO convertToDTO(TestResultSample result) {
        TestResultSampleDTO dto = new TestResultSampleDTO();
        dto.setId(result.getId());
        dto.setTestSampleId(result.getTestSample() != null ? result.getTestSample().getId() : null);
        dto.setAmelogenin(result.getAmelogenin());
        dto.setD3S1358(result.getD3S1358());
        dto.setD2S441(result.getD2S441());
        dto.setD10S1248(result.getD10S1248());
        dto.setD13S317(result.getD13S317());
        dto.setD16S539(result.getD16S539());
        dto.setCsf1po(result.getCsf1po());
        dto.setTh01(result.getTh01());
        dto.setVwa(result.getVwa());
        dto.setD7S820(result.getD7S820());
        dto.setD21S11(result.getD21S11());
        dto.setPentaE(result.getPentaE());
        dto.setFga(result.getFga());
        dto.setD22S1045(result.getD22S1045());
        dto.setD8S1179(result.getD8S1179());
        dto.setD18S51(result.getD18S51());
        dto.setPentaD(result.getPentaD());
        dto.setD2S1339(result.getD2S1339());
        dto.setD19S433(result.getD19S433());
        dto.setD5S818(result.getD5S818());
        dto.setD1S1656(result.getD1S1656());
        dto.setTpox(result.getTpox());
        return dto;
    }
}
