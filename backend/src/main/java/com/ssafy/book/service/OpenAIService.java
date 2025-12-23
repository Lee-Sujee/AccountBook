package com.ssafy.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeFinances(String prompt) {
        return getGPTResponse(prompt);
    }

    private String getGPTResponse(String prompt) {
        String url = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions";

        ChatRequest body = new ChatRequest(
                "gpt-4o-mini",
                List.of(
                        new Message("system", "You are a helpful and friendly financial assistant."),
                        new Message("user", prompt)
                ),
                300 // 글자 제한하려면 토큰 조금 넉넉히
        );

        try {
            String requestJson = objectMapper.writeValueAsString(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("API 요청 실패: " + response.getStatusCode());
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode choices0 = root.path("choices").path(0);
            String content = choices0.path("message").path("content").asText("");

            if (content.isBlank()) {
                throw new RuntimeException("API 응답 파싱 실패(빈 content)");
            }

            return content;

        } catch (Exception e) {
            throw new RuntimeException("API 요청 중 오류: " + e.getMessage(), e);
        }
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    static class Message {
        private String role;
        private String content;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ChatRequest {
        private String model;
        private List<Message> messages;
        private Integer max_tokens;
    }
}
