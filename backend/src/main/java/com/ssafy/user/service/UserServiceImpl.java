package com.ssafy.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.security.JwtUtil;
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
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	
	//회원가입 처리 (마지막에 insert로 회원가입 정보 보내기)
	@Override
	@Transactional
	public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
		if(!signUpRequestDto.getPassword().equals(signUpRequestDto.getPasswordCheck())) {
			return null;
		}
		
		 String hashedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
		 signUpRequestDto.setPassword(hashedPassword);
		
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

		if(user == null) return null;
		
		boolean isMatch = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
	    if (!isMatch) return null;
		
	    String token = jwtUtil.generateToken(user.getId());
	    

		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setId(user.getId());
		loginResponseDto.setEmail(user.getEmail());
		loginResponseDto.setName(user.getName());
		loginResponseDto.setToken(token); 
		return loginResponseDto;
	}

	
	//사용자 정보 수정
	@Override
	@Transactional
	public UserResponseDto update(String userId, UserUpdateRequestDto dto) {

	    // userId 기반으로 유저 조회
	    User user = userRepository.selectUserById(userId);

	    if (user == null) {
	        return null;
	    }

	    // 수정 값 반영
	    user.setName(dto.getName());
	    user.setAge(dto.getAge());
	    user.setJob(dto.getJob());

	    // DB 업데이트
	    int updated = userRepository.updateUserInfo(user);

	    // 성공 시 응답 DTO 반환
	    if (updated > 0) {
	        UserResponseDto response = new UserResponseDto();
	        response.setEmail(user.getEmail());
	        response.setName(user.getName());
	        response.setAge(user.getAge());
	        response.setJob(user.getJob());
	        return response;
	    }

	    return null;
	}


	
	//비밀번호 수정	
	@Override
	@Transactional
	public boolean changePassword(String userId, PasswordChangeRequestDto passwordChangeRequestDto) {
		//새 비밀번호 == 새 비밀번호 확인 일치하는지 확인
		//일치하지 않으면 false..
		if(!passwordChangeRequestDto.getNewPassword().equals(passwordChangeRequestDto.getNewPasswordCheck())) {
			return false;
		}
		
		//id로 일단 사용자 조회
		User user = userRepository.selectUserById(userId);
		if(user == null) {
			return false;
		}
		
		//입력한 현재 비밀번호화 db 비밀번호 일치하는지 확인 
		//일치하지 않으면 false
		if(!passwordEncoder.matches(passwordChangeRequestDto.getCurrentPassword(), user.getPassword())) {
			return false;
		}
		
		//새 비밀번호로 업데이트
		String newHashedPw = passwordEncoder.encode(passwordChangeRequestDto.getNewPassword());
		user.setPassword(newHashedPw);
		
		int updatePass = userRepository.updatePassword(user);
		
		return updatePass > 0;
		
	}


	@Override
	public UserResponseDto getMyPage(String userId) {
		User user = userRepository.selectUserById(userId);

	    if (user == null) {
	        return null;
	    }

	    UserResponseDto userResponseDto = new UserResponseDto();
	    userResponseDto.setEmail(user.getEmail());
	    userResponseDto.setName(user.getName());
	    userResponseDto.setAge(user.getAge());
	    userResponseDto.setJob(user.getJob());

	    return userResponseDto;
	}



	

	
}
