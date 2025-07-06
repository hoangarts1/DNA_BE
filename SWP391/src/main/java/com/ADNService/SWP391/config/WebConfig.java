package com.ADNService.SWP391.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // hoặc cụ thể: "/api/gemini"
                .allowedOrigins("http://localhost:3000") // frontend URL
                .allowedMethods("*") // GET, POST, etc.
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
