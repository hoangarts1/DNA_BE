package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByAccountId(Long accountId);

}
