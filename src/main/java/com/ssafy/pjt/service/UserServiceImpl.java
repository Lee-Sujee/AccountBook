package com.ssafy.pjt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.pjt.model.dao.UserDao;
import com.ssafy.pjt.model.dto.User;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;

	@Override
	public int regist(User user) {
		return userDao.insert(user);
	}

	@Override
	public User getUserById(String id) {
		return userDao.searchById(id);
	}

	@Override
	public int updateUser(User user) {
		return userDao.update(user);
	}
	
}
