package com.ADNService.SWP391.dto;

import java.util.Date;
import java.util.List;

public class BlogDTO {
    private Long blogId;
    private String blogName;
    private String content;
    private String imgDetailBase64;
    private List<String> imagesBase64;
    private String blogType;
    private Date blogDate;

    public BlogDTO() {
    }

    public BlogDTO(Long blogId, String blogName, String content, String imgDetailBase64, List<String> imagesBase64, String blogType, Date blogDate) {
        this.blogId = blogId;
        this.blogName = blogName;
        this.content = content;
        this.imgDetailBase64 = imgDetailBase64;
        this.imagesBase64 = imagesBase64;
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

    public String getImgDetailBase64() {
        return imgDetailBase64;
    }

    public void setImgDetailBase64(String imgDetailBase64) {
        this.imgDetailBase64 = imgDetailBase64;
    }

    public List<String> getImagesBase64() {
        return imagesBase64;
    }

    public void setImagesBase64(List<String> imagesBase64) {
        this.imagesBase64 = imagesBase64;
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