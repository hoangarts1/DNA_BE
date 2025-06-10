package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
