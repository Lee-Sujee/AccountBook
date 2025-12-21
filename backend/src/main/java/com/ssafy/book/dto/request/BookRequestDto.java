package com.ssafy.book.dto.request;

import lombok.Data;

// 등록, 수정 요청 DTO
@Data
public class BookRequestDto {
    private String category;
    private String content;
    private String type; // income / expense
    private int amount;
    private String memo;

    private String createdAt;
}
