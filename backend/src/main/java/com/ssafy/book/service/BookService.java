package com.ssafy.book.service;

import java.util.List;

import com.ssafy.book.entity.Book;

public interface BookService {
	List<Book> selectAll(String userId);
	
	Book select(int id, String userId);
	
	int insert(Book book);
	
	int update(Book book);
	
	int delete(int id, String userId);
}
