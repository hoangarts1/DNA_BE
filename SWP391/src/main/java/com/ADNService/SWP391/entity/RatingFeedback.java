package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class RatingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingFeedbackId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private TestOrder testOrder;

    @Column(nullable = false)
    private int rating;  // must be 1-5

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String comment;

    private LocalDate date;

    public RatingFeedback() {
    }

    public RatingFeedback(String comment, TestOrder testOrder, LocalDate date, int rating, Long ratingFeedbackId) {
        this.comment = comment;
        this.testOrder = testOrder;
        this.date = date;
        this.rating = rating;
        this.ratingFeedbackId = ratingFeedbackId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TestOrder getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(TestOrder testOrder) {
        this.testOrder = testOrder;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getRatingFeedbackId() {
        return ratingFeedbackId;
    }

    public void setRatingFeedbackId(Long ratingFeedbackId) {
        this.ratingFeedbackId = ratingFeedbackId;
    }
}
