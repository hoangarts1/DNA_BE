package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services, Long> {
    List<Services> findByServiceType(String serviceType);

}
