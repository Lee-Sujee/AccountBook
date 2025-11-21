package com.ssafy.pjt.model.dto;

import lombok.Data;

@Data
public class Comment {
	private int commentNum;
	private int boardNum;
	private String commentContent;
	private int commentLike;
	private String commentRegDate;
}
