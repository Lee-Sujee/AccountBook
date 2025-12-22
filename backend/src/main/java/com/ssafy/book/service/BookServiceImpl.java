package com.ssafy.book.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.book.dto.response.CategorySummaryDto;
import com.ssafy.book.entity.Book;
import com.ssafy.book.mapper.BookStatsMapper;
import com.ssafy.book.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookStatsMapper bookStatsMapper;
	
	@Autowired
	private OpenAIService openAIService;
	
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

	@Override
	public List<CategorySummaryDto> getCategorySummary(
			String userId, 
			String type, 
			int year, 
			int month) {
		LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0, 0);
		LocalDateTime end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth())
				.withHour(23).withMinute(59).withSecond(59);
		
		return bookStatsMapper.selectCategorySummary(userId, type, start, end);
	}

	@Override
	public String analyzeFinances(String userId, int income, int expense) {
		return openAIService.analyzeFinances(income, expense);
	}
	
	

}
