package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestResult;
import com.ADNService.SWP391.entity.TestResultSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByTestOrder_OrderId(Long orderId);

}
