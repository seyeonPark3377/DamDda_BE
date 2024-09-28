package org.eightbit.damdda.project.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "package_rewards_options")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageRewards extends BaseEntity {
    /************************중요**************************/
    /*Package만의 id를 만드는 방법도 고려*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ProjectPackage projectPackage;

    @ManyToOne
    @JoinColumn(name="reward_id")
    private ProjectRewards projectReward;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private int rewardCount; //선물 갯수

    public void setProjectPackage(ProjectPackage projectPackage){this.projectPackage =projectPackage;}
    public void changeRewardCount(int rewardCount){
        this.rewardCount = rewardCount;
    }
    public void setProject(Project project){this.project = project;}
    //reward가 package보다 먼저 생성이 되기 떄문에 project는 reward, package 내부에 있음.

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();

            return "ProjectReward{" +
                    "id=" + id +
                    ", rewardCount=" + rewardCount +
                    '}';
    }
}

