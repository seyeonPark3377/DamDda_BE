package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(value={AuditingEntityListener.class})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;  // 기본키로 주문 ID를 설정

    // Delivery 정보와 연관된 필드
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    // Payment 정보와 연관된 필드
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    // SupportingProject 정보와 연관된 필드
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "supporting_project_id", nullable = false)
    private SupportingProject supportingProject;

    // SupportingPackage 정보와 연관된 필드
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ordered_packages", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "order_id"), // 현재 엔티티의 외래 키
            inverseJoinColumns = @JoinColumn(name = "supporting_package_id") // 연관된 SupportingPackage의 외래 키
    )
    private Set<SupportingPackage> supportingPackages;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;  // 주문 수정 시간

}