package com.ssafy.exception;

public class ChallengeNotFoundException extends RuntimeException {
	public ChallengeNotFoundException() {
		super("해당 챌린지를 찾을 수 없습니다.");
	}
	
	public ChallengeNotFoundException(String message) {
		super(message);
	}
}
