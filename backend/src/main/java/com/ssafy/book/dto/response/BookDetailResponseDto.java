package com.ssafy.book.dto.response;

import lombok.Data;

//상세조회
@Data
public class BookDetailResponseDto {
	private Long id;
	private String category;
	private String content;
	private String type;
	private int amount;
	private String createdAt;
	private String memo;
}
