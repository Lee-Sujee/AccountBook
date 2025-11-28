package com.ssafy.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.user.dao.UserDao;
import com.ssafy.user.dto.LoginRequestDto;
import com.ssafy.user.dto.LoginResponseDto;
import com.ssafy.user.dto.PasswordChangeRequestDto;
import com.ssafy.user.dto.SignUpRequestDto;
import com.ssafy.user.dto.SignUpResponseDto;
import com.ssafy.user.dto.UserResponseDto;
import com.ssafy.user.dto.UserUpdateRequestDto;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	//회원가입 처리
	@Override
	@Transactional
	public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
		if(!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordCheck())) {
			return null;	
		}
		userDao.insertUser(signUpRequestDto);
		return null;
	}

	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changePassword(String email, PasswordChangeRequestDto passwordChangeRequestDto) {
		// TODO Auto-generated method stub
		return false;
	}

	

	
}
