package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.RatingFeedbackDTO;

import java.util.List;

public interface RatingFeedbackService {
    RatingFeedbackDTO create(RatingFeedbackDTO dto);
    RatingFeedbackDTO getById(Long id);
    List<RatingFeedbackDTO> getAll();
    RatingFeedbackDTO update(Long id, RatingFeedbackDTO dto);
    void delete(Long id);
    RatingFeedbackDTO getByOrderId(Long orderId); // Thêm phương thức mới
}
