package org.eightbit.damdda.admin.domain;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "admin_approvals")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class AdminApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Project project;

    @ColumnDefault("0")
    private Integer approval;

    @ColumnDefault("'프로젝트 승인 대기 중...'")
    private String approvalText;
    private Timestamp approvalAt;

}