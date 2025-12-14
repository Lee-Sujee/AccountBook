package com.ssafy.book.entity;

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
	private String createdAt;
}
