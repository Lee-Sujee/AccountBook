package com.ssafy.user.service;

import com.ssafy.user.dto.LoginRequestDto;
import com.ssafy.user.dto.LoginResponseDto;
import com.ssafy.user.dto.PasswordChangeRequestDto;
import com.ssafy.user.dto.SignUpRequestDto;
import com.ssafy.user.dto.SignUpResponseDto;
import com.ssafy.user.dto.UserResponseDto;
import com.ssafy.user.dto.UserUpdateRequestDto;

public interface UserService {
	SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto); //회원가입 가즈아~!!
	LoginResponseDto login(LoginRequestDto loginRequestDto); //로그인 가즈아~!!
	UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto); //user 수정
	boolean changePassword(String email, PasswordChangeRequestDto passwordChangeRequestDto);
}
