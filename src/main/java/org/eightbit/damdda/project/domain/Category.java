package org.eightbit.damdda.project.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {

    @Id
    private String name;

}
