package com.ssafy.community.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.community.dto.request.CommunityUpdateRequestDto;
import com.ssafy.community.dto.response.CommunityResponseDto;
import com.ssafy.community.entity.Community;

@Mapper
public interface CommunityRepository {

	List selectCommunityList();

	void insertCommunity(Community community);

	CommunityResponseDto selectCommunityDetail(int id);
	
	int increaseViews(int id);

	int updateCommunity(int id, CommunityUpdateRequestDto dto);

	void deleteCommunity(int id);


	int selectBoardLikeCount(int boardId);
	
	boolean existsBoardLike(int boardId, String userId);
	
	void insertBoardLike(int boardId, String userId);
	
	void increaseBoardLikeCount(int boardId);
	
	void deleteBoardLike(int boardId, String userId);
	
	void decreaseBoardLikeCount(int boardId);

}
