package com.ssafy.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.community.dto.request.CommunityRequestDto;
import com.ssafy.community.dto.request.CommunityUpdateRequestDto;
import com.ssafy.community.dto.response.CommunityLikeResponseDto;
import com.ssafy.community.dto.response.CommunityResponseDto;
import com.ssafy.community.entity.Community;
import com.ssafy.community.repository.CommunityRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CommunityServiceImpl implements CommunityService {

	@Autowired
	private CommunityRepository communityRepository;

	@Override
	@Transactional
	public List getCommunityList() {
		return communityRepository.selectCommunityList();
	}

	@Override
	@Transactional
	public CommunityResponseDto createCommunity(String userId, CommunityRequestDto dto) {
		Community community = new Community();
		community.setUserId(userId);
		community.setTitle(dto.getTitle());
		community.setContent(dto.getContent());
		communityRepository.insertCommunity(community);
		int newId = community.getId();
		return communityRepository.selectCommunityDetail(newId);
	}

	@Override
	@Transactional
	public CommunityResponseDto getCommunityDetail(int id) {
		return communityRepository.selectCommunityDetail(id);
	}

	@Override
	@Transactional
	public CommunityResponseDto updateCommunity(int id, CommunityUpdateRequestDto dto, String userId) {
		CommunityResponseDto post = communityRepository.selectCommunityDetail(id);

		communityRepository.updateCommunity(id, dto);

		return communityRepository.selectCommunityDetail(id);
	}

	@Override
	@Transactional
	public CommunityResponseDto deleteCommunity(int id, String userId) {
		communityRepository.deleteCommunity(id);
		return null;
	}

	@Override
	@Transactional
	public void increaseViewWithCookie(int postId, HttpServletRequest request, HttpServletResponse response) {
		String cookieName = "community_view_" + postId;
		Cookie[] cookies = request.getCookies();
		
		boolean viewed = false;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					viewed = true;
					break;
				}
			}
		}

		if (!viewed) {
			communityRepository.increaseViews(postId);

			Cookie viewCookie = new Cookie(cookieName, "true");
			viewCookie.setMaxAge(60 * 10); // 10분
			viewCookie.setPath("/");
			response.addCookie(viewCookie);
		}
	}

	@Override
	public CommunityLikeResponseDto toggleLike(int boardId, String userId) {
		boolean exists = communityRepository.existsBoardLike(boardId, userId);
		if(exists) {
			communityRepository.deleteBoardLike(boardId, userId);
			communityRepository.decreaseBoardLikeCount(boardId);
		}
		else {
			communityRepository.insertBoardLike(boardId, userId);
			communityRepository.increaseBoardLikeCount(boardId);
		}
		
		int likeCount = communityRepository.selectBoardLikeCount(boardId);
		return new CommunityLikeResponseDto(!exists, likeCount);
	}

}
