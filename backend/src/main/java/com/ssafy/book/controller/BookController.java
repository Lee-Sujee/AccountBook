package com.ssafy.book.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ResponseEntity<?> getBookList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        String userId = userDetails.getUserId();

        // year/month 없으면 기존대로 전체
        if (year == null || month == null) {
            List<Book> list = bookService.selectAll(userId);
            return ResponseEntity.ok(list);
        }

        // year/month 있으면 해당 월만
        List<Book> list = bookService.selectByMonth(userId, year, month);
        return ResponseEntity.ok(list);
    }

    // 항목 세부조회 (GET /book/{id})
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @PathVariable("id") int id) {

        String userId = userDetails.getUserId();
        Book book = bookService.select(id, userId);

        if (book == null) {
            return new ResponseEntity<>("해당 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(book);
    }

    // ✅ 등록 (POST /book) - DTO로 받기
    @PostMapping
    public ResponseEntity<?> insert(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody BookRequestDto dto) {

        Book book = new Book();
        book.setUserId(userDetails.getUserId());
        book.setCategory(dto.getCategory());
        book.setContent(dto.getContent());
        book.setType(dto.getType());
        book.setAmount(dto.getAmount());
        book.setMemo(dto.getMemo());

        // ✅ 사용자가 입력한 날짜를 createdAt에 저장
        book.setCreatedAt(parseToLocalDateTime(dto.getCreatedAt()));

        int result = bookService.insert(book);

        if (result > 0) return ResponseEntity.ok("등록 완료");
        return new ResponseEntity<>("등록 실패", HttpStatus.BAD_REQUEST);
    }

    // ✅ 수정 (PUT /book/{id}) - DTO로 받기
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable("id") int id,
                                    @RequestBody BookRequestDto dto) {

        Book book = new Book();
        book.setId(id);
        book.setUserId(userDetails.getUserId());
        book.setCategory(dto.getCategory());
        book.setContent(dto.getContent());
        book.setType(dto.getType());
        book.setAmount(dto.getAmount());
        book.setMemo(dto.getMemo());

        // ✅ 수정 시에도 사용자가 입력한 날짜로 업데이트
        book.setCreatedAt(parseToLocalDateTime(dto.getCreatedAt()));

        int result = bookService.update(book);

        if (result > 0) return ResponseEntity.ok("수정 완료");
        return new ResponseEntity<>("수정 실패", HttpStatus.BAD_REQUEST);
    }

    // 삭제 (DELETE /book/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable("id") int id) {

        String userId = userDetails.getUserId();
        int result = bookService.delete(id, userId);

        if (result > 0) return ResponseEntity.ok("삭제 완료");
        return new ResponseEntity<>("삭제 실패", HttpStatus.BAD_REQUEST);
    }

    // 카테고리별 수입/지출 요약(분석)
    @GetMapping("/summary")
    public ResponseEntity<?> getBookSummary(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestParam String type,
                                                @RequestParam int year,
                                                @RequestParam int month) {

        String userId = userDetails.getUserId();
        return ResponseEntity.ok(bookService.getCategorySummary(userId, type, year, month));
    }

    // ✅ "2025-12-21T13:20" 같은 문자열 -> LocalDateTime
    private LocalDateTime parseToLocalDateTime(String s) {
        if (s == null || s.isBlank()) {
            // 날짜 안 보내면 현재시간(원하면 예외 처리로 바꿔도 됨)
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(s); // datetime-local 포맷과 호환
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
