package com.ssafy.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.book.entity.Book;
import com.ssafy.book.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Book> selectAll(String userId) {
		return bookRepository.selectAll(userId);
	}

	@Override
	public Book select(int id, String userId) {
		return bookRepository.select(id, userId);
	}

	@Override
	public int insert(Book book) {
		return bookRepository.insert(book);
	}

	@Override
	public int update(Book book) {
		return bookRepository.update(book);
	}

	@Override
	public int delete(int id, String userId) {
		return bookRepository.delete(id, userId);
	}
	
	

}
