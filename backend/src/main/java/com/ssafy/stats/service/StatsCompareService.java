package com.ssafy.stats.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.stats.repository.StatsRepository;

@Service
public class StatsCompareService {

    @Autowired
    private StatsRepository repository;

    public Map<String, Object> compare(String productName, int userPrice) {

        Integer avgPrice = repository.findAveragePriceByMenu(productName);

        if (avgPrice == null) {
            return Map.of(
                "averagePrice", 0,
                "result", "비교할 평균 가격 데이터가 없습니다."
            );
        }

        String result;
        //사용자가 입력한 금액이 평균금액보다 비싸다면?
        if (userPrice > avgPrice * 1.1) {
            result = "비싼 소비예요 😥";
            //아니라면? -> 훨씬 저렴하게 구입
        } else if (userPrice < avgPrice * 0.9) {
            result = "아주 합리적인 소비예요 👍";
            //평균적인 금액
        } else {
            result = "적정한 소비예요 🙂";
        }

        return Map.of(
            "averagePrice", avgPrice,
            "result", result
        );
    }
}
