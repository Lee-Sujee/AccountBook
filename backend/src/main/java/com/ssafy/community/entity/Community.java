package com.ssafy.community.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Community {
	private int id;
	private String userId;
	private String title;
	private String content;
	private int likes;
	private int view;
	private LocalDateTime createdAt;
}
