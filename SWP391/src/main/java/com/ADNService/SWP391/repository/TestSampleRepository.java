package com.ADNService.SWP391.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.ADNService.SWP391.entity.TestSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestSampleRepository extends JpaRepository<TestSample, Long> {
    @Query("SELECT ts FROM TestSample ts WHERE ts.order.orderId = :orderId")
    List<TestSample> getTestSamplesByOrderId(@Param("orderId") String orderId);

}
