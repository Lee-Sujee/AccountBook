package com.ssafy.stats.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Stats {
    private Long id;
    private String menu;      // 상품명
    private int price;        // 평균판매가격
    private String category;  // 업태
    private LocalDate surveyDate; //조사일 추가 최근거로 잘라야 되니까
}

