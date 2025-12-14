package com.ssafy.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.book.entity.Book;
import com.ssafy.book.service.BookService;
import com.ssafy.security.CustomUserDetails;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	//전체 조회 (GET/book)
	@GetMapping
	public ResponseEntity<?> getBookList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		//필요한 userId 가져오기
		String userId = userDetails.getUserId();
		List<Book> list = bookService.selectAll(userId);
		
		return ResponseEntity.ok(list);
	}
	
	//항목 세부조회 (GET/book/{id})
	@GetMapping("/{id}")
	public ResponseEntity<?> getBook(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") int id) {
		
		String userId = userDetails.getUserId();
		Book book = bookService.select(id, userId);
		
		if(book==null) {
			return new ResponseEntity<>("해당 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(book);
	}
	
	//등록 (POST/book)
	@PostMapping
	public ResponseEntity<?> insert(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Book book) {
		
		//로그인한 사용자 ID 설정
		book.setUserId(userDetails.getUserId());
		
		int result = bookService.insert(book);
		
		if(result > 0) return ResponseEntity.ok("등록 완료");
		return new ResponseEntity<>("등록 실패", HttpStatus.BAD_REQUEST);
		
	}
	
	//수정 (PUT/book/{id})
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable("id") int id,
			@RequestBody Book book){
		
		//필요한 id, userId 가져오기
		book.setId(id);
		book.setUserId(userDetails.getUserId());
		
		int result = bookService.update(book);
		
		if(result > 0) return ResponseEntity.ok("수정 완료");
		return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
	}
	
	//삭제 (DELETE/book/{id})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable("id") int id) {
		
		String userId = userDetails.getUserId();
		
		int result = bookService.delete(id, userId);
		
		if(result > 0) return ResponseEntity.ok("삭제 완료");
		return new ResponseEntity<>("삭제 실패", HttpStatus.BAD_REQUEST);
	}
}
