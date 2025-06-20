package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestSampleRepository extends JpaRepository<TestSample, Long> {
    List<TestSample> findByOrder_OrderId(Long orderId);
//    List<TestSample> findByTestOrder_OrderId(Long orderId);

    List<TestSample> getTestSamplesByOrder_OrderId(Long attr0);
}
