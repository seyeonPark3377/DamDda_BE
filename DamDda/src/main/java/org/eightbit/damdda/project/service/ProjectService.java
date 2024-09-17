package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.CategoriesDTO;
import org.eightbit.damdda.project.dto.ProjectDetailDTO;

import java.util.List;

public interface ProjectService {

//    public List<Project> getProjectsByIds(List<Long> projectIds);

    Long register(ProjectDetailDTO projectDetailDTO, boolean submit);
    Project findById(Long id);
}
