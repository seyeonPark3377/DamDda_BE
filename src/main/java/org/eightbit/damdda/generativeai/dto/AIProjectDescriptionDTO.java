package org.eightbit.damdda.generativeai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIProjectDescriptionDTO {

    // 프로젝트 제목
    private String title;

    // 프로젝트 카테고리
    private String category;

    // 프로젝트 태그 목록 (여러 개의 태그를 배열로 받음)
    private String[] tags;

    // 프로젝트에 대한 설명
    private String description;

}
