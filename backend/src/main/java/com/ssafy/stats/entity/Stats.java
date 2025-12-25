package com.ssafy.stats.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Stats {
    private Long id;
    private String menu;   
    private int price;      
    private String category;  
    private LocalDate surveyDate;
}

