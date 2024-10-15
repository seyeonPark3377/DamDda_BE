package org.eightbit.damdda.order.repository;

import org.eightbit.damdda.order.domain.SupportingPackage;
import org.eightbit.damdda.project.dto.DailySupporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SupportingPackageRepository extends JpaRepository<SupportingPackage, Long> {
    // 필요한 추가 쿼리 메서드 정의 가능

    /////////지영///////////
    //후원 프로젝트의 시작일, 마감일, 달성률, 총 후원 금액, 후원자 수, 남은 기간을 가져오는 로직
    //특정 프로젝트의 총 후원 금액을 계산하는 메서드

    /* supporting 패키지에서  supporting_project_id가 projectid와 일치하는 걸 같고 packageprice를 모두 더함
     **/
    @Query("SELECT sp.packagePrice FROM SupportingPackage sp WHERE sp.supportingProject.project.id = :projectId")
    List<String> findPackagePricesByProjectId(@Param("projectId") Long projectId);

    // 특정 프로젝트의 후원자 수를 계산하는 메서드 (중복 후원자를 제거)
    @Query("SELECT COUNT(DISTINCT sp.user.id) FROM SupportingProject sp WHERE sp.project.id = :projectId")
    Long getTotalSupporters(@Param("projectId") Long projectId);

    // project_id로 프로젝트의 end_date를 가져오는 쿼리
    @Query("SELECT DISTINCT sp.supportingProject.project.endDate FROM SupportingPackage sp WHERE sp.supportingProject.project.id = :projectId")
    Timestamp findProjectEndDateByProjectId(@Param("projectId") Long projectId);

    // 특정 프로젝트의 created_at 가져오는 쿼리
    @Query("SELECT p.createdAt FROM Project p WHERE p.id = :projectId")
    Timestamp getCreatedAtByProjectId(@Param("projectId") Long projectId);



    //목표금액 가져오는 쿼리
    @Query("SELECT p.targetFunding FROM Project p WHERE p.id = :projectId")
    Long getTargetFundingByProjectId(@Param("projectId") Long projectId);



}
