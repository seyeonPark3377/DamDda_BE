package org.eightbit.damdda.project.dto;


import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectBoxHostDTO {
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long fundsReceive;
    private Long targetFunding;
    private String nickName;
    private LocalDateTime endDate;
    private String Approval;
}
