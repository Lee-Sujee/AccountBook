package com.ssafy.config;

import jakarta.servlet.MultipartConfigElement; 
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 개별 파일 최대 크기 설정 (10MB)
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); 
        
        // 전체 요청 최대 크기 설정 (10MB)
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        
        return factory.createMultipartConfig(); 
    }
}