package org.eightbit.damdda.project.repository;

import org.eightbit.damdda.project.domain.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    public List<ProjectImage> findAllByProjectId(Long projectId);
    public List<ProjectImage> findAllByProjectIdOrderByOrd(Long projectId);

}
