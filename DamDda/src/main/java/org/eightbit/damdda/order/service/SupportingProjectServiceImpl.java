package org.eightbit.damdda.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.order.Repository.SupportingProjectRepository;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.eightbit.damdda.project.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


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

}