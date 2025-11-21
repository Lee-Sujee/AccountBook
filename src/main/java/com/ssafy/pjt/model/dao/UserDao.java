package com.ssafy.pjt.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.pjt.model.dto.User;

@Mapper
public interface UserDao {
	int insert(User user);
	User searchById(String id);
	int update(User user);
}
