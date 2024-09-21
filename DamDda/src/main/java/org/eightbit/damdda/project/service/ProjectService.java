package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {

//    public List<Project> getProjectsByIds(List<Long> projectIds);
    PageResponseDTO<ProjectBoxHostDTO> getListProjectBoxHostDTO(Long bno, PageRequestDTO pageRequestDTO);
    ProjectDetailHostDTO readProjectDetailHost(Long projectId);
    ProjectResponseDetailDTO readProjectDetail(Long projectId);
    String delProject(Long projectId);
    Long register(Long memberId,
                  ProjectDetailDTO projectDetailDTO,
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
