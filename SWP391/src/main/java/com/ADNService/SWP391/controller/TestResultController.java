package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestResultDTO;
import com.ADNService.SWP391.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/TestResults")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @PostMapping
    public TestResultDTO create(@RequestBody TestResultDTO dto) {
        return testResultService.createTestResult(dto);
    }

    @GetMapping
    public List<TestResultDTO> getAll() {
        return testResultService.getAllTestResults();
    }

    @GetMapping("/{id}")
    public TestResultDTO getById(@PathVariable Long id) {
        return testResultService.getTestResultById(id);
    }

    @PutMapping("/{id}")
    public TestResultDTO update(@PathVariable Long id, @RequestBody TestResultDTO dto) {
        return testResultService.updateTestResult(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
    }
}
