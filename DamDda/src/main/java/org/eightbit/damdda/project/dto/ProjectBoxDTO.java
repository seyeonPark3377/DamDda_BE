package org.eightbit.damdda.project.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectBoxDTO {
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long fundsReceive;
    private Long targetFunding;
    private String nickName;
    private LocalDateTime endDate;
    private boolean Liked;
}
