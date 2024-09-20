package org.eightbit.damdda.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDetailHostDTO {
    private String title;
    private String description;
    private String descriptionDetail;     // 프로젝트 상세설명
    private Long fundsReceive;
    private Long targetFunding;
    private String nickName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long supporterCnt;
    private Long likerCnt;
    private String category;
    private String Approval;    //관리자 승인여부
    private String rejectMessage;   //거절메시지
//    private List<projectImageDTO> imgs;
}
