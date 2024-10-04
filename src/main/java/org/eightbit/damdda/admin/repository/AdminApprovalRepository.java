package org.eightbit.damdda.admin.repository;


import org.eightbit.damdda.admin.domain.AdminApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminApprovalRepository extends JpaRepository<AdminApproval, Long> {
    Optional<AdminApproval> findByProjectId(Long projectId);
    List<AdminApproval> findAllByApproval(Integer approval);
}
