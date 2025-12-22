package com.ssafy.challenge.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.challenge.dto.request.ChallengeRequestDto;
import com.ssafy.challenge.dto.response.ChallengeResponseDto;
import com.ssafy.challenge.entity.ChallengeEntity;
import com.ssafy.challenge.enums.ChallengeStatus;
import com.ssafy.challenge.repository.ChallengeRepository;
import com.ssafy.exception.ChallengeNotFoundException;
import com.ssafy.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

	private final ChallengeRepository challengeRepository;

	@Override
	@Transactional
	public void createChallenge(String userId, ChallengeRequestDto request) {
		// 시작일이 종료일보다 빠른지 체크
		if (request.getStartDate().isAfter(request.getEndDate())) {
			throw new IllegalArgumentException("시작일은 종료일보다 늦을 수 없습니다.");
		}

		// status 정하기
		LocalDate today = LocalDate.now();

		ChallengeStatus status = request.getStartDate().isAfter(today) ? ChallengeStatus.READY
				: ChallengeStatus.ONGOING;

		ChallengeEntity challenge = new ChallengeEntity();
		challenge.setUserId(userId);
		challenge.setStartDate(request.getStartDate());
		challenge.setEndDate(request.getEndDate());
		challenge.setTarget(request.getTarget());
		challenge.setDescription(request.getDescription());
		challenge.setStatus(status);

		int res = challengeRepository.createChallenge(challenge);
		if (res != 1)
			throw new RuntimeException("챌린지 등록에 실패했습니다.");
	}

	@Scheduled(cron = "0 0 0 * * *") // 매일 자정
	@Transactional
	public void updateChallengeStatus() {
		LocalDate today = LocalDate.now();

		challengeRepository.updateStatusToOngoing(today);
		challengeRepository.updateStatusToEnd(today);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ChallengeResponseDto> getChallengeList(String userId) {
		List<ChallengeResponseDto> res = challengeRepository.getChallengeList(userId);
		return res;
	}
	@Override
	@Transactional(readOnly = true)
	public ChallengeResponseDto getOneChallenge(int challengeId, String userId) {
		ChallengeResponseDto res = challengeRepository.getOneChallenge(challengeId);
		if (res == null)
			throw new ChallengeNotFoundException();
		if (!userId.equals(res.getUserId()))
			throw new ForbiddenException();
		return res;
	} 


	@Override
	@Transactional
	public void updateChallenge(int challengeId, String userId, ChallengeRequestDto request) {
		ChallengeResponseDto challenge = getOneChallenge(challengeId, userId);
		if (!challenge.getUserId().equals(userId)) {
			throw new ForbiddenException();
		}
		challenge.setStartDate(request.getStartDate());
		challenge.setEndDate(request.getEndDate());
		challenge.setTarget(request.getTarget());
		challenge.setDescription(request.getDescription());
		// status 정하기
		LocalDate today = LocalDate.now();
		
		ChallengeStatus status = request.getStartDate().isAfter(today) ? ChallengeStatus.READY
				: ChallengeStatus.ONGOING;
		challenge.setStatus(status);
		// 시작일이 종료일보다 빠른지 체크
		if (request.getStartDate().isAfter(request.getEndDate())) {
			throw new IllegalArgumentException("시작일은 종료일보다 늦을 수 없습니다.");
		}


		int res = challengeRepository.updateChallenge(challenge);
		if (res != 1)
			throw new RuntimeException("챌린지 수정에 실패했습니다.");
	}

	@Override
	@Transactional
	public void deleteChallenge(int challengeId, String userId) {
		ChallengeResponseDto challenge = getOneChallenge(challengeId, userId);
		if (!challenge.getUserId().equals(userId))
			throw new ForbiddenException();
		int res = challengeRepository.deleteChallenge(challenge);
		if (res != 1)
			throw new RuntimeException("챌린지 삭제에 실패했습니다.");
	}

	

}
