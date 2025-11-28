package com.ssafy.user.dto;

import lombok.Data;

@Data
public class PasswordChangeRequestDto {
	private String currentPassword;
	private String newPassword;
	private String newPasswordCheck;
}
