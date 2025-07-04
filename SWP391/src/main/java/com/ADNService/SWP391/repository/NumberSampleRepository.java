package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.NumberSample;
import com.ADNService.SWP391.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NumberSampleRepository extends JpaRepository<NumberSample, Long> {
    Optional<NumberSample> findByServiceType(String serviceType);

}
