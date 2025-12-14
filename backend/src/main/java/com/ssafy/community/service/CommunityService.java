package com.ssafy.community.service;

import java.util.List;

import com.ssafy.community.dto.request.CommunityRequestDto;
import com.ssafy.community.dto.request.CommunityUpdateRequestDto;
import com.ssafy.community.dto.response.CommunityLikeResponseDto;
import com.ssafy.community.dto.response.CommunityResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CommunityService {

	List getCommunityList();

	CommunityResponseDto createCommunity(String userId, CommunityRequestDto dto);

	CommunityResponseDto getCommunityDetail(int id);

	CommunityResponseDto updateCommunity(int id, CommunityUpdateRequestDto dto, String userId);

	CommunityResponseDto deleteCommunity(int id, String userId);

	void increaseViewWithCookie(int id, HttpServletRequest request, HttpServletResponse response);

	CommunityLikeResponseDto toggleLike(int id, String userId);
	
}
