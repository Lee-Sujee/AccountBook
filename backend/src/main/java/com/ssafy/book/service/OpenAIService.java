package com.ssafy.book.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {
	@Value("${openai.api.key}")
    private String apiKey;
	
	
	public String analyzeFinances(int income, int expense) {
		String prompt = String.format("사용자의 수입은 %d원이고 지출은 %d원입니다. 이 데이터를 분석하여 경제적으로 전문적인 분석을 바탕으로 친절한 조언 제공해주세요. 단, 400자 내외로 500자를 넘지 않도록 대답해주고, 중간중간 줄바꿈을 통해 보기 편하게 작성해주세요", income, expense);
		return getGPTResponse(prompt);
	}
	
	private String getGPTResponse(String prompt) {
		String url = "https://gms.ssafy.io/gmsapi/api.openai.com/v1/chat/completions";
		
		String requestBody = String.format(
			    "{\"model\":\"gpt-4o-mini\",\"messages\":[" +
			    "{\"role\":\"system\",\"content\":\"You are a helpful and friendly financial assistant.\"}," +
			    "{\"role\":\"user\",\"content\":\"수입이 500,000원이고 지출이 200,000원입니다. 이 데이터를 분석하여 경제적으로 전문적인 분석을 바탕으로 친절한 조언 제공해주세요.\"}," +
			    "{\"role\":\"assistant\",\"content\":\"수입 대비 지출이 40%%로 적당하지만, 더 나은 재정 관리를 위해 지출을 더 줄이거나 저축을 늘리는 것이 좋습니다. 또한 사람들의 평균 임금은 2000000 ~ 2500000 사이 정도 입니다. 수입을 더 늘릴 수 있는 방안을 찾아보시길 추천드려요!\"}," +
			    "{\"role\":\"user\",\"content\":\"수입이 1,000,000원이고 지출이 700,000원입니다. 어떻게 분석할 수 있나요?\"}," +
			    "{\"role\":\"assistant\",\"content\":\"수입 대비 지출 비율이 70%%로 높은 수준입니다. 지출을 줄이고, 여유 자금을 투자하거나 저축하는 것이 중요합니다. 혹시 지금 저축하고자하는 목표가 있나요? 챌린지 기능을 통해 저축에 도전해보세요! 장볼때는 평균계산기를 통해 작은 지출부터 아끼시는 것도 좋은 방법입니다!\"}," +
			    "{\"role\":\"user\",\"content\":\"%s\"}] ,\\\"max_tokens\\\":150}",
			    prompt);
		
		// POST 요청 보내기!
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.set("Content-Type", "application/json");
		
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        
        try {
        	
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답 상태 코드가 2xx(성공적)일 경우
            if (response.getStatusCode() == HttpStatus.OK) {
            	ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());  // JSON 응답 파싱
                String content = rootNode.path("choices").get(0).path("message").path("content").asText();  // 메시지 내용 추출

                return content;
            } else {
                throw new RuntimeException("API 요청 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 처리: 오류가 발생하면 예외 출력
            e.printStackTrace();
            throw new RuntimeException("API 요청 중 오류 발생: " + e.getMessage());
        }
	}
}
