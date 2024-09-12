package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "reward_options")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RewardOption extends BaseEntity {
    @ManyToOne
    private ProjectReward projectReward;

    private String optionType;
    private String optionName;

}

