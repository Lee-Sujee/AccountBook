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
	//회원가입 처리 (마지막에 insert로 회원가입 정보 보내기)
	
	@Override
	@Transactional
	public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
		if(!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordCheck())) {
			return null;
		}
		//DB에 넣어야겠지..? 맞나 몰라
		userDao.insertUser(signUpRequestDto);
		return null;
		}

	
	//로그인
	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		//login에 필요한 이메일, 패스워드 저장
		String email = loginRequestDto.getEmail();
		String password = loginRequestDto.getPassword();
		
		//이메일로 비밀번호 조회 (해당 이메일로 회원가입 한 password 조회)
		String dbPassword = userDao.selectHashedPasswordByEmail(email);
		
		//저장된 비번 없으면 null 반환
		if(dbPassword == null) {
			return null;
		}
		
		//비번 일치하는지 확인
		if(password.equals(dbPassword)) {
			UserResponseDto user = userDao.selectUserByEmail(email);
			return new LoginResponseDto(); //로그인 성공 -> 응답 DTO 반환해야겠지요..? 맞나요...?
			//비번 일치하지 않으면 null 반환
		} else {
			return null;
		}
	}

	
	//사용자 정보 수정
	@Override
	@Transactional
	public UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto) {
		//email이 DTO에 저장되어 있어야 수정이 가능
		//없으면? 회원가입도 안 되어 있는데 어떻게 수정해!
		int existEmail = userDao.updateUserInfo(userUpdateRequestDto);
		
		if(existEmail > 0) {
			//만약 있다면 (값이 무조건 0 이상이겠지? 이렇게 하는 거 맞겠지?)
			return new UserResponseDto(); //응답 DTO 반환
		}
		return null; //업데이트 안 되면 null 반환
	}

	
	//비밀번호 수정	
	@Override
	@Transactional
	public boolean changePassword(String email, PasswordChangeRequestDto passwordChangeRequestDto) {
		return false;
		
	}

	

	
}
