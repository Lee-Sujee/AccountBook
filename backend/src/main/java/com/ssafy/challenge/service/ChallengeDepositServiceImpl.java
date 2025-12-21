package com.ssafy.challenge.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.challenge.dto.request.ChallengeDepositRequestDto;
import com.ssafy.challenge.dto.response.ChallengeDepositResponseDto;
import com.ssafy.challenge.dto.response.ChallengeDepositSummaryResponseDto;
import com.ssafy.challenge.dto.response.ChallengeResponseDto;
import com.ssafy.challenge.repository.ChallengeDepositRepository;
import com.ssafy.challenge.repository.ChallengeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeDepositServiceImpl implements ChallengeDepositService {
	
	private final ChallengeService challengeService;
	private final ChallengeRepository challengeRepository;
	private final ChallengeDepositRepository challengeDepositRepository;
	
	@Override
	@Transactional
	public void createDeposit(int challengeId, String userId, ChallengeDepositRequestDto request) {
		challengeService.getOneChallenge(challengeId, userId);
		
		if(request.getAmount() <= 0)
			throw new IllegalArgumentException("입금 금액은 0보다 커야 합니다.");
		ChallengeResponseDto challenge = challengeService.getOneChallenge(challengeId, userId);

		LocalDate today = LocalDate.now();
		if (today.isBefore(challenge.getStartDate())) {
		    throw new IllegalStateException("아직 챌린지 시작 전입니다! 시작일을 먼저 수정해주세요.");
		}
		int res = challengeDepositRepository.createDeposit(challengeId, request.getAmount());
		if(res != 1) throw new RuntimeException("입금 실패");
		
		int totalAmount = getDepositSummary(challengeId, userId).getTotalAmount();
	    int target = challenge.getTarget();
	    if (totalAmount >= target) {
	        challengeRepository.updateStatusToSuccess(challengeId);
	    }
	}

	@Override
	@Transactional(readOnly = true)
	public List<ChallengeDepositResponseDto> getDepositList(int challengeId, String userId) {
		challengeService.getOneChallenge(challengeId, userId);
		
		return challengeDepositRepository.getDepositList(challengeId);
	}

	@Override
	@Transactional(readOnly = true)
	public ChallengeDepositSummaryResponseDto getDepositSummary(int challengeId, String userId) {
		challengeService.getOneChallenge(challengeId, userId);
		return challengeDepositRepository.getDepositSummary(challengeId);
	}
	
}