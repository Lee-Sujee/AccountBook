package com.ssafy.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.user.dto.request.LoginRequestDto;
import com.ssafy.user.dto.request.SignUpRequestDto;
import com.ssafy.user.dto.response.LoginResponseDto;
import com.ssafy.user.dto.response.SignUpResponseDto;
import com.ssafy.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// 회원가입 POST /user
	@PostMapping("/signUp")
	public ResponseEntity<?> regist(@RequestBody SignUpRequestDto signUpRequestDto) throws Exception {
		SignUpResponseDto signUpResponseDto = userService.signUp(signUpRequestDto);
		if(signUpResponseDto == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return ResponseEntity.ok(signUpResponseDto);
	}
	
	// 로그인 POST /user
	@PostMapping("")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
		LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
		if(loginResponseDto == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return ResponseEntity.ok(loginResponseDto);
	}
	
//	@PutMapping("/{id}")
//	public ResponseEntity<?> update(@PathVariable String id, @RequestBody User user) throws Exception {
//		user.setUserId(id);
//		int result = userService.updateUser(user);
//		
//		if(result != 1) {
//			//실패함
//			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//		}
//		//성공함
//		loginUser = userService.getUserById(id);
//		System.out.println(loginUser);
//		return ResponseEntity.ok(result);
//	}
}  
