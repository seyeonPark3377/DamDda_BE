package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "project_tags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectTag extends BaseEntity {
    @ManyToOne
    private Project project;
    @ManyToOne
    private Tag tag;
}
