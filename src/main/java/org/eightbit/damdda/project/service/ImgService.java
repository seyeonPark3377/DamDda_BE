package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectImage;
import org.eightbit.damdda.project.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImgService {
    boolean deleteImageFiles(List<ProjectImage> images);
    String saveThumbnailImages(Long projectId, ProjectImage thumbnailImage);
    void saveImages(Project project, List<FileDTO> productImages, Long ImageTypeId);
}
