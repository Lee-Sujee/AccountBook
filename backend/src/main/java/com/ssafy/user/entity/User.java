package com.ssafy.user.entity;

import lombok.Data;

@Data
public class User {
	private String id;
	private String email;
	private String password;
	private String name;
	private Integer age;
	private String job;
	private String created_at;
}
