package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    Optional<Services> findByServiceType(String serviceType);
}
