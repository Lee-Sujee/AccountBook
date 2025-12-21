package com.ssafy.challenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.challenge.dto.request.ChallengeDepositRequestDto;
import com.ssafy.challenge.dto.response.ChallengeDepositResponseDto;
import com.ssafy.challenge.dto.response.ChallengeDepositSummaryResponseDto;
import com.ssafy.challenge.service.ChallengeDepositService;
import com.ssafy.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeDepositController {
	private final ChallengeDepositService challengeDepositService;
	
	// 입금하기
	@PostMapping("/{challengeId}/deposit")
	public ResponseEntity<Void> createDeposit(@PathVariable int challengeId, @AuthenticationPrincipal CustomUserDetails user, 
			@RequestBody ChallengeDepositRequestDto request){
		challengeDepositService.createDeposit(challengeId, user.getUserId(), request);
		return ResponseEntity.noContent().build();
	}
	
	// 입금내역 전체조회
	@GetMapping("/{challengeId}/deposit")
	public ResponseEntity<List<ChallengeDepositResponseDto>> getDepositList(@PathVariable int challengeId, @AuthenticationPrincipal CustomUserDetails user){
		List<ChallengeDepositResponseDto> res = challengeDepositService.getDepositList(challengeId, user.getUserId());
		return ResponseEntity.ok(res);
	}
	
	// 총 입금액 조회
	@GetMapping("/{challengeId}/deposit/summary")
	public ResponseEntity<ChallengeDepositSummaryResponseDto> getDepositSummary(@PathVariable int challengeId, @AuthenticationPrincipal CustomUserDetails user){
		ChallengeDepositSummaryResponseDto res = challengeDepositService.getDepositSummary(challengeId, user.getUserId());
		return ResponseEntity.ok(res);
	}
}
