package com.ssafy.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.user.dto.SignUpRequestDto;
import com.ssafy.user.dto.UserResponseDto;
import com.ssafy.user.dto.UserUpdateRequestDto;

@Mapper
public interface UserDao {
	int insertUser(SignUpRequestDto user);
	
	UserResponseDto selectUserByEmail(String email);
	
	String selectHashedPasswordByEmail(String email);
	
	int updateUserInfo(UserUpdateRequestDto user);

}
