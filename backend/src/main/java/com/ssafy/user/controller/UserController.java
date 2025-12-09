package com.ssafy.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.security.CustomUserDetails;
import com.ssafy.user.dto.request.LoginRequestDto;
import com.ssafy.user.dto.request.PasswordChangeRequestDto;
import com.ssafy.user.dto.request.SignUpRequestDto;
import com.ssafy.user.dto.request.UserUpdateRequestDto;
import com.ssafy.user.dto.response.LoginResponseDto;
import com.ssafy.user.dto.response.SignUpResponseDto;
import com.ssafy.user.dto.response.UserResponseDto;
import com.ssafy.user.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	// 회원가입 POST /user
	@PostMapping("/signUp")
	public ResponseEntity<?> regist(@RequestBody SignUpRequestDto signUpRequestDto) throws Exception {
		SignUpResponseDto signUpResponseDto = userService.signUp(signUpRequestDto);
		if (signUpResponseDto == null)
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

		return ResponseEntity.ok(signUpResponseDto);

	}

	// 로그인 POST /user
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
		LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
		if (loginResponseDto == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return ResponseEntity.ok(loginResponseDto);
	}

	// 마이페이지
	@GetMapping("/myPage")
	public ResponseEntity<?> getMyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {

		UserResponseDto user = userService.getMyPage(userDetails.getUserId());

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
		}

		return ResponseEntity.ok(user);
	}

	// 회원정보 수정
	@PutMapping("/myPage")
	public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody UserUpdateRequestDto dto) throws Exception {

		String userId = userDetails.getUserId();
		UserResponseDto updatedUser = userService.update(userId, dto);

		if (updatedUser != null) {
			return ResponseEntity.ok(updatedUser);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보 수정 실패: 일치하는 사용자를 찾을 수 없습니다.");
	}

	// 비밀번호 수정
	@PutMapping("/password")
	public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestBody PasswordChangeRequestDto passwordChangeRequestDto) throws Exception {
		boolean isChanged = userService.changePassword(userDetails.getUserId(), passwordChangeRequestDto);

		if (isChanged) {
			return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
		}
		return new ResponseEntity<>("비밀번호 변경 실패: 현재 비밀번호 불일치 또는 새 비밀번호 확인 오류", HttpStatus.UNAUTHORIZED);
	}
}
