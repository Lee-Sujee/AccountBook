package com.ssafy.challenge.controller;

import java.util.List;

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

import com.ssafy.challenge.dto.request.ChallengeRequestDto;
import com.ssafy.challenge.dto.response.ChallengeResponseDto;
import com.ssafy.challenge.service.ChallengeService;
import com.ssafy.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
	private final ChallengeService challengeService;
	
	@PostMapping("")
	public ResponseEntity<Void> createChallenge(
			 @AuthenticationPrincipal CustomUserDetails user, @RequestBody ChallengeRequestDto request){
		challengeService.createChallenge(user.getUserId(), request);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("")
	public ResponseEntity<List<ChallengeResponseDto>> getChallengeList(@AuthenticationPrincipal CustomUserDetails user){
		List<ChallengeResponseDto> res = challengeService.getChallengeList(user.getUserId());
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/{challengeId}")
	public ResponseEntity<ChallengeResponseDto> getOneChallenge(
			@PathVariable int challengeId, @AuthenticationPrincipal CustomUserDetails user){
		ChallengeResponseDto res = challengeService.getOneChallenge(challengeId, user.getUserId());
		return ResponseEntity.ok(res);
	}
	
	@PutMapping("/{challengeId}")
	public ResponseEntity<Void> updateChallenge(@PathVariable int challengeId,
			@AuthenticationPrincipal CustomUserDetails user, @RequestBody ChallengeRequestDto request){
		challengeService.updateChallenge(challengeId, user.getUserId(), request);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{challengeId}")
	public ResponseEntity<Void> deleteChallenge(@PathVariable int challengeId,
			@AuthenticationPrincipal CustomUserDetails user){
		challengeService.deleteChallenge(challengeId, user.getUserId());
		return ResponseEntity.noContent().build();
	}
}
