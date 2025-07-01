package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.BlogDTO;
import com.ADNService.SWP391.entity.Blog;
import com.ADNService.SWP391.repository.BlogRepository;
import com.ADNService.SWP391.service.BlogService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public BlogDTO getBlogById(Long id) {
        return blogRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public BlogDTO createBlog(BlogDTO dto) {
        Blog blog = new Blog();
        blog.setBlogName(dto.getBlogName());
        blog.setContent(dto.getContent());
        blog.setImgDetail(dto.getImgDetailBase64());
        blog.setBlogType(dto.getBlogType());
        blog.setBlogDate(dto.getBlogDate());
        try {
            if (dto.getImagesBase64() != null && !dto.getImagesBase64().isEmpty()) {
                blog.setImages(objectMapper.writeValueAsString(dto.getImagesBase64()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu trữ danh sách ảnh: " + e.getMessage());
        }

        return convertToDTO(blogRepository.save(blog));
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO dto) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new RuntimeException("Blog với ID " + id + " không tồn tại.");
        }

        Blog blog = optionalBlog.get();
        blog.setBlogName(dto.getBlogName());
        blog.setContent(dto.getContent());
        blog.setImgDetail(dto.getImgDetailBase64());
        blog.setBlogType(dto.getBlogType());
        blog.setBlogDate(dto.getBlogDate());
        try {
            if (dto.getImagesBase64() != null && !dto.getImagesBase64().isEmpty()) {
                blog.setImages(objectMapper.writeValueAsString(dto.getImagesBase64()));
            } else {
                blog.setImages(null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu trữ danh sách ảnh: " + e.getMessage());
        }

        return convertToDTO(blogRepository.save(blog));
    }

    @Override
    public void deleteBlog(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new RuntimeException("Blog với ID " + id + " không tồn tại.");
        }
        blogRepository.deleteById(id);
    }

    @Override
    public List<BlogDTO> getBlogsByType(String blogType) {
        return blogRepository.findByBlogType(blogType).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BlogDTO convertToDTO(Blog blog) {
        BlogDTO dto = new BlogDTO();
        dto.setBlogId(blog.getBlogId());
        dto.setBlogName(blog.getBlogName());
        dto.setContent(blog.getContent());
        dto.setImgDetailBase64(blog.getImgDetail());
        dto.setBlogType(blog.getBlogType());
        dto.setBlogDate(blog.getBlogDate());
        try {
            if (blog.getImages() != null && !blog.getImages().isEmpty()) {
                dto.setImagesBase64(objectMapper.readValue(blog.getImages(), new TypeReference<List<String>>() {}));
            } else {
                dto.setImagesBase64(new ArrayList<>());
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc danh sách ảnh: " + e.getMessage());
        }
        return dto;
    }
}