package org.eightbit.damdda.order.repository;

import org.eightbit.damdda.order.domain.SupportingProject;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingProjectRepository extends JpaRepository<SupportingProject, Long> {
    // 특정 프로젝트에 대한 후원자 수를 카운트
    long countByProject(Project project);
}
