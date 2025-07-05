package com.ADNService.SWP391.dto;

import java.time.LocalDate;

public class RatingFeedbackDTO {
    private Long ratingFeedbackId;
    private Long testOrderId;
    private int rating;
    private String comment;
    private LocalDate date;

    public RatingFeedbackDTO() {
    }

    public RatingFeedbackDTO(String comment, Long testOrderId, LocalDate date, int rating, Long ratingFeedbackId) {
        this.comment = comment;
        this.testOrderId = testOrderId;
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

    public Long getTestOrderId() {
        return testOrderId;
    }

    public void setTestOrderId(Long testOrderId) {
        this.testOrderId = testOrderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}