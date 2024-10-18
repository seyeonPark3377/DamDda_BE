package org.eightbit.damdda.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.eightbit.damdda.project.domain.ProjectPackage;

import javax.persistence.*;

@Entity
@Table(name = "supporting_packages")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    //선택한 옵션
    @Column(columnDefinition = "json")
    private String OptionList;


}