package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.SampleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleTypeRepository extends JpaRepository<SampleType, Long> {
    boolean existsBySampleType(String sampleType);
}
