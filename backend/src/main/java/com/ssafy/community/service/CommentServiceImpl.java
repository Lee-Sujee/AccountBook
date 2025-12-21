package com.ssafy.community.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.community.dto.request.CommentRequestDto;
import com.ssafy.community.dto.response.CommentResponseDto;
import com.ssafy.community.dto.response.CommunityResponseDto;
import com.ssafy.community.entity.Community;
import com.ssafy.community.entity.CommunityComment;
import com.ssafy.community.repository.CommentRepository;
import com.ssafy.community.repository.CommunityRepository;
import com.ssafy.exception.CommentNotFoundException;
import com.ssafy.exception.ForbiddenException;
import com.ssafy.user.entity.User;
import com.ssafy.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	@Autowired
	private final CommentRepository commentRepository;
	@Autowired
	private final CommunityRepository communityRepository;
	@Autowired
	private final UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<CommentResponseDto> getCommentList(int boardId) {
		return commentRepository.getCommentList(boardId);
	}
	
	@Transactional(readOnly = true)
	public CommentResponseDto getOneComment(int commentId) {
		return commentRepository.getOneComment(commentId);
	}
	
	@Override
	@Transactional
	public void createComment(int boardId, String userId, CommentRequestDto dto) {
		// 게시글 조회
        CommunityResponseDto community = communityRepository.selectCommunityDetail(boardId);
        if(community == null) new RuntimeException("게시글이 존재하지 않습니다.");

        // 사용자 조회
        User user = userRepository.selectUserById(userId);
        if(user == null) new RuntimeException("사용자가 존재하지 않습니다.");

        // 댓글 엔티티 생성
        CommunityComment comment = new CommunityComment();
        comment.setCommunity(community);
        comment.setUser(user);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(new Date());

        commentRepository.createComment(comment);
	}

	@Override
	@Transactional
	public void updateComment(int commentId, String userId, CommentRequestDto dto) {
		CommentResponseDto comment = getOneComment(commentId);
		System.out.println("=================");
		System.out.println(userId);
		System.out.println(comment);
		if(comment == null) throw new CommentNotFoundException();
		
		if(!userId.equals(comment.getWriterId())) throw new ForbiddenException();
		
		commentRepository.updateComment(commentId, dto);		
	}
	
	
	@Override
	@Transactional
	public void deleteComment(int commentId, String userId) {
		CommentResponseDto comment = getOneComment(commentId);
		
		if(comment == null) throw new CommentNotFoundException();
		
		if (!comment.getWriterId().equals(userId)) throw new ForbiddenException();
		
		commentRepository.deleteComment(commentId);
	}
}
