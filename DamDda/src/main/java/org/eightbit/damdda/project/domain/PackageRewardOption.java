package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "package_rewards_options")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PackageRewardOption extends BaseEntity {
    @ManyToOne
    private ProjectPackage projectPackage;
    @ManyToOne
    private ProjectReward projectReward;
    @ManyToOne
    private RewardOption rewardOption;

    private Integer rewardCount;
}

