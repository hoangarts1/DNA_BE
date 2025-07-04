package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.NumberSampleDTO;

import java.util.List;

public interface NumberSampleService {
    NumberSampleDTO create(NumberSampleDTO dto);
    NumberSampleDTO update(Long id, NumberSampleDTO dto);
    NumberSampleDTO getById(Long id);
    List<NumberSampleDTO> getAll();
    void delete(Long id);
    double calculateTotalPrice(String serviceType, int sampleQuantity, double basePrice);

}
