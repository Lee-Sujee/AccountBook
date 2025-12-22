package com.ssafy.stats.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.stats.entity.Stats;
import com.ssafy.stats.repository.StatsRepository;

@Service
public class StatsInitService {

    @Autowired
    private StatsRepository repository;

//    public void initFromCsv(MultipartFile file) throws Exception {
//
//        // 1️⃣ 파일 유효성 체크 (실무 필수)
//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
//        }
//
//        // 2️⃣ 날짜 포맷
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
//
//        try (
//            Reader reader = new InputStreamReader(
//                file.getInputStream(),
//                Charset.forName("CP949")   // 한글 CSV 대응
//            );
//            CSVParser csvParser = new CSVParser(
//                reader,
//                CSVFormat.DEFAULT
//                    .withHeader("조사일", "상품명", "업태", "평균판매가격")
//                    .withSkipHeaderRecord()
//                    .withTrim()
//            )
//        ) {
//            // 필요 시 전체 초기화
//            // repository.deleteAll();
//
//            for (CSVRecord r : csvParser) {
//
//                // 3️⃣ 컬럼 추출
//                String menu = r.get("상품명");
//                String category = r.get("업태");
//                String priceStr = r.get("평균판매가격");
//                String dateStr = r.get("조사일");
//
//                // 4️⃣ 방어 로직 (빈 값 / 깨진 row)
//                if (menu == null || category == null || priceStr == null || dateStr == null) {
//                    continue;
//                }
//
//                menu = menu.trim();
//                category = category.trim();
//                priceStr = priceStr.trim();
//                dateStr = dateStr.trim();
//
//                if (menu.isEmpty() || category.isEmpty() || priceStr.isEmpty() || dateStr.isEmpty()) {
//                    continue;
//                }
//
//                int price;
//                LocalDate surveyDate;
//
//                try {
//                    price = Integer.parseInt(priceStr.replace(",", ""));
//                    surveyDate = LocalDate.parse(dateStr, formatter);
//                } catch (Exception e) {
//                    // 숫자/날짜 깨진 row는 skip
//                    continue;
//                }
//
//                // 5️⃣ 엔티티 생성
//                Stats stats = new Stats();
//                stats.setMenu(menu);
//                stats.setCategory(category);
//                stats.setPrice(price);
//                stats.setSurveyDate(surveyDate);
//
//                // 6️⃣ upsert (menu + category 기준 최신 1건 유지)
//                repository.upsertLatest(stats);
//            }
//        }
//    }
    
    public void initFromCsvForTest() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        try (
            InputStream is = getClass().getResourceAsStream("/data/stats.csv");
            Reader reader = new InputStreamReader(is, Charset.forName("CP949"));
            CSVParser csvParser = new CSVParser(
                reader,
                CSVFormat.DEFAULT
                    .withHeader("조사일", "상품명", "업태", "평균판매가격")
                    .withSkipHeaderRecord()
                    .withTrim()
            )
        ) {
            for (CSVRecord r : csvParser) {

                String menu = r.get("상품명").trim();
                String category = r.get("업태").trim();
                int price = Integer.parseInt(r.get("평균판매가격").replace(",", ""));
                LocalDate survey_date = LocalDate.parse(r.get("조사일"), formatter);

                Stats stats = new Stats();
                stats.setMenu(menu);
                stats.setCategory(category);
                stats.setPrice(price);
                stats.setSurveyDate(survey_date);

                repository.upsertLatest(stats);
            }
            System.out.println("for문끝났다");
        }
    }
}
