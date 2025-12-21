package com.ssafy.community.entity;

import java.util.Date;

import com.ssafy.community.dto.response.CommunityResponseDto;
import com.ssafy.user.entity.User;

import lombok.Data;

@Data
public class CommunityComment {
	private int id;
	private User user;
	private CommunityResponseDto community;
	private String content;
	private Date createdAt;
}
