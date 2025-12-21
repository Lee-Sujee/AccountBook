package com.ssafy.stats.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.stats.entity.Stats;
import com.ssafy.stats.repository.StatsRepository;

@Service
public class StatsCompareService {

    @Autowired
    private StatsRepository repository;

    public Map<String, Object> compare(String menu, String category, int userPrice) {

        Stats latest = repository.findByMenuAndCategory(menu, category);

        if (latest == null) {
            return Map.of(
                "menu", menu,
                "category", category,
                "averagePrice", 0,
                "result", "비교할 평균 가격 데이터가 없습니다."
            );
        }

        int avgPrice = latest.getPrice();

        String result;
        if (userPrice > avgPrice * 1.1) {
            result = "비싼 소비예요 😥";
        } else if (userPrice < avgPrice * 0.9) {
            result = "아주 합리적인 소비예요 👍";
        } else {
            result = "적정한 소비예요 🙂";
        }

        return Map.of(
            "menu", latest.getMenu(),
            "category", latest.getCategory(),
            "averagePrice", avgPrice,
            "userPrice", userPrice,
            "result", result,
            "surveyDate", latest.getSurveyDate()
        );
    }

    // ✅ 평균계산기: 키워드 검색 리스트
    public List<Stats> searchLatestByKeyword(String keyword) {
        String trimmed = (keyword == null) ? "" : keyword.trim();
        if (trimmed.isEmpty()) {
            return List.of();
        }
        return repository.findAllByMenuLike(trimmed);
    }
}
