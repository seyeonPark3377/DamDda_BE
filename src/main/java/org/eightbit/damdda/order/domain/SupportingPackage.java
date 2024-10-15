package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.project.domain.ProjectPackage;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supporting_packages")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class  SupportingPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @ManyToOne
    @JoinColumn(name = "supporting_project_id")
    private SupportingProject supportingProject;

    @ManyToMany(mappedBy = "supportingPackages")
    private Set<Order> orders = new HashSet<>(); // 역방향 다대다 관계

    private String packageName;
    private Long packagePrice;
    private Long packageCount;


}

