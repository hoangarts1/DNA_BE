package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.dto.TestResultSampleDTO;
import com.ADNService.SWP391.entity.TestResultSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestResultSampleRepository extends JpaRepository<TestResultSample, Long> {
    Optional<TestResultSample> findByid(Long id);
    @Query("SELECT trs FROM TestResultSample trs WHERE trs.testSample.id = :testSampleId")
    List<TestResultSample> getTestResultSampleIdByTestSampleId(@Param("testSampleId") String testSampleId);

    @Query("SELECT trs FROM TestResultSample trs " +
            "JOIN trs.testSample ts " +
            "JOIN ts.order o " +
            "WHERE o.orderId = :orderId")
    List<TestResultSample> getTestResultSamplesByOrderId(@Param("orderId") String orderId);

}
