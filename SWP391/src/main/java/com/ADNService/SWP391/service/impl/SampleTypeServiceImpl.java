package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.SampleTypeDTO;
import com.ADNService.SWP391.entity.SampleType;
import com.ADNService.SWP391.repository.SampleTypeRepository;
import com.ADNService.SWP391.service.SampleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SampleTypeServiceImpl implements SampleTypeService {

    @Autowired
    private SampleTypeRepository sampleTypeRepository;

    @Override
    public SampleTypeDTO create(SampleTypeDTO dto) {
        if (sampleTypeRepository.existsBySampleType(dto.getSampleType())) {
            throw new RuntimeException("Sample type already exists");
        }
        SampleType type = new SampleType();
        type.setSampleType(dto.getSampleType());
        return toDTO(sampleTypeRepository.save(type));
    }

    @Override
    public List<SampleTypeDTO> getAll() {
        return sampleTypeRepository.findAll()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SampleTypeDTO getById(Long id) {
        SampleType type = sampleTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return toDTO(type);
    }

    @Override
    public SampleTypeDTO update(Long id, SampleTypeDTO dto) {
        SampleType type = sampleTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        type.setSampleType(dto.getSampleType());
        return toDTO(sampleTypeRepository.save(type));
    }

    @Override
    public void delete(Long id) {
        if (!sampleTypeRepository.existsById(id)) {
            throw new RuntimeException("Not found");
        }
        sampleTypeRepository.deleteById(id);
    }

    private SampleTypeDTO toDTO(SampleType entity) {
        return new SampleTypeDTO(entity.getId(), entity.getSampleType());
    }
}
