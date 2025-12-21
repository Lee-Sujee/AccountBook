package com.ssafy.book.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.book.dto.response.CategorySummaryDto;

@Mapper
public interface BookStatsMapper {
	
	List<CategorySummaryDto> selectCategorySummary(
			@Param("userId") String userId, 
			@Param("type") String type,
			@Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);
}
