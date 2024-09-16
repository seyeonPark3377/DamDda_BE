package org.eightbit.damdda.project.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "package_rewards_options")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//PackageRewardOption -> PackageReward
public class PackageReward extends BaseEntity {
    @ManyToOne
    private Project project;
    @ManyToOne
    private ProjectPackage projectPackage;
    @ManyToOne
    private ProjectReward projectReward;

    private Integer rewardCount;
}

