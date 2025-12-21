package com.ssafy.challenge.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.challenge.dto.response.ChallengeResponseDto;
import com.ssafy.challenge.entity.ChallengeDepositEntity;
import com.ssafy.challenge.entity.ChallengeEntity;

@Mapper
public interface ChallengeRepository {

	int createChallenge(ChallengeEntity challenge);

	List<ChallengeResponseDto> getChallengeList(String userId);

	ChallengeResponseDto getOneChallenge(int challengeId);

	int updateChallenge(ChallengeResponseDto challenge);

	int deleteChallenge(ChallengeResponseDto challenge);

	int updateStatusToOngoing(LocalDate today);
	
	int updateStatusToEnd(LocalDate today);

	void updateStatusToSuccess(int challengeId);

}
