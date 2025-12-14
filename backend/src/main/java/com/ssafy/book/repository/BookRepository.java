package com.ssafy.book.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.book.entity.Book;

@Mapper
public interface BookRepository {
	
	//userId로 찾은 user의 가계부 상세조회
	List<Book> selectAll(String userId);
	
	//가계부 항목 상세보기 (id + userId)
	Book select(int id, String userId);
	
	int insert(Book book);
	
	int update(Book book);
	
	int delete(int id, String userId);
}

