package com.ssafy.stats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiService {

    @Value("${gms.api.key}")
    private String apiKey;

    private final String GMS_API_URL = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS) // GPT 분석 시간을 고려해 조금 더 넉넉하게 설정
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build();

    /**
    gpt가 외부 시장 가격 알아올 거에요
     */
    public String getExternalMarketPrice(String menu) {
        String prompt = String.format(
            "사용자가 '%s'의 가격을 궁금해해. 대한민국 내 일반적인 대형마트나 온라인 쇼핑몰의 평균 가격(단위/용량 포함)을 조사해서 알려줘.\n" +
            "답변 형식은 반드시 아래 형식을 지켜줘.\n" +
            "[가격: 0,000원], [팁: 구매 시 유용한 정보 한 문장]\n" +
            "예시: [가격: 5,500원], [팁: 리필용 제품을 묶음으로 구매하는 것이 훨씬 경제적입니다.]", menu);

        return callGpt(prompt, "너는 한국의 온/오프라인 물가 전문가야. 아주 정확한 가격 정보를 제공해.");
    }

    /**
     * 공통 GPT 호출 메서드
     */
    private String callGpt(String prompt, String systemRole) {
        try {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("model", "gpt-4o-mini");

            List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", systemRole),
                Map.of("role", "user", "content", prompt)
            );

            requestMap.put("messages", messages);
            requestMap.put("temperature", 0.7);

            String jsonBody = objectMapper.writeValueAsString(requestMap);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(GMS_API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey.trim())
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "[가격: 정보 없음], [팁: 현재 시장 데이터를 가져올 수 없습니다.]";
                }

                String responseBody = response.body().string();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                List choices = (List) responseMap.get("choices");
                Map firstChoice = (Map) choices.get(0);
                Map message = (Map) firstChoice.get("message");
                return ((String) message.get("content")).trim();
            }
        } catch (IOException e) {
            return "[가격: 에러], [팁: 네트워크 연결 상태를 확인해주세요.]";
        }
    }

    /**
     * AI에게 사용자 입력과 가장 유사한 DB 메뉴명을 매칭하게 하는 메서드
     */
    public String findBestMatch(String userConfigMenu, List<String> dbMenus) {
        try {
            // AI에게 페르소나와 강한 규칙 부여
            String systemRole = "너는 마트 상품명 매칭 전문가야. 규칙을 반드시 지켜.\n" 
                    + "1. 입력된 단어(예: 퐁퐁)의 '용도'를 파악해라.\n"
                    + "2. 제공된 리스트에서 용도가 가장 비슷한 상품명을 딱 하나만 골라라.\n" 
                    + "3. '퐁퐁'이나 '트리오'는 리스트에 '주방세제'가 있다면 그것을 골라라.\n"
                    + "4. 리스트에 있는 이름 그대로 대답하고, 부연설명은 절대 하지 마라.\n" 
                    + "5. 절대 'NONE'이라고 하지 말고 리스트에서 가장 확률 높은 것을 골라라.";

            String userPrompt = "상품 리스트: " + dbMenus.toString() + "\n사용자 입력: " + userConfigMenu;

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("model", "gpt-4o-mini");
            requestMap.put("messages", List.of(
                Map.of("role", "system", "content", systemRole),
                Map.of("role", "user", "content", userPrompt)
            ));
            requestMap.put("temperature", 0.5);

            String jsonBody = objectMapper.writeValueAsString(requestMap);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(GMS_API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey.trim())
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) return "ERROR";

                String responseBody = response.body().string();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                List choices = (List) responseMap.get("choices");
                Map firstChoice = (Map) choices.get(0);
                Map message = (Map) firstChoice.get("message");
                String content = ((String) message.get("content")).trim();

                // 따옴표나 마침표 제거 후 순수 텍스트만 반환
                return content.replaceAll("['\"\\.]", "");
            }
        } catch (IOException e) {
            return "ERROR";
        }
    }
}