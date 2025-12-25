package com.ssafy.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.stats.service.StatsInitService;

@RestController
@RequestMapping("/admin/product-price")
public class StatsAdminController {
	
	@Autowired
	private StatsInitService service;
	
	@PostMapping("/init/test")
	public ResponseEntity<?> initTest() throws Exception {
	    service.initFromCsvForTest();
	    return ResponseEntity.ok("로컬 CSV 초기화 완료");
	}

}