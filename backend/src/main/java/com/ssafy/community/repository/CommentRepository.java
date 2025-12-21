package com.ssafy.community.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.community.dto.request.CommentRequestDto;
import com.ssafy.community.dto.response.CommentResponseDto;
import com.ssafy.community.entity.CommunityComment;

@Mapper
public interface CommentRepository {

	List<CommentResponseDto> getCommentList(int boardId);

	CommentResponseDto getOneComment(int commentId);

	int createComment(CommunityComment comment);

	void updateComment(int commentId, CommentRequestDto dto);

	void deleteComment(int commentId);

}
