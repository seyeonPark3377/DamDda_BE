package org.eightbit.damdda.order.repository;

import org.eightbit.damdda.order.domain.SupportingPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SupportingPackageRepository extends JpaRepository<SupportingPackage, Long> {

    @Query("SELECT sProj.supportedAt, SUM(sp.projectPackage.packagePrice) " +
            "FROM SupportingPackage sp " +
            "JOIN sp.supportingProject sProj " +
            "JOIN sProj.payment p " +
            "WHERE sp.supportingProject.project.id = :projectId " +
            "AND p.paymentStatus = '결제 완료' " +
            "GROUP BY sProj.supportedAt " +
            "ORDER BY sProj.supportedAt")
    List<Object[]> findTotalPackagePriceByProjectIdGroupedByDate(@Param("projectId") Long projectId);
    // 프로젝트 ID에 해당하는 후원 데이터를 날짜별로 조회하는 쿼리
    // - sProj.supportedAt: 후원이 발생한 날짜
    // - SUM(sp.projectPackage.packagePrice): 해당 날짜의 총 후원 금액 합계
    // 조건:
    // - 지정된 프로젝트 ID에 해당하는 후원 데이터
    // - 결제 상태가 '결제 완료'인 경우만 필터링
    // 그룹화 및 정렬:
    // - 후원 발생 날짜(sProj.supportedAt)별로 그룹화
    // - 날짜 순으로 결과 정렬
    // 반환값:
    // - List<Object[]> 형식으로 반환하며, 각 배열의 첫 번째 요소는 후원 날짜(LocalDateTime), 두 번째 요소는 총 후원 금액(Long)

    Set<SupportingPackage> findByOrder_OrderId(@Param("orderId") Long orderId);
}