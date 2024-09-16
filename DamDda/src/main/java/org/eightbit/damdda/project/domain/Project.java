package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project extends DateEntity {

    //@ManyToOne
    //private Member member;
    //@ManyToOne
    //private Category category;
    //@ManyToMany
    //private Set<Tag> tags;

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
    private Timestamp submitAt;
}
