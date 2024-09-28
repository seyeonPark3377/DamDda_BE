package org.eightbit.damdda.order.service;

import org.eightbit.damdda.project.domain.Project;

public interface SupportingProjectService {
    long countByProject(Project project);
}
