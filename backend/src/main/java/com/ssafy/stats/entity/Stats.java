package com.ssafy.stats.entity;

import lombok.Data;

@Data
public class Stats {
    private Long id;
    private String menu;      // 상품명
    private int price;        // 평균판매가격
    private String category;  // 업태
}

