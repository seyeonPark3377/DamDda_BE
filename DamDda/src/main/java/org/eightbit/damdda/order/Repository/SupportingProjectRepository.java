package org.eightbit.damdda.order.Repository;

import org.eightbit.damdda.order.domain.SupportingProject;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportingProjectRepository extends JpaRepository<SupportingProject, Long> {
    // 특정 프로젝트에 대한 지원 횟수 가져오기
    long countByProject(Project project);
}
