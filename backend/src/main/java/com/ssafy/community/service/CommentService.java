package com.ssafy.community.service;

import java.util.List;

import com.ssafy.community.dto.request.CommentRequestDto;
import com.ssafy.community.dto.response.CommentResponseDto;
import com.ssafy.community.dto.response.CommunityResponseDto;

public interface CommentService {

	List<CommentResponseDto> getCommentList(int boardId);

	void createComment(int boardId, String userId, CommentRequestDto dto);

	void deleteComment(int commentId, String userId);

	void updateComment(int commentId, String userId, CommentRequestDto dto);

}
