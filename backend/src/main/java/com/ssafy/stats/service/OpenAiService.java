package com.ssafy.stats.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Service
public class OpenAiService {

    @Value("${gms.api.key}")
    private String apiKey;

    private final String API_URL = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions";
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 프롬프트 -> gpt에게 보내버리고
     */
    public String getGptResponse(String systemRole, String userPrompt, double temperature) {
        ChatRequest body = new ChatRequest(
            "gpt-4o-mini",
            List.of(
                new Message("system", systemRole),
                new Message("user", userPrompt)
            ),
            temperature
        );

        try {
            String requestJson = objectMapper.writeValueAsString(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey.trim());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("choices").path(0).path("message").path("content").asText("").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    // 내부 DTO 클래스
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
        private Double temperature;
    }
}