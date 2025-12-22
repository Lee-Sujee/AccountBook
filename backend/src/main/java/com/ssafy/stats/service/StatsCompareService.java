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

    /**
     * ✅ DB 데이터 + AI 매칭 + GPT 시장 조언을 모두 합친 메인 로직
     */
    public Map<String, Object> getCombinedResult(String menu, String category, int userPrice) {
        // 1. DB 데이터 조회 (일단 전체 업태 조회)
        List<Stats> targetList = repository.findAllByMenuLike(menu);

        // 2. 검색 결과가 없으면 AI 매칭 시도
        if (targetList.isEmpty()) {
            List<String> allMenus = repository.findAllDistinctMenus();
            String aiMatchedMenu = openAiService.findBestMatch(menu, allMenus);

            if (!"NONE".equals(aiMatchedMenu) && !"ERROR".equals(aiMatchedMenu)) {
                targetList = repository.findAllByMenuLike(aiMatchedMenu);
            }
        }

        // 3. 중복 제거 (상품명 + 업태 + 조사일 기준)
        Map<String, Stats> uniqueMap = new LinkedHashMap<>(); 
        for (Stats s : targetList) {
            String key = s.getMenu() + s.getCategory() + s.getSurveyDate();
            if (!uniqueMap.containsKey(key)) {
                uniqueMap.put(key, s);
            }
        }

        // 4. 프론트용 리스트 변환
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

        // 5. ChatGPT에게 외부 시장 정보 물어보기
        String gptAdvice = openAiService.getExternalMarketPrice(menu);

        // 6. 결과 묶어서 반환
        Map<String, Object> response = new HashMap<>();
        response.put("dbResults", dbResults);
        response.put("gptAdvice", gptAdvice);

        return response;
    }

    /**
     * ✅ 컨트롤러에서 호출하는 키워드 검색 메서드
     * (이 부분이 없거나 이름이 다르면 에러가 납니다!)
     */
    public List<Stats> searchLatestByKeyword(String keyword) {
        String trimmed = (keyword == null) ? "" : keyword.trim();
        if (trimmed.isEmpty()) {
            return new ArrayList<>();
        }
        // Repository에 있는 메서드를 호출하여 리스트 반환
        return repository.findAllByMenuLike(trimmed);
    }
}