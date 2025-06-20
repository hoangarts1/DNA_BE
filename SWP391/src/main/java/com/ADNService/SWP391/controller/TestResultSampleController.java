package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.entity.TestResult;
import com.ADNService.SWP391.service.TestResultSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-result-samples")
public class TestResultSampleController {

    @Autowired
    private TestResultSampleService testResultSampleService;

    @GetMapping("/by-test-sample/{testSampleId}")
    public List<TestResultSampleDTO> getByTestSampleId(@PathVariable Long testSampleId) {
        return testResultSampleService.getTestResultSamplesByTestSampleId(testSampleId);
    }

    @GetMapping("/{id}")
    public TestResultSampleDTO getById(@PathVariable Long id) {
        return testResultSampleService.getTestResultSampleById(id);
    }

    @PostMapping
    public TestResultSampleDTO createTestResultSample(@RequestBody TestResultSampleDTO dto) {
        return testResultSampleService.createTestResultSample(dto);
    }

    @PutMapping("/{id}")
    public TestResultSampleDTO updateTestResultSample(@PathVariable Long id, @RequestBody TestResultSampleDTO dto) {
        return testResultSampleService.updateTestResultSample(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTestResultSample(@PathVariable Long id) {
        testResultSampleService.deleteTestResultSample(id);
    }
    @PostMapping("/list")
    public List<TestResultSampleDTO> createTestResultSamples(@RequestBody List<TestResultSampleDTO> dtoList) {
        return testResultSampleService.createTestResultSamples(dtoList);
    }
//    @PostMapping("/compare")
//    public ResponseEntity<TestResult> compareSamples(@RequestParam Long orderId,
//                                                     @RequestParam Long sampleId1,
//                                                     @RequestParam Long sampleId2,
//                                                     @RequestParam Long accountId) {
//        TestResult testResult = testResultSampleService.generateTestResult(orderId, sampleId1, sampleId2, accountId);
//        return ResponseEntity.ok(testResult);
//    }
}
