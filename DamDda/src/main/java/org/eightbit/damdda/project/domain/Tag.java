package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tag extends BaseEntity {
    private String name;
    private Integer usageFrequency;
}
