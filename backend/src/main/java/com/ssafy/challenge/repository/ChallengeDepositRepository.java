package com.ssafy.challenge.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.challenge.dto.response.ChallengeDepositResponseDto;
import com.ssafy.challenge.dto.response.ChallengeDepositSummaryResponseDto;

@Mapper
public interface ChallengeDepositRepository {

	int createDeposit(int challengeId, int amount);

	List<ChallengeDepositResponseDto> getDepositList(int challengeId);

	ChallengeDepositSummaryResponseDto getDepositSummary(int challengeId);

	int updateDeposit(int depositId, int challengeId, int amount);

	
}