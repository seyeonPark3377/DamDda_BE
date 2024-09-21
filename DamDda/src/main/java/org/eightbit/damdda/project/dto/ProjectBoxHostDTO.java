package org.eightbit.damdda.project.dto;


import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectBoxHostDTO {
    private String title;   // 프로젝트 명
    private String description; //프로젝트 설명
    private String thumbnailUrl;    //썸네일 url
    private Long fundsReceive;  //받은 후원금
    private Long targetFunding; //목표 후원금
    private String nickName;    //프로젝트 담당자 닉네임
    private Timestamp endDate;  //마감일
    private Integer approval;    //관리자 승인여부
}