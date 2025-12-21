package com.ssafy.challenge.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ssafy.challenge.enums.ChallengeStatus;

import lombok.Data;

@Data
public class ChallengeEntity {
	private int id;
	private String userId;
	private LocalDate startDate;
	private LocalDate endDate;
	private int target;
	private String description;
	private ChallengeStatus status;
	private LocalDateTime createdAt;
}

 