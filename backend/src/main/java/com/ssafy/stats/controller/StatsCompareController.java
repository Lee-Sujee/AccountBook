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

    // (이미 수정했다고 한 비교 API 예시)
    // GET /api/v1/comparison/price?menu=...&category=...&userPrice=...
    @GetMapping("/price")
    public ResponseEntity<?> comparePrice(
            @RequestParam String menu,
            @RequestParam String category,
            @RequestParam int userPrice
    ) {
        return ResponseEntity.ok(compareService.compare(menu, category, userPrice));
    }

    // ✅ 평균계산기: 키워드로 menu 검색 → 업태별(=category별) 결과 리스트
    // GET /api/v1/comparison/latest/search?keyword=비타500
    @GetMapping("/latest/search")
    public ResponseEntity<?> searchLatestList(@RequestParam String keyword) {
        return ResponseEntity.ok(compareService.searchLatestByKeyword(keyword));
    }
}
