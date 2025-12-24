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

        String inputMenu = normalizeInput(menu);

        // 1) DB 조회
        List<Stats> targetList = repository.findAllByMenuLike(inputMenu);

        // "상위개념"이면 DB가 1개라도 잡혀도 확장 검색 수행
        boolean shouldExpand = targetList.isEmpty() || isBroadTerm(inputMenu);

        if (shouldExpand && !inputMenu.isEmpty()) {

            List<String> allMenus = repository.findAllDistinctMenus();

            String systemRole =
                "너는 '사용자 입력'을 '상품 리스트' 기반으로 '검색용 핵심 상품명(키워드)' 1개로 변환하는 엔진이다. 규칙을 반드시 지켜라.\n" +
                "1) 출력은 반드시 검색용 핵심 키워드 1개만 출력해라.\n" +
                "2) 출력에는 따옴표, 대괄호, 설명, 이모지, 줄바꿈을 절대 포함하지 마라.\n" +
                "3) 브랜드/제조사만 다른 동일 계열이 여러 개 있으면 브랜드를 제거한 공통 핵심 상품명만 출력해라.\n" +
                "   예: '포스트 콘푸라이트', '켈로그 콘푸라이트'가 있으면 반드시 '콘푸라이트'\n" +
                "4) 동의어/표현 차이를 이해해 매칭하라. (예: 시리얼=씨리얼)\n" +
                "5) 예시: 사용자 입력이 '씨리얼/시리얼'이고 리스트에 콘푸라이트 계열이 있으면 '콘푸라이트'\n" +
                "6) NONE/ERROR 같은 단어를 출력하지 마라.";

            String userPrompt = "상품 리스트: " + allMenus + "\n사용자 입력: " + inputMenu;

            String aiKeywordRaw = openAiService.getGptResponse(systemRole, userPrompt, 0.0);
            String aiKeyword = cleanGptText(aiKeywordRaw);

            if (!aiKeyword.isBlank()
                    && !"NONE".equalsIgnoreCase(aiKeyword)
                    && !"ERROR".equalsIgnoreCase(aiKeyword)) {

                // 1차 검색
                List<Stats> list1 = repository.findAllByMenuLike(aiKeyword);

                // 2차 확장 검색
                String core = extractCoreKeywordSmart(aiKeyword);
                List<Stats> list2 = core.equals(aiKeyword) ? Collections.emptyList()
                        : repository.findAllByMenuLike(core);

                // 기존 DB결과(targetList) + list1 + list2 합치기
                Map<String, Stats> merged = new LinkedHashMap<>();
                for (Stats s : targetList) merged.put(uniqueKey(s), s);
                for (Stats s : list1) merged.put(uniqueKey(s), s);
                for (Stats s : list2) merged.put(uniqueKey(s), s);

                targetList = new ArrayList<>(merged.values());
            }
        }

        Map<String, Stats> uniqueMap = new LinkedHashMap<>();
        for (Stats s : targetList) uniqueMap.putIfAbsent(uniqueKey(s), s);

        List<Map<String, Object>> dbResults = uniqueMap.values().stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("menu", s.getMenu());
            map.put("category", s.getCategory());
            map.put("averagePrice", s.getPrice());
            map.put("surveyDate", s.getSurveyDate());

            int avg = s.getPrice();
            String resultMsg = (userPrice > avg * 1.1) ? "비싼 소비예요 😥"
                    : (userPrice < avg * 0.9) ? "아주 합리적인 소비예요 👍"
                    : "적정한 소비예요 🙂";
            map.put("result", resultMsg);
            return map;
        }).collect(Collectors.toList());

        String marketSystemRole =
                "너는 한국의 온/오프라인 물가를 바탕으로 구매 전 참고용 '일반적인 가격대'를 현실적으로 제시하는 도우미다. " +
                "과장하지 말고 보수적으로 추정해라. " +
                "팁은 항상 3개의 설명형 문장으로 작성해라.";

        String marketPrompt = String.format(
                "사용자가 '%s'의 가격을 궁금해해. 대한민국 내 일반적인 대형마트나 온라인 쇼핑몰에서 흔히 유통되는 기준(단위/용량 포함)으로 참고용 평균 가격대를 제시해줘.\n" +
                "답변 형식은 반드시 아래 형식을 지켜줘.\n" +
                "[가격: 0,000원]\n" +
                "[팁: 문장1. 문장2. 문장3.]\n" +
                "규칙:\n" +
                "- [팁:] 안에는 정확히 3개의 문장을 포함해라.\n" +
                "- 각 문장은 최소 50자 이상, 최대 90자 이내로 충분히 설명적으로 작성해라.\n" +
                "- 각 문장은 소비자가 왜 도움이 되는지 이해할 수 있도록 이유를 포함해 서술해라.\n" +
                "- 문장 끝은 반드시 마침표(.)로 끝내라.\n" +
                "- 가격과 팁 사이는 줄바꿈 1번만 허용한다.\n" +
                "- 팁 내부에서는 줄바꿈을 절대 사용하지 마라.\n" +
                "- 과장하거나 확인 불가능한 단정은 금지한다.\n" +
                "예시: [가격: 5,500원]\n" +
                "[팁: 대용량이나 묶음 상품은 단위당 가격이 낮아 장기적인 지출을 줄이는 데 도움이 됩니다. 행사 기간에는 동일 상품이라도 할인 폭이 커질 수 있어 구매 시점을 비교하는 것이 좋습니다. 용량과 중량을 함께 확인하면 겉보기 가격보다 실제 가성비를 정확히 판단할 수 있습니다.]",
                inputMenu
        );




        String gptAdvice = openAiService.getGptResponse(marketSystemRole, marketPrompt, 0.2);

        Map<String, Object> response = new HashMap<>();
        response.put("dbResults", dbResults);
        response.put("gptAdvice", gptAdvice);
        return response;
    }

    public List<Stats> searchLatestByKeyword(String keyword) {
        String trimmed = (keyword == null) ? "" : keyword.trim();
        return trimmed.isEmpty() ? new ArrayList<>() : repository.findAllByMenuLike(trimmed);
    }

    // ---------------- helpers ----------------

    private String uniqueKey(Stats s) {
        return s.getMenu() + s.getCategory() + s.getSurveyDate();
    }

    private String cleanGptText(String raw) {
        if (raw == null) return "";
        return raw.replaceAll("[\\[\\]\"'`\\.\\r\\n]", "").trim();
    }

    private boolean isBroadTerm(String input) {
        if (input == null) return false;
        return Set.of("씨리얼", "시리얼").contains(input.trim());
    }

    private String normalizeInput(String menu) {
        String s = (menu == null) ? "" : menu.trim();
        if (s.equals("시리얼")) return "씨리얼";
        return s;
    }

    private String extractCoreKeywordSmart(String s) {
        if (s == null) return "";
        String t = s.trim();
        if (t.isEmpty()) return "";

        t = t.replace("포스트", " ").replace("켈로그", " ");

        String[] parts = t.trim().split("\\s+");
        for (int i = parts.length - 1; i >= 0; i--) {
            String tok = parts[i].trim();
            if (tok.isEmpty()) continue;
            if (tok.matches(".*[가-힣].*") && tok.replaceAll("[^가-힣]", "").length() >= 2) {
                return tok;
            }
        }
        return parts[parts.length - 1].trim();
    }
}
