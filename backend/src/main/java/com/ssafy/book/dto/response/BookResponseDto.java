package com.ssafy.book.dto.response;

import lombok.Data;

@Data
public class BookResponseDto {
	private Long id;
	private String category;
	private String content;
	private String type;
	private int amount;
	private String createdAt;
}
