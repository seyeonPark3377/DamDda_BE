package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project extends DateEntity {

    @ManyToOne
    private Member member;
    @ManyToOne
    private Category category;

    private String title;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long targetFunding;
    private Long fundsReceive;
    private Long supporterCount;
    private Long viewCount;
    private Long likeCount;
    private String thumbnailUrl;
}
