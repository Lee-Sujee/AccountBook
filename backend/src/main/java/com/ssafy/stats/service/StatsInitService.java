package com.ssafy.stats.service;

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

    public void initFromCsv(MultipartFile file) throws Exception {

        // CSV 조사일 포맷이 "YYYY-MM-DD"면 이대로 OK
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        try (
            Reader reader = new InputStreamReader(
                file.getInputStream(),
                Charset.forName("CP949")
            );
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader("조사일", "상품명", "업태", "평균판매가격")
                .withSkipHeaderRecord()
                .withTrim())
        ) {
            // repository.deleteAll();

            for (CSVRecord r : csvParser) {

                String menu = r.get("상품명").trim();
                String category = r.get("업태").trim();
                String priceStr = r.get("평균판매가격").trim();
                String dateStr = r.get("조사일").trim();

                // 방어 로직
                if (menu.isEmpty() || category.isEmpty() || priceStr.isEmpty() || dateStr.isEmpty()) {
                    continue;
                }

                Stats stats = new Stats();
                stats.setMenu(menu);
                stats.setCategory(category);
                stats.setPrice(Integer.parseInt(priceStr));
                stats.setSurveyDate(LocalDate.parse(dateStr, formatter));

                //upsert (menu, category) 조합당 최신 1건만 유지
                repository.upsertLatest(stats);
            }
        }
    }
}
