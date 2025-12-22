package com.ssafy.stats.runner;

import org.springframework.stereotype.Component;

import com.ssafy.stats.service.StatsInitService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatsInitRunner {

    private final StatsInitService service;

    @PostConstruct
    public void init() throws Exception {
        service.initFromCsvForTest();
    }
}
