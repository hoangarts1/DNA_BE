package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.entity.TestResultSample;
import com.ADNService.SWP391.service.TestResultSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/TestResultSamples")
public class TestResultSampleController {

    @Autowired
    private TestResultSampleService testResultSampleService;

    @PostMapping
    public TestResultSampleDTO create(@RequestBody TestResultSampleDTO dto) {
        return testResultSampleService.createTestResultSample(dto);
    }

    @GetMapping
    public List<TestResultSampleDTO> getAll() {
        return testResultSampleService.getAllTestResultSamples();
    }

    @GetMapping("/{id}")
    public TestResultSampleDTO getById(@PathVariable Long id) {
        return testResultSampleService.getTestResultSampleById(id);
    }

    @PutMapping("/{id}")
    public TestResultSampleDTO update(@PathVariable Long id, @RequestBody TestResultSampleDTO dto) {
        return testResultSampleService.updateTestResultSample(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        testResultSampleService.deleteTestResultSample(id);
    }

    @GetMapping("/TestSample/{testSampleId}")
    public ResponseEntity<List<TestResultSampleDTO>> getTestResultSampleIdsByTestSampleId(@PathVariable String testSampleId) {
        List<TestResultSampleDTO> ids = testResultSampleService.getTestResultSampleIdByTestSampleId(testSampleId);
        return ResponseEntity.ok(ids);
    }
    @GetMapping("/TestOrder/{orderId}")
    public ResponseEntity<List<TestResultSampleDTO>> getTestResultSamplesByOrderId(@PathVariable String orderId) {
        List<TestResultSampleDTO> result = testResultSampleService.getTestResultSamplesByOrderId(orderId);
        return ResponseEntity.ok(result);
    }



}
