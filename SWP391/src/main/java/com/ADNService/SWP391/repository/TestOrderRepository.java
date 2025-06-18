package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.TestOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestOrderRepository extends JpaRepository<TestOrder, Long> {
    List<TestOrder> findByCustomerId(Long customerId);
}
