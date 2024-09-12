package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "project_rewards")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectReward extends BaseEntity {
    @ManyToOne
    private Project project;

    private String rewardName;
}

