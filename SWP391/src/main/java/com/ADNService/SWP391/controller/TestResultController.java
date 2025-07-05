package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    // Tạo một hoặc nhiều kết quả xét nghiệm mới
    @PostMapping("/create")
    public ResponseEntity<List<TestResultDTO>> createTestResults(@RequestBody List<TestResultDTO> dtos) {
        List<TestResultDTO> results = dtos.stream()
                .map(dto -> testResultService.createTestResult(dto))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    // Các phương thức khác giữ nguyên
    @GetMapping
    public List<TestResultDTO> getAllTestResults() {
        return testResultService.getAllTestResults();
    }

    @GetMapping("/{id}")
    public TestResultDTO getTestResultById(@PathVariable Long id) {
        return testResultService.getTestResultById(id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TestResultDTO>> getTestResultByOrderId(@PathVariable Long orderId) {
        List<TestResultDTO> results = testResultService.getTestResultByOrderId(orderId);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{id}")
    public TestResultDTO updateTestResult(@PathVariable Long id, @RequestBody TestResultDTO dto) {
        return testResultService.updateTestResult(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTestResult(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
    }
}