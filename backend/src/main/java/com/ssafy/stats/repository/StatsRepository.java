package com.ssafy.stats.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.stats.entity.Stats;

@Mapper
public interface StatsRepository {

    // 필요하면 유지 (CSV 업로드할 때 전체 갈아끼우기 할 경우)
    void deleteAll();

    // (menu, category) 유니크 기반 최신만 유지
    void upsertLatest(Stats stats);

    // 비교용: 특정 상품 + 업태 1건
    Stats findByMenuAndCategory(@Param("menu") String menu,
                                @Param("category") String category);

    // 평균계산기용: 특정 상품(menu)에 대해 업태별 1개씩 리스트
    // (DB에 (menu, category) 1행만 유지되므로 그대로 조회하면 됨)
    List<Stats> findAllByMenuLike(@Param("keyword") String keyword);
}
