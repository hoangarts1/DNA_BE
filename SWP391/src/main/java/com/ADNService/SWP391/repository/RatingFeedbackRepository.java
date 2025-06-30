package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.RatingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingFeedbackRepository extends JpaRepository<RatingFeedback, Long> {
    Optional<RatingFeedback> findByTestOrderOrderId(Long orderId);
}
