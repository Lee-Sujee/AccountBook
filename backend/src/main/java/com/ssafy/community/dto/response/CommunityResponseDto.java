package com.ssafy.community.dto.response;

import lombok.Data;

@Data
public class CommunityResponseDto {
	private int id;
	private String title;
	private String content;
	private String writerId;
	private String writerName;
	private int likes;
	private int views;
	private String createdAt;
}
