package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestResult;
import com.ADNService.SWP391.entity.TestResultSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestOrder_OrderId(Long orderId);

    @Query("SELECT r FROM TestResult r WHERE " +
            "(r.sampleId1.id = :sampleId1 AND r.sampleId2.id = :sampleId2) OR " +
            "(r.sampleId1.id = :sampleId2 AND r.sampleId2.id = :sampleId1)")
    List<TestResult> findBySampleIdPair(@Param("sampleId1") Long sampleId1, @Param("sampleId2") Long sampleId2);

}
