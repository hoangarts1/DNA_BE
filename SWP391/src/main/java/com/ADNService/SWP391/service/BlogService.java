package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.BlogDTO;

import java.util.List;

import java.util.List;

public interface BlogService {
    BlogDTO create(BlogDTO dto);
    BlogDTO update(Long id, BlogDTO dto);
    void delete(Long id);
    BlogDTO getById(Long id);
    List<BlogDTO> getAll();
    BlogDTO toggleActive(Long id);
    List<BlogDTO> getBlogsByType(String type);
}