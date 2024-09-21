package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.member.domain.Member;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "liked_projects")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikedProject extends BaseEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private Project project;

    @CreatedDate
    @Column(updatable = true)
    private Timestamp likedAt;
}
