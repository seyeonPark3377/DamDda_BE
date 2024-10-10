package org.eightbit.damdda.generativeai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * webClient: WebClient 인스턴스를 생성하는 메서드
     *
     * @return WebClient 외부 API와 비동기 통신을 위한 WebClient 인스턴스 반환
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                // 기본 URL 설정 (외부 API 서버 주소)
                .baseUrl("http://211.188.48.96:5000")
                // WebClient 인스턴스 생성 및 반환
                .build();
    }
}
