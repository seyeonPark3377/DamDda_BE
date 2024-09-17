package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.project.domain.Project;
//import org.eightbit.damdda.project.domain.Package;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supporting_packages")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupportingPackage extends BaseEntity {
    @ManyToOne
    private Project project;
//    @ManyToOne
//    private Package projectPackage;

    private Integer packageCount;
}

