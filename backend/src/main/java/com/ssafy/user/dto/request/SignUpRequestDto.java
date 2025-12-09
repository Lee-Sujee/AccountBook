package com.ssafy.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestDto {
	private String id;
	@NotBlank(message="이메일은 필수입니다.")
    @Email(message="올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message="비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message="비밀번호 확인은 필수입니다.")
    private String passwordCheck; // 비밀번호 재확인용
    
	private String name;
	private Integer age;
	private String job;
}
