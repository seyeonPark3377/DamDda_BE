package org.eightbit.damdda.project.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WritingProjectDTO {
    private Long id;
    private String title;       // 프로젝트 명
}
