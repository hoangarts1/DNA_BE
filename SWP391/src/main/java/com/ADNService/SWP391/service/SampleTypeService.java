package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.SampleTypeDTO;

import java.util.List;

public interface SampleTypeService {
    SampleTypeDTO create(SampleTypeDTO dto);
    List<SampleTypeDTO> getAll();
    SampleTypeDTO getById(Long id);
    SampleTypeDTO update(Long id, SampleTypeDTO dto);
    void delete(Long id);
}
