package com.ssafy.pjt.model.dto;

import lombok.Data;

@Data
public class User {
	private int userNum;
	private String userId;
	private String userPassword;
	private String userName;
	private int userAge;
	private String userJob;
	private String signupDate;

}
