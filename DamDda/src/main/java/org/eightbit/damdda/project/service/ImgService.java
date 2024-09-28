package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImgService {
    boolean deleteImageFiles(List<ProjectImage> images);
    void saveImages(Project project, List<MultipartFile> productImages, List<MultipartFile> descriptionImages);
}
