package com.ssafy.stats.controller;

import java.util.Map;

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

    @GetMapping("/price")
    public ResponseEntity<?> comparePrice(
            @RequestParam String productName,
            @RequestParam int userPrice
    ) {

        Map<String, Object> result =
                compareService.compare(productName, userPrice);

        return ResponseEntity.ok(result);
    }
}
