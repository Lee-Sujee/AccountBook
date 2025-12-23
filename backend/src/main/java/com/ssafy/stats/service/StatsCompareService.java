package com.ssafy.stats.service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssafy.stats.entity.Stats;
import com.ssafy.stats.repository.StatsRepository;

@Service
public class StatsCompareService {

    @Autowired
    private StatsRepository repository;

    @Autowired
    private OpenAiService openAiService;

    public Map<String, Object> getCombinedResult(String menu, String category, int userPrice) {
        // 1. db 조회 -> 데이터 찾기
        List<Stats> targetList = repository.findAllByMenuLike(menu);
        if (targetList.isEmpty()) {
            List<String> allMenus = repository.findAllDistinctMenus();
            
            // 프롬프트
            String systemRole = "너는 마트 상품명 매칭 전문가야. 규칙을 반드시 지켜.\n" 
                    + "1. 입력된 단어(예: 퐁퐁)의 '용도'를 파악해라.\n"
                    + "2. 제공된 리스트에서 용도가 가장 비슷한 상품명을 딱 하나만 골라라.\n" 
                    + "3. '퐁퐁'이나 '트리오'는 리스트에 '주방세제'가 있다면 그것을 골라라.\n"
                    + "4. 리스트에 있는 이름 그대로 대답하고, 부연설명은 절대 하지 마라.\n" 
                    + "5. 절대 'NONE'이라고 하지 말고 리스트에서 가장 확률 높은 것을 골라라.";
            String userPrompt = "상품 리스트: " + allMenus.toString() + "\n사용자 입력: " + menu;

            String aiMatchedMenu = openAiService.getGptResponse(systemRole, userPrompt, 0.5);
            
            if (!"NONE".equals(aiMatchedMenu) && !"ERROR".equals(aiMatchedMenu)) {
                targetList = repository.findAllByMenuLike(aiMatchedMenu.replaceAll("['\"\\.]", ""));
            }
        }

        // DB 결과
        Map<String, Stats> uniqueMap = new LinkedHashMap<>(); 
        for (Stats s : targetList) {
            String key = s.getMenu() + s.getCategory() + s.getSurveyDate();
            if (!uniqueMap.containsKey(key)) uniqueMap.put(key, s);
        }
        List<Map<String, Object>> dbResults = uniqueMap.values().stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("menu", s.getMenu());
            map.put("category", s.getCategory());
            map.put("averagePrice", s.getPrice());
            map.put("surveyDate", s.getSurveyDate());
            int avg = s.getPrice();
            String resultMsg = (userPrice > avg * 1.1) ? "비싼 소비예요 😥" : 
                               (userPrice < avg * 0.9) ? "아주 합리적인 소비예요 👍" : "적정한 소비예요 🙂";
            map.put("result", resultMsg);
            return map;
        }).collect(Collectors.toList());

        // ChatGPT -> 외부 시장 평균가 가져와!
        String marketSystemRole = "너는 한국의 온/오프라인 물가 전문가야. 아주 정확한 가격 정보를 제공해.";
        String marketPrompt = String.format(
            "사용자가 '%s'의 가격을 궁금해해. 대한민국 내 일반적인 대형마트나 온라인 쇼핑몰의 평균 가격(단위/용량 포함)을 조사해서 알려줘.\n" +
            "답변 형식은 반드시 아래 형식을 지켜줘.\n" +
            "[가격: 0,000원], [팁: 구매 시 유용한 정보 한 문장]\n" +
            "예시: [가격: 5,500원], [팁: 리필용 제품을 묶음으로 구매하는 것이 훨씬 경제적입니다.]", menu);

        String gptAdvice = openAiService.getGptResponse(marketSystemRole, marketPrompt, 0.7);

        // 결과 내놔!!!
        Map<String, Object> response = new HashMap<>();
        response.put("dbResults", dbResults);
        response.put("gptAdvice", gptAdvice);
        return response;
    }

    public List<Stats> searchLatestByKeyword(String keyword) {
        String trimmed = (keyword == null) ? "" : keyword.trim();
        return trimmed.isEmpty() ? new ArrayList<>() : repository.findAllByMenuLike(trimmed);
    }
}