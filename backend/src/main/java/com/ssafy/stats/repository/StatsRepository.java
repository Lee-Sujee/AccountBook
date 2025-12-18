package com.ssafy.stats.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.stats.entity.Stats;

@Mapper
public interface StatsRepository {

    void deleteAll();

    void insert(Stats stats);

    //상품명 검색
    List<Stats> findByMenuContaining(@Param("keyword") String keyword);
    
    //평균가격 조회
    Integer findAveragePriceByMenu(@Param("menu") String menu);
}

