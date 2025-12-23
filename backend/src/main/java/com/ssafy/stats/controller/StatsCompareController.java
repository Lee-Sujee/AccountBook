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
     * 평균 가격 비교 API
     * GET /api/v1/comparison/price?menu=...&category=...&userPrice=...
     */
    @GetMapping("/price")
    public ResponseEntity<?> comparePrice(
            @RequestParam String menu,
            @RequestParam String category,
            @RequestParam int userPrice
    ) {
    	//gpt 답변까지 포함
        return ResponseEntity.ok(compareService.getCombinedResult(menu, category, userPrice));
    }

    /**
     * 키워드로 최신 데이터 검색 API
     * GET /api/v1/comparison/latest/search?keyword=비타500
     */
    @GetMapping("/latest/search")
    public ResponseEntity<?> searchLatestList(@RequestParam String keyword) {
        // 키워드 검색 메서드 호출
        return ResponseEntity.ok(compareService.searchLatestByKeyword(keyword));
    }
}