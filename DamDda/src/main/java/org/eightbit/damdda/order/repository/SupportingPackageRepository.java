package org.eightbit.damdda.order.repository;

import org.eightbit.damdda.order.domain.SupportingPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingPackageRepository extends JpaRepository<SupportingPackage, Long> {
    // 필요한 추가 쿼리 메서드 정의 가능
}
