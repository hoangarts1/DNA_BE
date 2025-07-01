package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String blogName;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String imgDetail; // Lưu ảnh tiêu đề dưới dạng base64

    @Column(columnDefinition = "TEXT")
    private String images; // Lưu danh sách base64 của ảnh nội dung dưới dạng JSON

    @Column(columnDefinition = "NVARCHAR(50)")
    private String blogType;

    @Temporal(TemporalType.DATE)
    private Date blogDate;

    public Blog() {
    }

    public Blog(String blogName, String content, String imgDetail, String images, String blogType, Date blogDate) {
        this.blogName = blogName;
        this.content = content;
        this.imgDetail = imgDetail;
        this.images = images;
        this.blogType = blogType;
        this.blogDate = blogDate;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgDetail() {
        return imgDetail;
    }

    public void setImgDetail(String imgDetail) {
        this.imgDetail = imgDetail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBlogType() {
        return blogType;
    }

    public void setBlogType(String blogType) {
        this.blogType = blogType;
    }

    public Date getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(Date blogDate) {
        this.blogDate = blogDate;
    }
}