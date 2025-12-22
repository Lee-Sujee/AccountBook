package com.ssafy.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.stats.service.StatsCompareService;

@RestController
@RequestMapping("/api/v1/comparison")
public class StatsCompareController {

    @Autowired
    private StatsCompareService compareService;

    /**
     * ✅ 평균 가격 비교 API
     * DB 데이터(리스트)와 ChatGPT의 시장 조언(String)을 한꺼번에 반환합니다.
     * GET /api/v1/comparison/price?menu=...&category=...&userPrice=...
     */
    @GetMapping("/price")
    public ResponseEntity<?> comparePrice(
            @RequestParam String menu,
            @RequestParam String category,
            @RequestParam int userPrice
    ) {
        // 기존 compare() 대신, GPT 답변까지 포함된 getCombinedResult()를 호출합니다.
        return ResponseEntity.ok(compareService.getCombinedResult(menu, category, userPrice));
    }

    /**
     * ✅ 키워드로 최신 데이터 검색 API
     * GET /api/v1/comparison/latest/search?keyword=비타500
     */
    @GetMapping("/latest/search")
    public ResponseEntity<?> searchLatestList(@RequestParam String keyword) {
        // 서비스에 작성된 키워드 검색 메서드를 호출합니다.
        return ResponseEntity.ok(compareService.searchLatestByKeyword(keyword));
    }
}