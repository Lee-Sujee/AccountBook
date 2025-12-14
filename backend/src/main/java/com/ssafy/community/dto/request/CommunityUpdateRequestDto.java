package com.ssafy.community.dto.request;

import lombok.Data;

@Data
public class CommunityUpdateRequestDto {
	private int id;
	private String title;
	private String content;
}
