package com.ssafy.pjt.service;

import com.ssafy.pjt.model.dto.User;

public interface UserService {
	int regist(User user); //회원가입 가즈아~!!
	User getUserById(String id); //로그인 가즈아~!!
	int updateUser(User user);
}
