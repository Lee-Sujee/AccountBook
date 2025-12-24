package com.ssafy.common.ai;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class OpenAIGateway {

    @Value("${gms.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    private final String url = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** book 등 기존 코드 호환용(temperature 기본값) */
    public String chat(String systemPrompt, String userPrompt, Integer maxTokens) {
        return chat(systemPrompt, userPrompt, maxTokens, 0.2); // 기본값 적당히
    }

    /** stats에서 쓰는 temperature 지원 버전 */
    public String chat(String systemPrompt, String userPrompt, Integer maxTokens, Double temperature) {
        ChatRequest body = new ChatRequest(
                model,
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userPrompt)
                ),
                maxTokens,
                temperature
        );

        try {
            String requestJson = objectMapper.writeValueAsString(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey.trim());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("OpenAI 요청 실패: " + response.getStatusCode());
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").path(0).path("message").path("content").asText("").trim();

            if (content.isBlank()) {
                throw new RuntimeException("OpenAI 응답 파싱 실패(빈 content)");
            }
            return content;

        } catch (Exception e) {
            throw new RuntimeException("OpenAI 요청 중 오류: " + e.getMessage(), e);
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
        private Double temperature;
    }
}
