package org.eightbit.damdda.project.repository;

import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.deletedAt IS NULL")
    List<Project> findAllActiveProjects();
}
