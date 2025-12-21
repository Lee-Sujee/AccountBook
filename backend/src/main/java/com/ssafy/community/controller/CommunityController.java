package com.ssafy.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.community.dto.request.CommunityRequestDto;
import com.ssafy.community.dto.request.CommunityUpdateRequestDto;
import com.ssafy.community.dto.response.CommunityLikeResponseDto;
import com.ssafy.community.dto.response.CommunityResponseDto;
import com.ssafy.community.service.CommunityService;
import com.ssafy.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/community")
@CrossOrigin(origins = "*")
public class CommunityController {
	@Autowired
	private CommunityService communityService;
	
	@GetMapping("")
	public ResponseEntity<List<CommunityResponseDto>> getCommunityList(){
		List communityList = communityService.getCommunityList();
		if(communityList.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(communityList);
	}
	
	@PostMapping("/write")
	public ResponseEntity<CommunityResponseDto> createCommunity(@AuthenticationPrincipal CustomUserDetails userDetails,
		    @RequestBody CommunityRequestDto dto){
		CommunityResponseDto communityResponseDto = communityService.createCommunity(userDetails.getUserId(), dto);
		if(communityResponseDto == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(communityResponseDto);
	}
	
	// 상세조회
	@GetMapping("/{id}")
	public ResponseEntity<CommunityResponseDto> getCommunityDetail(@PathVariable int id){
		CommunityResponseDto communityResponseDto = communityService.getCommunityDetail(id);
		return ResponseEntity.ok(communityResponseDto);
	}
	
	// 상세조회 - 조회수 증가
	@PostMapping("/{id}/view")
	public ResponseEntity<Void> increaseView(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {
	    communityService.increaseViewWithCookie(id, request, response);
	    return ResponseEntity.noContent().build();
	}

	// 게시글 좋아요
	@PostMapping("/{id}/like")
	public ResponseEntity<CommunityLikeResponseDto> like(@PathVariable int id, @AuthenticationPrincipal CustomUserDetails user){
		if(user == null)
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		CommunityLikeResponseDto result = communityService.toggleLike(id, user.getUserId());
		return ResponseEntity.ok(result);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CommunityResponseDto> updateCommunity(@PathVariable int id, @RequestBody CommunityUpdateRequestDto dto,
	    @AuthenticationPrincipal CustomUserDetails user
	) {
	    CommunityResponseDto communityResponseDto = communityService.updateCommunity(id, dto, user.getUserId());
	    return ResponseEntity.ok(communityResponseDto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCommunity(@PathVariable int id, @AuthenticationPrincipal CustomUserDetails user){
		communityService.deleteCommunity(id, user.getUserId());
		return ResponseEntity.ok(null);
	}
}
