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

public class  SupportingPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @ManyToOne
    @JoinColumn(name="id")
    private ProjectPackage projectPackage;

    @ManyToOne
    @JoinColumn(name = "supporting_project_id")
    private SupportingProject supportingProject;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    private Integer packageCount;

    @Column(columnDefinition = "json")
    private String OptionList;

}