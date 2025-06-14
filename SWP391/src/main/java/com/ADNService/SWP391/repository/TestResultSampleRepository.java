package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestResultSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestResultSampleRepository extends JpaRepository<TestResultSample, Long> {
    Optional<TestResultSample> findByid(Long id);
}
