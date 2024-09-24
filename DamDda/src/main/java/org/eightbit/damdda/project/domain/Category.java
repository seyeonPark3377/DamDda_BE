package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString   //(exclude = "projects")
public class Category {

    @Id
    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<Project> projects;
//
//    public void setProjects(List<Project> projects) {
//        this.projects = projects;
//    }
}
