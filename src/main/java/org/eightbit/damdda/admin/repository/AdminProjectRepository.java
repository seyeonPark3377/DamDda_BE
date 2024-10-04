package org.eightbit.damdda.admin.repository;


import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProjectRepository extends JpaRepository<Project, Long> {
}
