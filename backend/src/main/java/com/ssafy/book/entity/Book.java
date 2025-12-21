package com.ssafy.book.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Book {
	private int id;
	private String userId;
	private String category;
	private String content;
	private String type;
	private int amount;
	private String memo;
	private LocalDateTime createdAt;
}
