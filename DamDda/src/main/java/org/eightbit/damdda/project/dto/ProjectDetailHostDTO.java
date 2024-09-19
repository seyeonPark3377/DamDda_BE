package org.eightbit.damdda.project.dto;

import java.time.LocalDateTime;


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
