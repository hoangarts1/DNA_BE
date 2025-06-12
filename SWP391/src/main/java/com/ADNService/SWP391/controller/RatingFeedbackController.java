package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.RatingFeedbackDTO;
import com.ADNService.SWP391.service.RatingFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating-feedbacks")
public class RatingFeedbackController {

    @Autowired
    private RatingFeedbackService ratingFeedbackService;

    @PostMapping
    public ResponseEntity<RatingFeedbackDTO> create(@RequestBody RatingFeedbackDTO dto) {
        return ResponseEntity.ok(ratingFeedbackService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingFeedbackDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingFeedbackService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RatingFeedbackDTO>> getAll() {
        return ResponseEntity.ok(ratingFeedbackService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RatingFeedbackDTO> update(@PathVariable Long id, @RequestBody RatingFeedbackDTO dto) {
        return ResponseEntity.ok(ratingFeedbackService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ratingFeedbackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
