package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    private String blogName;

    private String urlBlog;

    @Temporal(TemporalType.DATE)
    private Date blogDate;

    public Blog() {
    }

    public Blog(String blogName, String urlBlog, Date blogDate) {
        this.blogName = blogName;
        this.urlBlog = urlBlog;
        this.blogDate = blogDate;
    }

    public Date getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(Date blogDate) {
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

    public String getUrlBlog() {
        return urlBlog;
    }

    public void setUrlBlog(String urlBlog) {
        this.urlBlog = urlBlog;
    }
}
