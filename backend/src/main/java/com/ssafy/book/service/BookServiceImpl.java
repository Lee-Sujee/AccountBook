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
import com.ssafy.common.ai.OpenAIGateway;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookStatsMapper bookStatsMapper;
	
	@Autowired
	private OpenAIGateway openAIGateway;
	
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
	
	// 수입, 지출 전달
	@Override
	public String analyzeFinances(String userId, AnalyzeFinancesRequest req) {
	    if (req == null || req.getHistory() == null || req.getHistory().isEmpty()) {
	        return "분석할 내역이 없습니다.";
	    }

	    // 합계 계산
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

	    // 지출 카테고리 TOP 5
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

	    // 프롬프트
	    String prompt = """
	            너는 개인 가계부를 분석하는 재무 코치야.
	            기간: %s
	            총수입: %d원, 총지출: %d원, 잔액: %d원, 지출비율: %d%%
	            지출 TOP5: %s
	            위 내용을 바탕으로 사용자의 소비 패턴을 요약하고, 개선 포인트와 실행 가능한 절약/수입 개선 액션을 제안해줘.
	            판단은 데이터에 근거하고, 과도한 추측은 하지 마.
	            대답은 800자를 넘지 않게 작성하고 말투는 대화형으로 적어줘.
	            답변을 다 쓴 뒤 스스로 글자 수를 가늠해서 800자를 넘으면 "불필요한 수식어/중복 문장"을 삭제하고 더 짧게 다시 써.
	            글자수는 절대로 출력하지 마.

	            예시) 당신의 이번달 지출 내역을 보니 총 수입 100만원에 지출은 쇼핑 30000원, 교통비 5500원, 식비 2000원으로
	            수입에 비해 지출이 매우 적은 편이에요! 하지만 수입이 100만원 정도면 평균에 비해 많이 적은 편이라고 할 수 있어요.
	            가능하다면 수입을 늘리는 방안을 추천드릴게요! 그 중에서도 식비가 가장 높은 걸 보니 이번달에 혹시 약속이 많았나요?
	            다음달에는 장보기를 늘리고 식비 지출을 줄인다면 더 건강한 소비습관을 가질 수 있을거에요!
	            """.formatted(period, income, expense, balance, ratio, top5.isBlank() ? "없음" : top5);

	    String systemPrompt = "You are a helpful and friendly financial assistant.";

	    return openAIGateway.chat(systemPrompt, prompt, 1200);
	}


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

	private LocalDateTime parseToLocalDateTime(String s) {
        if (s == null || s.isBlank()) return LocalDateTime.now();
        return LocalDateTime.parse(s);
    }

}
