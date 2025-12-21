package com.ssafy.community.dto.response;

import lombok.Data;

@Data
public class CommentResponseDto {
	private int id;
	private String writerId;
	private String writerName;
	private int boardId;
	private String content;
	private String createdAt;
}
