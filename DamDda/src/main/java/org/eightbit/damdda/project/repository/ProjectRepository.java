package org.eightbit.damdda.project.repository;

import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.ProjectBoxHostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long> {
//    @Query("SELECT p FROM Project p WHERE p.deletedAt IS NULL")
//    List<Project> findAllActiveProjects();

    @Query("select p from Project p where p.member.id = :memberId" )
    Page<Project> listOfProjectBoxHost(@Param("memberId") Long memberId, Pageable pageable);

    public Project findByMemberId(Long memberId);


}
