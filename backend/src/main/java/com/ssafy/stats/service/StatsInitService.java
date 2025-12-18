package com.ssafy.stats.service;

import java.io.InputStreamReader;
import java.io.Reader;

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

        try (
            Reader reader = new InputStreamReader(
                file.getInputStream(),
                java.nio.charset.Charset.forName("CP949")
            );
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withHeader("조사일", "상품명", "업태", "평균판매가격")
                .withSkipHeaderRecord()
                .withTrim())
        ) {
            repository.deleteAll();

            for (CSVRecord csvRecord : csvParser) {

                Stats stats = new Stats();
                stats.setMenu(csvRecord.get("상품명").trim());
                stats.setCategory(csvRecord.get("업태").trim());
                stats.setPrice(
                    Integer.parseInt(csvRecord.get("평균판매가격").trim())
                );

                System.out.println(
                    "저장 대상: " +
                    stats.getMenu() + " / " +
                    stats.getCategory() + " / " +
                    stats.getPrice()
                );

                repository.insert(stats);
            }
        }
    }
}
