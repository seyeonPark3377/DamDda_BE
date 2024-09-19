package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImgService {
    boolean deleteImageFiles(List<ProjectImage> images, String S_imageUrl);
    void saveImages(Project project, List<MultipartFile> images) throws IOException;
}
