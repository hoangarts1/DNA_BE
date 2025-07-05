package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.BlogDTO;
import com.ADNService.SWP391.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Để React gọi API
public class BlogController {

    private final BlogService blogService;

    @PutMapping("/{id}/toggle-active")
    public BlogDTO toggleActive(@PathVariable Long id) {
        blogService.toggleActive(id);
        return blogService.getById(id);
    }

    @GetMapping
    public List<BlogDTO> getAll() {
        return blogService.getAll();
    }

    @GetMapping("/{id}")
    public BlogDTO getById(@PathVariable Long id) {
        return blogService.getById(id);
    }

    @PostMapping
    public BlogDTO create(@RequestBody BlogDTO dto) {
        return blogService.create(dto);
    }

    @PutMapping("/{id}")
    public BlogDTO update(@PathVariable Long id, @RequestBody BlogDTO dto) {
        return blogService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        blogService.delete(id);
    }
}