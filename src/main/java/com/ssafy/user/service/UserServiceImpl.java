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
	public UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto) {
		//email이 DTO에 저장되어 있어야 수정이 가능
		//없으면? 회원가입도 안 되어 있는데 어떻게 수정해!
//		int existEmail = userDao.updateUserInfo(userUpdateRequestDto);
//		
//		if(existEmail > 0) {
//			//만약 있다면 (값이 무조건 0 이상이겠지? 이렇게 하는 거 맞겠지?)
//			return new UserResponseDto(); //응답 DTO 반환
//		}
//		return null; //업데이트 안 되면 null 반환
		return null;
	}

	
	//비밀번호 수정	
	@Override
	@Transactional
	public boolean changePassword(String email, PasswordChangeRequestDto passwordChangeRequestDto) {
		return false;
		
	}

	

	
}
