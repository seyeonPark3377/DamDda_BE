package org.eightbit.damdda.admin.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Project;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_approvals")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminApproval extends BaseEntity {
    @ManyToOne
    private Admin admin;

    @OneToOne
    private Project project;

    @ColumnDefault("0")
    private Integer approval;

    private String approvalText;
    private Timestamp approvalAt;
}

