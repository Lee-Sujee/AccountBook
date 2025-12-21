package com.ssafy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	// 403 Forbidden
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<Void> handleForbiddenException(ForbiddenException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	// 404 Not Found
	@ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Void> handleCommentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
	
	// 404 Not Found
	@ExceptionHandler(ChallengeNotFoundException.class)
	public ResponseEntity<Void> handleCommentNotFoundException(ChallengeNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	//그 외-서버 오류 @ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleException(Exception e) {
        e.printStackTrace(); // 로그용
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
