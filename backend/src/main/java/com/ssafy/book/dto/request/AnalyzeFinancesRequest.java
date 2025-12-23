package com.ssafy.book.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnalyzeFinancesRequest {
    private String type;   // "income" | "expense" 
    private Integer year;
    private Integer month;
    private List<HistoryItem> history;

    @Getter @Setter
    public static class HistoryItem {
        private String date;     // LocalDate로 바꿔도 됨
        private String category;
        private Integer amount;
        private String type;     // entry 자체 타입
        private String memo;
    }
}