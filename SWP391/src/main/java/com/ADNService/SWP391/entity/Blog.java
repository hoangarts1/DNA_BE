package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String title;

    @Lob
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String contentHtml;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String titleImageBase64;

    private String blogType;

    private LocalDate blogDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isActive = true;

    public Blog() {}

    public Blog(Long id, String title, String contentHtml, String titleImageBase64, String blogType, LocalDate blogDate, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isActive) {
        this.id = id;
        this.title = title;
        this.contentHtml = contentHtml;
        this.titleImageBase64 = titleImageBase64;
        this.blogType = blogType;
        this.blogDate = blogDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getTitleImageBase64() {
        return titleImageBase64;
    }

    public void setTitleImageBase64(String titleImageBase64) {
        this.titleImageBase64 = titleImageBase64;
    }

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
    }

    public LocalDate getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(LocalDate blogDate) {
        this.blogDate = blogDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
