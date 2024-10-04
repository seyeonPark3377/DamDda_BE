package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Project;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supporting_projects")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupportingProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SupportingProjectId;

    private LocalDateTime supportedAt; // 후원 시간

    @ManyToOne
    @JoinColumn(name = "user_id")  // 외래 키로 참조할 필드
    private Member user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}

