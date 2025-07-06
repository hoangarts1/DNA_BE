package com.ADNService.SWP391.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/gemini")
public class GeminiChatController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String prompt = request.get("message");

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(
                Map.of("parts", List.of(Map.of("text", prompt)))
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-goog-api-key", geminiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // Parse phản hồi
        List<Map> candidates = (List<Map>) response.getBody().get("candidates");
        Map content = (Map) ((Map) candidates.get(0)).get("content");
        String text = (String) ((Map) ((List<?>) content.get("parts")).get(0)).get("text");

        return ResponseEntity.ok(text);
    }
}
