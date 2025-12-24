package com.ssafy.community.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.community.dto.request.CommentRequestDto;
import com.ssafy.community.dto.response.CommentResponseDto;
import com.ssafy.community.service.CommentService;
import com.ssafy.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommentController {
	
	private final CommentService commentService;
	// 전체 댓글 조회
	@GetMapping("/{boardId}/comments")
	public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable int boardId){
		List<CommentResponseDto> res = commentService.getCommentList(boardId);
		return ResponseEntity.ok(res);
	}
	
	// 댓글 작성
	@PostMapping("/{boardId}/comments")
	public ResponseEntity<Void> createComment(
			@PathVariable int boardId, @AuthenticationPrincipal CustomUserDetails user, 
			@RequestBody CommentRequestDto dto){
		commentService.createComment(boardId, user.getUserId(), dto);
		return ResponseEntity.noContent().build();
	}
	
	// 댓글 수정
	@PutMapping("/comments/{commentId}")
	public ResponseEntity<Void> updateComment(@PathVariable int commentId, 
			@AuthenticationPrincipal CustomUserDetails user, @RequestBody CommentRequestDto dto){
		commentService.updateComment(commentId, user.getUserId(), dto);
		return ResponseEntity.noContent().build();
	}
	
	// 댓글 삭제
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable int commentId, @AuthenticationPrincipal CustomUserDetails user){
		commentService.deleteComment(commentId, user.getUserId());
		return ResponseEntity.noContent().build();
	}
}
