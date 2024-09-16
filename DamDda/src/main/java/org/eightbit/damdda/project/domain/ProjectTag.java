package org.eightbit.damdda.project.domain;
//이거 필요 없을 듯

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
