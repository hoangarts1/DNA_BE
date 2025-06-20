package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestResultSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestResultSampleRepository extends JpaRepository<TestResultSample, Long> {
    List<TestResultSample> findByTestSampleId(Long testSampleId);
    Optional<TestResultSample> findByTestSampleIdAndLocusName(Long testSampleId, String locusName);
    List<TestResultSample> findByTestSample_Id(Long testSampleId);

}
