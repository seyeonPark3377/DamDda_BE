package org.eightbit.damdda.order.service;

import org.eightbit.damdda.order.domain.SupportingProject;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportingProjectService {
    long countByProject(Project project);
}
