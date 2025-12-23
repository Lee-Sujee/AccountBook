package com.ssafy.book.service;

import java.util.List;

import com.ssafy.book.dto.request.AnalyzeFinancesRequest;
import com.ssafy.book.dto.response.CategorySummaryDto;
import com.ssafy.book.entity.Book;

public interface BookService {
	List<Book> selectAll(String userId);
	
	Book select(int id, String userId);
	
	List<Book> selectByMonth(String userId, int year, int month);
	
	int insert(Book book);
	
	int update(Book book);
	
	int delete(int id, String userId);
	
	List<CategorySummaryDto> getCategorySummary(String userId, String type, int year, int month);

	public String analyzeFinances(String userId, AnalyzeFinancesRequest req);

}
