package com.ssafy.pjt.model.dto;

import lombok.Data;

@Data
public class Challenge {
	private int userNum;
	private int challengeId;
	private String challengeStartDate;
	private String challengeEndDate;
	private int totalAmount;
	private int currentAmount;
	private String challengeCategory;
	private String challengeDetail;
	private boolean challengeStatus;
	private String challengeRegDate;
}
