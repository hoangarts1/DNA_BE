package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.service.TestResultSampleService;
import com.ADNService.SWP391.service.impl.TestResultPDFServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-result-samples")
public class TestResultSampleController {

    @Autowired
    private TestResultSampleService testResultSampleService;
    @Autowired
    private TestResultPDFServiceImpl testResultPDFService;

    @GetMapping("/by-test-sample/{testSampleId}")
    public List<TestResultSampleDTO> getByTestSampleId(@PathVariable Long testSampleId) {
        return testResultSampleService.getTestResultSamplesByTestSampleId(testSampleId);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TestResultSampleDTO>> getTestResultSamplesByOrderId(@PathVariable Long orderId) {
        List<TestResultSampleDTO> samples = testResultSampleService.getTestResultSamplesByOrderId(orderId);
        return ResponseEntity.ok(samples);
    }

    @GetMapping("/{id}")
    public TestResultSampleDTO getById(@PathVariable Long id) {
        return testResultSampleService.getTestResultSampleById(id);
    }

    @PostMapping
    public TestResultSampleDTO createTestResultSample(@RequestBody TestResultSampleDTO dto) {
        return testResultSampleService.createTestResultSample(dto);
    }

    @PostMapping("/list")
    public List<TestResultSampleDTO> createTestResultSamples(@RequestBody List<TestResultSampleDTO> dtoList) {
        return testResultSampleService.createTestResultSamples(dtoList);
    }

    @PutMapping("/{id}")
    public TestResultSampleDTO updateTestResultSample(@PathVariable Long id, @RequestBody TestResultSampleDTO dto) {
        return testResultSampleService.updateTestResultSample(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTestResultSample(@PathVariable Long id) {
        testResultSampleService.deleteTestResultSample(id);
    }


}