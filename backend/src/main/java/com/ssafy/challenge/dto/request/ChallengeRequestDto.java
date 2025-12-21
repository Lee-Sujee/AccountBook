package com.ssafy.challenge.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ChallengeRequestDto {
	private LocalDate startDate;
	private LocalDate endDate;
	private int target;
	private String description;
}
