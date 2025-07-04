package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.NumberSampleDTO;
import com.ADNService.SWP391.entity.NumberSample;
import com.ADNService.SWP391.repository.NumberSampleRepository;
import com.ADNService.SWP391.service.NumberSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/number-sample")
public class NumberSampleController {

    @Autowired
    private NumberSampleRepository numberSampleRepository;

    @Autowired
    private NumberSampleService numberSampleService;


    private NumberSampleDTO toDTO(NumberSample entity) {
        return new NumberSampleDTO(
                entity.getId(),
                entity.getBaseQuantity(),
                entity.getExtraPricePerSample(),
                entity.getServiceType()
        );
    }

    @GetMapping
    public List<NumberSampleDTO> getAll() {
        return numberSampleRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{serviceType}")
    public NumberSampleDTO getByServiceType(@PathVariable String serviceType) {
        NumberSample ns = numberSampleRepository.findByServiceType(serviceType.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình mẫu cho loại đơn: " + serviceType));
        return toDTO(ns);
    }

    @PostMapping
    public NumberSampleDTO create(@RequestBody NumberSampleDTO dto) {
        NumberSample ns = new NumberSample(
                dto.getBaseQuantity(),
                dto.getExtraPricePerSample(),
                dto.getServiceType().toUpperCase()
        );
        return toDTO(numberSampleRepository.save(ns));
    }

    @PutMapping("/{id}")
    public NumberSampleDTO update(@PathVariable Long id, @RequestBody NumberSampleDTO dto) {
        NumberSample existing = numberSampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy NumberSample với ID: " + id));

        existing.setBaseQuantity(dto.getBaseQuantity());
        existing.setExtraPricePerSample(dto.getExtraPricePerSample());
        existing.setServiceType(dto.getServiceType().toUpperCase());

        return toDTO(numberSampleRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        numberSampleRepository.deleteById(id);
    }
    @GetMapping("/calculate")
    public ResponseEntity<Double> calculateTotalPrice(
            @RequestParam String serviceType,
            @RequestParam int sampleQuantity,
            @RequestParam double basePrice) {
        double total = numberSampleService.calculateTotalPrice(serviceType, sampleQuantity, basePrice);
        return ResponseEntity.ok(total);
    }

}
