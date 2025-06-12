package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class RatingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingFeedbackId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private int rating;  // must be 1-5

    private String comment;

    private LocalDate date;

    public RatingFeedback() {
    }

    public RatingFeedback(String comment, Customer customer, LocalDate date, int rating, Long ratingFeedbackId) {
        this.comment = comment;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
