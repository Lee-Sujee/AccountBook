package com.ssafy.pjt.model.dto;

import lombok.Data;

@Data
public class Book {
	private int bookNum;
	private int userNum;
	private String bookRegDate;
	private String bookCategory;
	private String bookContent;
	private boolean bookType;
	private int bookAmount;
	private String bookMemo;
}
