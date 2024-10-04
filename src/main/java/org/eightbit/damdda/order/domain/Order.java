package org.eightbit.damdda.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eightbit.damdda.project.domain.Project;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;  // 기본키로 주문 ID를 설정

    // Delivery 정보와 연관된 필드
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    // Payment 정보와 연관된 필드
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // SupportingProject 정보와 연관된 필드
    @ManyToOne
    @JoinColumn(name = "supporting_project_id", nullable = false)
    private SupportingProject supportingProject;

    // SupportingPackage 정보와 연관된 필드
    @ManyToOne
    @JoinColumn(name = "supporting_package_id", nullable = false)
    private SupportingPackage supportingPackage;

    private LocalDateTime createdAt;  // 주문 생성 시간
    private LocalDateTime updatedAt;  // 주문 수정 시간

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


}