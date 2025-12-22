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
	
//	@PostMapping("/init")
//	public ResponseEntity<?> init(@RequestParam("file") MultipartFile file) {
//	    System.out.println("🔥 ProductPriceAdminController 진입");
//	    System.out.println("📦 파일 이름 = " + file.getOriginalFilename());
//
//	    try {
//	        service.initFromCsv(file);
//	        return ResponseEntity.ok("product-price 초기화 완료!");
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.badRequest().body("CSV 처리 실패");
//	    }
//	}

}