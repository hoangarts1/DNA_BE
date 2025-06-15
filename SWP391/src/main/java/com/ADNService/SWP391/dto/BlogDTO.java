package com.ADNService.SWP391.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class BlogDTO {
    private Long blogId;
    private String blogName;
    private String urlBlog;
    private Date blogDate;

    public BlogDTO() {
    }

    public BlogDTO(Date blogDate, Long blogId, String blogName, String urlBlog) {
        this.blogDate = blogDate;
        this.blogId = blogId;
        this.blogName = blogName;
        this.urlBlog = urlBlog;
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
