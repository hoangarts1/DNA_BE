package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestSampleDTO;
import com.ADNService.SWP391.service.TestSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testSamples")
public class TestSampleController {

    @Autowired
    private TestSampleService testSampleService;

    @PostMapping
    public TestSampleDTO create(@RequestBody TestSampleDTO dto) {
        return testSampleService.createTestSample(dto);
    }

    @GetMapping
    public List<TestSampleDTO> getAll() {
        return testSampleService.getAllTestSamples();
    }

    @GetMapping("/{id}")
    public TestSampleDTO getById(@PathVariable Long id) {
        return testSampleService.getTestSampleById(id);
    }

    @PutMapping("/{id}")
    public TestSampleDTO update(@PathVariable Long id, @RequestBody TestSampleDTO dto) {
        return testSampleService.updateTestSample(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        testSampleService.deleteTestSample(id);
    }

    @GetMapping("/TestOrder/{orderId}/TestSamples")
    public ResponseEntity<List<TestSampleDTO>> getTestSamplesByOrderId(@PathVariable String orderId) {
        List<TestSampleDTO> samples = testSampleService.getTestSamplesByOrderId(orderId);
        return ResponseEntity.ok(samples);
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TestSampleDTO>> getByOrderId(@PathVariable Long orderId) {
        List<TestSampleDTO> samples = testSampleService.getTestSamplesByOrderId(orderId);
        return ResponseEntity.ok(samples);
    }
}
