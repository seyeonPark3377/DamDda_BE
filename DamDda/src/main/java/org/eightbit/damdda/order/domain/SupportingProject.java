package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Project;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "supporting_projects")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupportingProject extends BaseEntity {
    private Timestamp supportedAt;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Payment payment;

    @ManyToOne
    private Delivery delivery;
}

