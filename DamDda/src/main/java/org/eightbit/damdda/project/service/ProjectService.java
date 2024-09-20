package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.CategoriesDTO;
import org.eightbit.damdda.project.dto.ProjectDetailDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

//    public List<Project> getProjectsByIds(List<Long> projectIds);
    String delProject(Long projectId);
    Long register(ProjectDetailDTO projectDetailDTO,
                  boolean submit,
                  List<MultipartFile> productImages,
                  List<MultipartFile> descriptionImages,
                  List<MultipartFile> docs);
    Project findById(Long id);
    Long updateProject(ProjectDetailDTO projectDetailDTO,
                       Long projectId,
                       boolean submit,
                       List<MultipartFile> productImages,
                       List<MultipartFile> descriptionImages,
                       List<MultipartFile> docs);
}
