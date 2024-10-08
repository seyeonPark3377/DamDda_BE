package org.eightbit.damdda.project.repository;

import org.eightbit.damdda.project.domain.ProjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectDocumentRepository  extends JpaRepository<ProjectDocument, Long> {
    public List<ProjectDocument> findAllByProjectId(Long projectId);
    public List<ProjectDocument> findAllByProjectIdOrderByOrd(Long projectId);
    void deleteByUrlIn(List<String> urls);
}

