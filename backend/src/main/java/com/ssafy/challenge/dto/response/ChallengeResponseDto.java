package com.ssafy.challenge.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.challenge.enums.ChallengeStatus;

import lombok.Data;

@Data
public class ChallengeResponseDto {
	private int id;
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int target;
    private String description;
    private ChallengeStatus status;
    private LocalDateTime createdAt;
}
