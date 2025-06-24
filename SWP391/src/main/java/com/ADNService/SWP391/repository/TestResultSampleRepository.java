package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestResultSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestResultSampleRepository extends JpaRepository<TestResultSample, Long> {
    List<TestResultSample> findByTestSampleId(Long testSampleId);
    Optional<TestResultSample> findByTestSampleIdAndLocusName(Long testSampleId, String locusName);
    List<TestResultSample> findByTestSample_Id(Long testSampleId);

    @Query("SELECT trs FROM TestResultSample trs WHERE trs.testSample.order.orderId = :orderId")
    List<TestResultSample> findByOrderId(Long orderId);
}