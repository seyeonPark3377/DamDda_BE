package org.eightbit.damdda.admin.domain;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 관리자 승인(AdminApproval) 엔티티. 'admin_approvals' 테이블과 매핑됨.
 */
@Entity
@Table(name = "admin_approvals")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminApproval {

    /**
     * 관리자 승인 고유 식별자 (ID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 프로젝트와의 1:1 매핑
     */
    @OneToOne
    private Project project;

    /**
     * 승인 상태 (0: 대기, 1: 승인, 2: 거절)
     */
    @ColumnDefault("0")
    private Integer approval;

    // TODO: 확인 필요
    /**
     * 승인 관련 설명 (기본값: '프로젝트 승인 대기 중...')
     */
    @ColumnDefault("'프로젝트 승인 대기 중...'")
    private String approvalText;

    // TODO: 자동 저장이 아니라 수동 저장?
    /**
     * 승인 처리 시간
     */
    private Timestamp approvalAt;
}