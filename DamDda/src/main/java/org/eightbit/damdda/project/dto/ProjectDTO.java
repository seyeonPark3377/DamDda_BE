package org.eightbit.damdda.project.dto;


import lombok.*;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Category;

import java.sql.Timestamp;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDTO {

    private Long id;
    private Member member;
    private String title;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long targetFunding;
    private Category category;
    private Long fundsReceive;
    private Long supporterCount;
    private Long viewCount;
    private Long likeCount;
    private String thumbnailUrl;
    private Timestamp createdAt;
    private Timestamp deletedAt;
}
