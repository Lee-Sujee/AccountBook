package com.ssafy.user.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
	private String id;
	private String email;
	private String name;
	private String token;
}
