package com.ssafy.stats.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.stats.entity.Stats;

@Mapper
public interface StatsRepository {

    void deleteAll();

    void upsertLatest(Stats stats);

    Stats findByMenuAndCategory(@Param("menu") String menu,
                                @Param("category") String category);

    List<Stats> findAllByMenuLike(@Param("keyword") String keyword);
    
    List<String> findAllDistinctMenus();
    
}