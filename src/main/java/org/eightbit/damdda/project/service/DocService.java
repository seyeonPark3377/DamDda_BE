package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocService {

    boolean deleteDocFiles(List<ProjectDocument> docs);
    void saveDocs(Project project, List<MultipartFile> docs);
}
