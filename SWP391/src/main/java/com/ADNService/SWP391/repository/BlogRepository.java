package com.ADNService.SWP391.repository;

import com.ADNService.SWP391.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByBlogType(String blogType);
}