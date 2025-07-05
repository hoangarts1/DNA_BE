package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.SampleTypeDTO;
import com.ADNService.SWP391.service.SampleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sample-types")
public class SampleTypeController {

    @Autowired
    private SampleTypeService sampleTypeService;

    @PostMapping
    public ResponseEntity<SampleTypeDTO> create(@RequestBody SampleTypeDTO dto) {
        return ResponseEntity.ok(sampleTypeService.create(dto));
    }

    @GetMapping
    public List<SampleTypeDTO> getAll() {
        return sampleTypeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleTypeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleTypeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SampleTypeDTO> update(@PathVariable Long id, @RequestBody SampleTypeDTO dto) {
        return ResponseEntity.ok(sampleTypeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sampleTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
