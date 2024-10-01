package org.eightbit.damdda.project.repository;


import org.eightbit.damdda.project.domain.PackageRewards;
import org.eightbit.damdda.project.domain.ProjectPackage;
import org.eightbit.damdda.project.domain.ProjectRewards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface PackageRewardsRepository extends JpaRepository<PackageRewards,Long> {

    @Query("SELECT DISTINCT pr.projectReward FROM PackageRewards pr WHERE pr.project.id = :projectId")
    List<ProjectRewards> findRewardsByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT DISTINCT pr.projectReward FROM PackageRewards pr WHERE pr.projectPackage.id = :packageId")
    List<ProjectRewards> findRewardsByPackageId(@Param("packageId") Long packageId);

    @Query("SELECT pr FROM PackageRewards pr JOIN FETCH pr.projectReward WHERE pr.project.id = :projectId")
    List<PackageRewards> findAllRewardsByProjectIdWithProjectReward(@Param("projectId") Long projectId);

    @Query("select pr.rewardCount from PackageRewards pr where pr.projectPackage.id =:packageId AND pr.projectReward.id=:rewardId")
    int findRewardCountByRewardId(@Param("packageId") Long packageId, @Param("rewardId") Long rewardId);

    @Query("select pr from PackageRewards pr where  pr.projectReward.rewardName=:rewardName")
    PackageRewards findPackageRewardByRewardName(@Param("rewardName") String rewardName);

}
