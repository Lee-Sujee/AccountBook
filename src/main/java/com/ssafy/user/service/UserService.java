package com.ssafy.user.service;

import com.ssafy.user.dto.request.LoginRequestDto;
import com.ssafy.user.dto.request.PasswordChangeRequestDto;
import com.ssafy.user.dto.request.SignUpRequestDto;
import com.ssafy.user.dto.request.UserUpdateRequestDto;
import com.ssafy.user.dto.response.LoginResponseDto;
import com.ssafy.user.dto.response.SignUpResponseDto;
import com.ssafy.user.dto.response.UserResponseDto;

public interface UserService {
	SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto); //회원가입 가즈아~!!
	LoginResponseDto login(LoginRequestDto loginRequestDto); //로그인 가즈아~!!
	UserResponseDto update(String email, UserUpdateRequestDto userUpdateRequestDto); //user 수정
	boolean changePassword(String email, PasswordChangeRequestDto passwordChangeRequestDto);
}
