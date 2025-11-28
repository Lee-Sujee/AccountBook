package com.ssafy.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
//	@Autowired
//	private UserService userService;
//	
//	private User loginUser;
//	
//	// 회원가입 POST /user
//	@PostMapping("/signUp")
//	public ResponseEntity<?> regist(User user) throws Exception {
//		int result = userService.regist(user);
//		if(result != 1) {
//			// 회원가입실패
//			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//		}
//		return ResponseEntity.ok(result);
//	}
//	
//	// 로그인 POST /user
//	@PostMapping("")
//	public ResponseEntity<?> login(User user) throws Exception {
//		String id = user.getUserId();
//		String pw = user.getUserPassword();
//		loginUser = userService.getUserById(id);
//		if(loginUser == null || !loginUser.getUserPassword().equals(pw)) {
//			// 로그인 실패
//			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
//		}
//		// 로그인 성공
//		return ResponseEntity.ok(loginUser);
//	}
//	
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
