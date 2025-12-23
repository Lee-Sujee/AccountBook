package com.ssafy.book.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.book.dto.request.BookRequestDto;
import com.ssafy.book.dto.response.CategorySummaryDto;
import com.ssafy.book.entity.Book;
import com.ssafy.book.repository.BookRepository;
import com.ssafy.book.stats.repository.BookStatsMapper;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookStatsMapper bookStatsMapper;
	
	@Autowired
	private OpenAIService openAIService;
	
	@Override
	@Transactional
	public List<Book> selectAll(String userId) {
		return bookRepository.selectAll(userId);
	}

	@Override
	@Transactional
	public Book select(int id, String userId) {
		Book book = bookRepository.select(id, userId);
		if(book == null) {
			throw new RuntimeException("해당 항목을 찾을 수 없습니다.");
		}
		return book;
	}

	@Override
	@Transactional
	public void insert(String userId, BookRequestDto dto) {
		Book book = convertToEntity(userId, dto);
		int result = bookRepository.insert(book);
		if (result <= 0) throw new RuntimeException("가계부 등록에 실패했습니다.");
	}

	@Override
	@Transactional
	public void update(int id, String userId, BookRequestDto dto) {
		this.select(id, userId);
		
		Book book = convertToEntity(userId, dto);
		book.setId(id);
		
		int result = bookRepository.update(book);
		if(result <= 0) throw new RuntimeException("가계부 수정에 실패했습니다.");
	}
	
	@Override
	@Transactional
	public void delete(int id, String userId) {
		//user 있는지 확인
		this.select(id, userId);
		
		int result = bookRepository.delete(id, userId);
		if (result <= 0) throw new RuntimeException("가계부 삭제에 실패했습니다.");
	}
	
	@Override
	@Transactional
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
	
	//수입, 지출 전달
	@Override
	public String analyzeFinances(String userId, int income, int expense) {
		return openAIService.analyzeFinances(income, expense);
	}
	
	//dto -> db에 저장할 수 있는 엔티티로 바꿈
	private Book convertToEntity(String userId, BookRequestDto dto) {
		Book book = new Book();
		
		book.setUserId(userId);
		book.setCategory(dto.getCategory());
		book.setContent(dto.getContent());
		book.setType(dto.getType());
		book.setAmount(dto.getAmount());
		book.setMemo(dto.getMemo());
		book.setCreatedAt(parseToLocalDateTime(dto.getCreatedAt()));
		return book;
	}
	
	//날짜 형식 변환
	private LocalDateTime parseToLocalDateTime(String s) {
        if (s == null || s.isBlank()) return LocalDateTime.now();
        return LocalDateTime.parse(s);
    }

}
