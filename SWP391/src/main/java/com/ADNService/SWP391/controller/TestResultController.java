package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    // Tạo kết quả xét nghiệm mới (Chỉ cần truyền orderId và customerId)
    @PostMapping("/create")
    public TestResultDTO createTestResult(@RequestBody TestResultDTO dto) {
        return testResultService.createTestResult(dto);
    }

    // Lấy tất cả kết quả xét nghiệm
    @GetMapping
    public List<TestResultDTO> getAllTestResults() {
        return testResultService.getAllTestResults();
    }

    // Lấy kết quả xét nghiệm theo ID
    @GetMapping("/{id}")
    public TestResultDTO getTestResultById(@PathVariable Long id) {
        return testResultService.getTestResultById(id);
    }

    // Cập nhật kết quả xét nghiệm
    @PutMapping("/{id}")
    public TestResultDTO updateTestResult(@PathVariable Long id, @RequestBody TestResultDTO dto) {
        return testResultService.updateTestResult(id, dto);
    }

    // Xóa kết quả xét nghiệm
    @DeleteMapping("/{id}")
    public void deleteTestResult(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
    }
}

