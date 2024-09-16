package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.ProjectDTO;
import org.eightbit.damdda.project.dto.ProjectRegistDTO;

public interface ProjectService {
    Long register(ProjectRegistDTO projectRegistDTO, boolean submit);
    Project findById(Long id);
}
