package com.ssafy.user.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.user.dto.request.SignUpRequestDto;
import com.ssafy.user.dto.request.UserUpdateRequestDto;
import com.ssafy.user.entity.User;

@Mapper
public interface UserRepository {
	int insertUser(SignUpRequestDto user);
	
	User selectUserById(String id);
	User selectUserByEmail(String email);
	
	String selectHashedPasswordByEmail(String email);
	
	int updateUserInfo(User user);
	
	int updatePassword(User user);

}