package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.BlogDTO;
import com.ADNService.SWP391.entity.Blog;
import com.ADNService.SWP391.repository.BlogRepository;
import com.ADNService.SWP391.service.BlogService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    private BlogDTO toDTO(Blog blog) {
        return BlogDTO.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .contentHtml(blog.getContentHtml())
                .titleImageBase64(blog.getTitleImageBase64())
                .blogType(blog.getBlogType())
                .blogDate(blog.getBlogDate().toString())
                .isActive(blog.isActive()) // mới thêm
                .build();
    }


    @Override
    public BlogDTO create(BlogDTO dto) {
        Blog blog = Blog.builder()
                .title(dto.getTitle())
                .contentHtml(dto.getContentHtml())
                .titleImageBase64(dto.getTitleImageBase64())
                .blogType(dto.getBlogType())
                .blogDate(LocalDate.parse(dto.getBlogDate()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        return toDTO(blogRepository.save(blog));
    }

    @Override
    public BlogDTO update(Long id, BlogDTO dto) {
        Blog blog = blogRepository.findById(id).orElseThrow();
        blog.setTitle(dto.getTitle());
        blog.setContentHtml(dto.getContentHtml());
        blog.setTitleImageBase64(dto.getTitleImageBase64());
        blog.setUpdatedAt(LocalDateTime.now());
        blog.setBlogType(dto.getBlogType());
        blog.setBlogDate(LocalDate.parse(dto.getBlogDate()));
        return toDTO(blogRepository.save(blog));
    }

    @Override
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public BlogDTO getById(Long id) {
        return blogRepository.findById(id).map(this::toDTO).orElseThrow();
    }

    @Override
    public List<BlogDTO> getAll() {
        return blogRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BlogDTO> getBlogsByType(String type) {
        return blogRepository.findByBlogType(type).stream()
                .filter(Blog::isActive)
                .map(this::toDTO) // ✅ dùng toDTO() trong chính class này
                .collect(Collectors.toList());
    }

    @Override
    public BlogDTO toggleActive(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow();
        blog.setActive(!blog.isActive()); // Đảo ngược trạng thái
        blog.setUpdatedAt(LocalDateTime.now());
        blog = blogRepository.save(blog); // lưu và gán lại
        return toDTO(blog); // ✅ TRẢ VỀ BlogDTO mới nhất
    }


}