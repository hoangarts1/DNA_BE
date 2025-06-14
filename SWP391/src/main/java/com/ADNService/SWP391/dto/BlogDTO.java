package com.ADNService.SWP391.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BlogDTO {
    private Long blogId;
    private String blogName;
    private String urlBlog;
    private Date blogDate;
}
