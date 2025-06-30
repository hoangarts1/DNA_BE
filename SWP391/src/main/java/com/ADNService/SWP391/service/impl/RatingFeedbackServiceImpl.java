package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.RatingFeedbackDTO;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.RatingFeedback;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.repository.RatingFeedbackRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.RatingFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingFeedbackServiceImpl implements RatingFeedbackService {

    @Autowired
    private RatingFeedbackRepository ratingFeedbackRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;


    @Override
    public RatingFeedbackDTO create(RatingFeedbackDTO dto) {
        TestOrder order = testOrderRepository.findById(dto.getTestOrderId())
                .orElseThrow(() -> new RuntimeException("TestOrder not found"));

        RatingFeedback feedback = new RatingFeedback();
        feedback.setTestOrder(order);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setDate(dto.getDate());

        return convertToDTO(ratingFeedbackRepository.save(feedback));
    }


    @Override
    public RatingFeedbackDTO getById(Long id) {
        return ratingFeedbackRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Rating feedback not found"));
    }

    @Override
    public List<RatingFeedbackDTO> getAll() {
        return ratingFeedbackRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RatingFeedbackDTO update(Long id, RatingFeedbackDTO dto) {
        RatingFeedback feedback = ratingFeedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating feedback not found"));

        TestOrder order = testOrderRepository.findById(dto.getTestOrderId())
                .orElseThrow(() -> new RuntimeException("TestOrder not found"));

        feedback.setTestOrder(order);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setDate(dto.getDate());

        return convertToDTO(ratingFeedbackRepository.save(feedback));
    }

    @Override
    public void delete(Long id) {
        ratingFeedbackRepository.deleteById(id);
    }

    private RatingFeedbackDTO convertToDTO(RatingFeedback feedback) {
        RatingFeedbackDTO dto = new RatingFeedbackDTO();
        dto.setRatingFeedbackId(feedback.getRatingFeedbackId());
        dto.setTestOrderId(feedback.getTestOrder().getOrderId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setDate(feedback.getDate());
        return dto;
    }
}
