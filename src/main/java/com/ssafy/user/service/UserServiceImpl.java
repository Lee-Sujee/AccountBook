package com.ssafy.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.user.dto.request.LoginRequestDto;
import com.ssafy.user.dto.request.PasswordChangeRequestDto;
import com.ssafy.user.dto.request.SignUpRequestDto;
import com.ssafy.user.dto.request.UserUpdateRequestDto;
import com.ssafy.user.dto.response.LoginResponseDto;
import com.ssafy.user.dto.response.SignUpResponseDto;
import com.ssafy.user.dto.response.UserResponseDto;
import com.ssafy.user.entity.User;
import com.ssafy.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	//회원가입 처리 (마지막에 insert로 회원가입 정보 보내기)
	
	@Override
	@Transactional
	public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
		if(!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordCheck())) {
			return null;
		}
		// UUID 설정
		String uuid = UUID.randomUUID().toString();
        signUpRequestDto.setId(uuid); // DTO에 ID 설정
		
        userRepository.insertUser(signUpRequestDto);
		SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
		signUpResponseDto.setEmail(signUpRequestDto.getEmail());
		signUpResponseDto.setName(signUpRequestDto.getName());
		return signUpResponseDto;
		}

	
	//로그인
	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		User user = userRepository.selectUserByEmail(loginRequestDto.getEmail());
		if(!loginRequestDto.getPassword().equals(user.getPassword())) return null;
		
		if(user == null) return null;
		
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setEmail(user.getEmail());
		loginResponseDto.setName(user.getName());
		return loginResponseDto;
	}

	
	//사용자 정보 수정
	@Override
	@Transactional
	public UserResponseDto update(String email, UserUpdateRequestDto userUpdateRequestDto) {
		
		//Id 가져와서 사용자 정보 조회부터
		User user = userRepository.selectUserByEmail(email);
		
		//해당 이메일에 해당하는 user가 없음
		if(user == null) {
			return null;
		}
		
		user.setName(userUpdateRequestDto.getName());
		user.setAge(userUpdateRequestDto.getAge());
		user.setJob(userUpdateRequestDto.getJob());
		
		int updateUser = userRepository.updateUserInfo(user);
		
		if(updateUser > 0) {
			UserResponseDto userResponseDto = new UserResponseDto();
			userResponseDto.setEmail(user.getEmail());
			userResponseDto.setName(user.getName());
			userResponseDto.setAge(user.getAge());
			userResponseDto.setJob(user.getJob());
			return userResponseDto;
		}
		return null;
	}

	
	//비밀번호 수정	
	@Override
	@Transactional
	public boolean changePassword(String email, PasswordChangeRequestDto passwordChangeRequestDto) {
		//새 비밀번호 == 새 비밀번호 확인 일치하는지 확인
		//일치하지 않으면 false..
		if(!passwordChangeRequestDto.getNewPassword().equals(passwordChangeRequestDto.getNewPasswordCheck())) {
			return false;
		}
		
		//이메일로 일단 사용자 조회
		User user = userRepository.selectUserByEmail(email);
		if(user == null) {
			return false;
		}
		
		//입력한 현재 비밀번호화 db 비밀번호 일치하는지 확인 
		//일치하지 않으면 false
		if(!passwordChangeRequestDto.getCurrentPassword().equals(user.getPassword())) {
			return false;
		}
		
		//새 비밀번호로 업데이트
		user.setPassword(passwordChangeRequestDto.getNewPassword());
		
		int updatePass = userRepository.updatePassword(user);
		
		return updatePass > 0;
		
	}



	

	
}
