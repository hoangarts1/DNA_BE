package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByAccountId(Long accountId);
}
