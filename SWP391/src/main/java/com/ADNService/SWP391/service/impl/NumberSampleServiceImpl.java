package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.NumberSampleDTO;
import com.ADNService.SWP391.entity.NumberSample;
import com.ADNService.SWP391.repository.NumberSampleRepository;
import com.ADNService.SWP391.service.NumberSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NumberSampleServiceImpl implements NumberSampleService {

    @Autowired
    private NumberSampleRepository numberSampleRepository;

    private NumberSampleDTO convertToDTO(NumberSample ns) {
        return new NumberSampleDTO(
                ns.getId(),
                ns.getBaseQuantity(),
                ns.getExtraPricePerSample(),
                ns.getServiceType()
        );
    }

    private NumberSample convertToEntity(NumberSampleDTO dto) {
        return new NumberSample(
                dto.getBaseQuantity(),
                dto.getExtraPricePerSample(),
                dto.getServiceType().toUpperCase() // chuẩn hoá DAN_SU / HANH_CHINH
        );
    }

    @Override
    public NumberSampleDTO create(NumberSampleDTO dto) {
        NumberSample entity = convertToEntity(dto);
        return convertToDTO(numberSampleRepository.save(entity));
    }

    @Override
    public NumberSampleDTO update(Long id, NumberSampleDTO dto) {
        NumberSample existing = numberSampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NumberSample not found with ID: " + id));

        existing.setBaseQuantity(dto.getBaseQuantity());
        existing.setExtraPricePerSample(dto.getExtraPricePerSample());
        existing.setServiceType(dto.getServiceType().toUpperCase());

        return convertToDTO(numberSampleRepository.save(existing));
    }

    @Override
    public NumberSampleDTO getById(Long id) {
        return numberSampleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("NumberSample not found with ID: " + id));
    }

    @Override
    public List<NumberSampleDTO> getAll() {
        return numberSampleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        numberSampleRepository.deleteById(id);
    }

    public double calculateTotalPrice(String serviceType, int sampleQuantity, double basePrice) {
        NumberSample config = numberSampleRepository.findByServiceType(serviceType.toUpperCase())
                .orElseThrow(() -> new RuntimeException("NumberSample config not found for serviceType: " + serviceType));

        int baseQuantity = config.getBaseQuantity();
        double extraPricePerSample = config.getExtraPricePerSample();

        if (sampleQuantity > baseQuantity) {
            int extraSamples = sampleQuantity - baseQuantity;
            return basePrice + (extraSamples * extraPricePerSample);
        } else {
            return basePrice;
        }
    }
}
