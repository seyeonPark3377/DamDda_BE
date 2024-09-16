package org.eightbit.damdda.project.repository;

import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
}
