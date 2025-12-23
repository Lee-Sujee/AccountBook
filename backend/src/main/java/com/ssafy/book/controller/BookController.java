package com.ssafy.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.book.dto.request.AnalyzeFinancesRequest;
import com.ssafy.book.dto.request.BookRequestDto;
import com.ssafy.book.entity.Book;
import com.ssafy.book.service.BookService;
import com.ssafy.security.CustomUserDetails;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;


    //예외 처리는 다 service 쪽으로 빼버렸어유
    // 전체 조회 (GET /book)
    @GetMapping
    public ResponseEntity<List<Book>> getBookList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(bookService.selectAll(userDetails.getUserId()));
    }

    // 항목 세부조회 (GET /book/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @PathVariable("id") int id) {
        return ResponseEntity.ok(bookService.select(id, userDetails.getUserId()));
    }

    // 등록 (POST /book) - DTO로 받기
    @PostMapping
    public ResponseEntity<Void> insert(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody BookRequestDto dto) {
    	bookService.insert(userDetails.getUserId(), dto);
    	
    	return ResponseEntity.ok().build();
    }

    // 수정 (PUT /book/{id}) - DTO로 받기
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable("id") int id,
                                    @RequestBody BookRequestDto dto) {
    	bookService.update(id, userDetails.getUserId(), dto);
    	return ResponseEntity.ok().build();
    }

    // 삭제 (DELETE /book/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable("id") int id) {

        bookService.delete(id, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 카테고리별 수입/지출 요약(분석)
    @GetMapping("/summary")
    public ResponseEntity<?> getBookSummary(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestParam String type,
                                                @RequestParam int year,
                                                @RequestParam int month) {

        return ResponseEntity.ok(bookService.getCategorySummary(userDetails.getUserId(), type, year, month));
    }
    
    // AI -> 수입/지출 분석
    @PostMapping("/analyze-finances")
    public ResponseEntity<String> analyzeFinances(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody AnalyzeFinancesRequest req
    ) {
        String userId = user.getUserId();
        String analysis = bookService.analyzeFinances(userId, req);
        return ResponseEntity.ok(analysis);

    }
}
