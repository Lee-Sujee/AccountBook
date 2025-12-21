package com.ssafy.challenge.service;

import java.util.List;

import com.ssafy.challenge.dto.request.ChallengeRequestDto;
import com.ssafy.challenge.dto.response.ChallengeResponseDto;
import com.ssafy.challenge.entity.ChallengeEntity;

public interface ChallengeService {

	void createChallenge(String userId, ChallengeRequestDto request);

	List<ChallengeResponseDto> getChallengeList(String userId);

	void updateChallenge(int challengeId, String userId, ChallengeRequestDto request);

	void deleteChallenge(int challengeId, String userId);

	ChallengeResponseDto getOneChallenge(int challengeId, String userId);


}
