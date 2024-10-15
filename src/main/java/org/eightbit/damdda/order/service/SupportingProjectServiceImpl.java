package org.eightbit.damdda.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.order.repository.SupportingProjectRepository;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.DailySupporting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class SupportingProjectServiceImpl implements SupportingProjectService {


    private final SupportingProjectRepository supportingProjectRepository;

    @Override
    public long countByProject(Project project) {
        return supportingProjectRepository.countByProject(project);
    }

    // 태욱
    // 일별 후원액 가져오는 쿼리
    @Override
    public List<?> getDailySupportingByProjectId(Long projectId) {
        return supportingProjectRepository.getDailySupportingByProjectId(projectId);
    }

}
