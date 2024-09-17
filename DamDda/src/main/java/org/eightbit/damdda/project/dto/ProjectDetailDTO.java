package org.eightbit.damdda.project.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDetailDTO {
    private String title;
    private String description;
    private Long fundsReceive;
    private Long targetFunding;
    private String nickName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long supporterCnt;
    private Long likeCnt;
    private String category;
    //    private List<projectImageDTO> imgs;
    private List<TagDTO> tags;

}
