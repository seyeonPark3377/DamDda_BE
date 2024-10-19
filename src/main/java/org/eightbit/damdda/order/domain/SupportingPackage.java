package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.project.domain.ProjectPackage;

import javax.persistence.*;

@Entity
@Table(name = "supporting_packages")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SupportingPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_package_id")
    private ProjectPackage projectPackage;

    @ManyToOne
    @JoinColumn(name = "supporting_project_id")
    private SupportingProject supportingProject;

    // 양방향 매핑 설정, ManyToOne 관계
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer packageCount;

    @Column(columnDefinition = "json")
    private String optionList;
}