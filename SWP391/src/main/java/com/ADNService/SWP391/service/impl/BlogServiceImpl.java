package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.BlogDTO;
import com.ADNService.SWP391.entity.Blog;
import com.ADNService.SWP391.repository.BlogRepository;
import com.ADNService.SWP391.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

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
        blog.setUrlBlog(dto.getUrlBlog());
        blog.setBlogDate(dto.getBlogDate());

        return convertToDTO(blogRepository.save(blog));
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO dto) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new RuntimeException("Blog with ID " + id + " does not exist.");
        }

        Blog blog = optionalBlog.get();
        blog.setBlogName(dto.getBlogName());
        blog.setUrlBlog(dto.getUrlBlog());
        blog.setBlogDate(dto.getBlogDate());

        return convertToDTO(blogRepository.save(blog));
    }

    @Override
    public void deleteBlog(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new RuntimeException("Blog with ID " + id + " does not exist.");
        }
        blogRepository.deleteById(id);
    }

    private BlogDTO convertToDTO(Blog blog) {
        BlogDTO dto = new BlogDTO();
        dto.setBlogId(blog.getBlogId());
        dto.setBlogName(blog.getBlogName());
        dto.setUrlBlog(blog.getUrlBlog());
        dto.setBlogDate(blog.getBlogDate());
        return dto;
    }
}
