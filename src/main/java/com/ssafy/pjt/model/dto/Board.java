package com.ssafy.pjt.model.dto;

import lombok.Data;

@Data
public class Board {
	private int boardNum;
	private int userNum;
	private String boardTitle;
	private String boardContent;
	private int baordLike;
	private int viewCount;
	private String boardRegDate;
}
