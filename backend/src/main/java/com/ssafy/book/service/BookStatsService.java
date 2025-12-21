package com.ssafy.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.book.dto.response.CategorySummaryDto;
import com.ssafy.book.mapper.BookStatsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookStatsService {
	private final BookStatsMapper bookStatsMapper;
	
	public List<CategorySummaryDto> getCategorySummary(String userId, String type) {
		return bookStatsMapper.selectCategorySummary(userId, type);
	}
}
