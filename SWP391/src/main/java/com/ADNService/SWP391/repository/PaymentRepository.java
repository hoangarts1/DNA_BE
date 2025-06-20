package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCustomer_Id(Long customerId);
    Optional<Payment> findByTransactionId(String transactionId);
}
