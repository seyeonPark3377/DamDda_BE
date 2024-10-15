package org.eightbit.damdda.order.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.DailySupporting;

import java.util.List;

public interface SupportingProjectService {
    long countByProject(Project project);

    // 태욱
    // 일별 후원액 가져오는 쿼리
    List<?> getDailySupportingByProjectId(Long projectId);
}
