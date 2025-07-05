package com.ADNService.SWP391.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final WebClient webClient;

    private final String OPENAI_API_KEY = "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";

    public ChatController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(Map.of("role", "user", "content", userMessage)));

        String response = webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, OPENAI_API_KEY)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(response);
    }
}

