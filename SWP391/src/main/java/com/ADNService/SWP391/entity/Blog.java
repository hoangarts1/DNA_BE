package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    private String blogName;

    private String urlBlog;

    @Temporal(TemporalType.DATE)
    private Date blogDate;
}
