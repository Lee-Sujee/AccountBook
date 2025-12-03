package com.ssafy.user.dto.request;

import lombok.Data;

@Data
public class SignUpRequestDto {
	private String id;
	private String email;
	private String password;
	private String passwordCheck; // 비밀번호 재확인용
	private String name;
	private Integer age;
	private String job;
}
