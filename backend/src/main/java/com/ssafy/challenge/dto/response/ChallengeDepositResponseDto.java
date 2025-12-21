package com.ssafy.challenge.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChallengeDepositResponseDto {
	private int id;            
    private int amount;
    private LocalDateTime createdAt;
}
