package org.eightbit.damdda.generativeai.service;

import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.generativeai.client.AIContentGenerationClient;
import org.eightbit.damdda.generativeai.dto.AIProjectDescriptionDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class GenerativeAIServiceImpl {

    // 생성형 AI 프로젝트 설명을 요청하는 클라이언트
    private final AIContentGenerationClient aiContentGenerationClient;

    public GenerativeAIServiceImpl(AIContentGenerationClient aiContentGenerationClient) {
        this.aiContentGenerationClient = aiContentGenerationClient;
    }

    /**
     * AI를 통해 프로젝트 설명을 생성하는 메서드
     *
     * @param aiProjectDescriptionDTO 프로젝트 설명 생성을 위한 데이터 (제목, 카테고리, 태그, 설명 포함)
     * @return Mono<String> 생성된 프로젝트 설명 (비동기 처리)
     */
    public Mono<String> generateProjectDescription(AIProjectDescriptionDTO aiProjectDescriptionDTO) {

        // AI API에 요청할 JSON 형식의 본문 생성
        String requestBody = String.format(
                "{\"title\":\"%s\",\"category\":\"%s\",\"tags\":[\"%s\"],\"description\":\"%s\"}",
                aiProjectDescriptionDTO.getTitle(),
                aiProjectDescriptionDTO.getCategory(),
                String.join("\",\"", aiProjectDescriptionDTO.getTags()),
                aiProjectDescriptionDTO.getDescription()
        );

        log.debug("[generativeai] Generated request body for AI API: {}", requestBody);

        // AIContentGenerationClient를 통해 외부 API 호출
        return aiContentGenerationClient.requestProjectDescription(requestBody)
                .onErrorResume(e -> {
                    log.error("[generativeai] Error while generating project description: {}", e.getMessage(), e);
                    // 오류 발생 시 처리, 새로운 RuntimeException으로 변환하여 반환
                    return Mono.error(new RuntimeException("Failed to generate project description", e));
                });
    }
}
