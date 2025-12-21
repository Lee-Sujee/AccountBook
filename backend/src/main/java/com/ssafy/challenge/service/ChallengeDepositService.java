package com.ssafy.challenge.service;

import java.util.List;

import com.ssafy.challenge.dto.request.ChallengeDepositRequestDto;
import com.ssafy.challenge.dto.response.ChallengeDepositResponseDto;
import com.ssafy.challenge.dto.response.ChallengeDepositSummaryResponseDto;

public interface ChallengeDepositService{

	void createDeposit(int challengeId, String userId, ChallengeDepositRequestDto request);

	List<ChallengeDepositResponseDto> getDepositList(int challengeId, String userId);

	ChallengeDepositSummaryResponseDto getDepositSummary(int challengeId, String userId);
	
}