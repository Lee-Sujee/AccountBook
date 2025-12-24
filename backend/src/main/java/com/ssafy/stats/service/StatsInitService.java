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
