package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.hibernate.annotations.Tables;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "project_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "project")
public class ProjectImage extends BaseEntity {

    @ManyToOne
    private Project project;

    private String url;
    private int ord;
    private String fileName;

    @ManyToOne
    private ProjectImageType imageType;


}
