package org.eightbit.damdda.admin.domain;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.common.domain.BaseEntity;
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
@Log4j2
public class AdminApproval extends BaseEntity {
//    @ManyToOne
//    private Admin admin;

    @OneToOne
    private Project project;

    @ColumnDefault("0")
    private Integer approval;

    private String approvalText;
    private Timestamp approvalAt;

}