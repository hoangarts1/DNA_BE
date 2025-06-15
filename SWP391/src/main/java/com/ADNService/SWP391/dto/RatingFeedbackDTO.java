package com.ADNService.SWP391.dto;

import java.time.LocalDate;

public class RatingFeedbackDTO {
    private Long ratingFeedbackId;
    private Long customerId;
    private int rating;
    private String comment;
    private LocalDate date;

    public RatingFeedbackDTO() {
    }

    public RatingFeedbackDTO(String comment, Long customerId, LocalDate date, int rating, Long ratingFeedbackId) {
        this.comment = comment;
        this.customerId = customerId;
        this.date = date;
        this.rating = rating;
        this.ratingFeedbackId = ratingFeedbackId;
    }

    public Long getRatingFeedbackId() {
        return ratingFeedbackId;
    }

    public void setRatingFeedbackId(Long ratingFeedbackId) {
        this.ratingFeedbackId = ratingFeedbackId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
