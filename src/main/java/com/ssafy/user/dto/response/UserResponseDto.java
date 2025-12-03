package com.ssafy.user.dto.response;

import lombok.Data;

// 마이페이지용...
@Data
public class UserResponseDto {
	private String email;
	private String name;
	private int age;
	private String job;
}
