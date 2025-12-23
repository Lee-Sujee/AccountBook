package com.ssafy.book.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.book.dto.request.AnalyzeFinancesRequest;
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
	public List<Book> selectByMonth(String userId, int year, int month) {
	    LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
	    LocalDateTime end = start.plusMonths(1);
	    return bookRepository.selectByMonth(userId, start, end);
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
	public String analyzeFinances(String userId, AnalyzeFinancesRequest req) {
	    if (req == null || req.getHistory() == null || req.getHistory().isEmpty()) {
	        return "분석할 내역이 없습니다.";
	    }

	    // 1) 합계 계산
	    int income = req.getHistory().stream()
	            .filter(h -> "income".equals(h.getType()))
	            .mapToInt(h -> h.getAmount() == null ? 0 : h.getAmount())
	            .sum();

	    int expense = req.getHistory().stream()
	            .filter(h -> "expense".equals(h.getType()))
	            .mapToInt(h -> h.getAmount() == null ? 0 : h.getAmount())
	            .sum();

	    int balance = income - expense;
	    int ratio = income == 0 ? 0 : (int) Math.round((expense * 100.0) / income);

	    // 2) 지출 카테고리 TOP 5
	    Map<String, Integer> expenseByCategory = req.getHistory().stream()
	            .filter(h -> "expense".equals(h.getType()))
	            .collect(Collectors.groupingBy(
	                    h -> (h.getCategory() == null || h.getCategory().isBlank()) ? "기타" : h.getCategory(),
	                    Collectors.summingInt(h -> h.getAmount() == null ? 0 : h.getAmount())
	            ));

	    String top5 = expenseByCategory.entrySet().stream()
	            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
	            .limit(5)
	            .map(e -> e.getKey() + " " + e.getValue() + "원")
	            .collect(Collectors.joining(", "));

	    String period = (req.getYear() != null && req.getMonth() != null)
	            ? req.getYear() + "년 " + req.getMonth() + "월"
	            : "이번 기간";

	    // 3) AI에게 줄 요약 텍스트(=프롬프트)
	    String prompt = """
	            너는 개인 재무 코치야.
	            기간: %s
	            총수입: %d원, 총지출: %d원, 잔액: %d원, 지출비율: %d%%
	            지출 TOP5: %s
	            위 내용을 바탕으로 1) 핵심 요약 2) 리스크/문제점 3) 실행 가능한 개선 액션 3가지를 한국어로 작성해줘.
	            400자 내외로 500자를 넘지 말고, 줄바꿈으로 보기 좋게 작성해줘.
	            """.formatted(period, income, expense, balance, ratio, top5.isBlank() ? "없음" : top5);

	    // OpenAIService에 "prompt를 받는 메서드" 하나 만들고 그걸 호출하는 걸 추천
	    return openAIService.analyzeFinances(prompt);
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
