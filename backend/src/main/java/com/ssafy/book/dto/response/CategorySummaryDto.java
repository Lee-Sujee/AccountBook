package com.ssafy.book.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CategorySummaryDto {
	private String category;
	private Long total;
}
