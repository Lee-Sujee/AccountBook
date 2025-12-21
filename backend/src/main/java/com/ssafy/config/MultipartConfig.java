package com.ssafy.config;

// 🚨 javax.servlet 대신 jakarta.servlet을 사용해야 합니다.
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
        
        // 1. 개별 파일 최대 크기 설정 (10MB)
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); 
        
        // 2. 전체 요청 최대 크기 설정 (10MB)
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        
        // Tomcat에 해당 설정을 적용하는 MultipartConfigElement 객체를 반환
        return factory.createMultipartConfig(); // 이제 jakarta 타입 반환 -> jakarta 변수에 저장
    }
}