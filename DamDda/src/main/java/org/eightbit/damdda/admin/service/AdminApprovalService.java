package org.eightbit.damdda.admin.service;

import org.eightbit.damdda.admin.domain.AdminApproval;

import java.util.List;
import java.util.Optional;

public interface AdminApprovalService {
    Optional<AdminApproval> findByProjectId(Long projectId);
    List<AdminApproval> findAllByApproval(Integer approval);
}
