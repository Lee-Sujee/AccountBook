package com.ssafy.user.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
	private String name;
	private int age;
	private String job;
}
