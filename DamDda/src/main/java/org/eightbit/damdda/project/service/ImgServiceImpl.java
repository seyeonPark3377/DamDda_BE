package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectImage;
import org.eightbit.damdda.project.domain.ProjectImageType;
import org.eightbit.damdda.project.repository.ProjectImageRepository;
import org.eightbit.damdda.project.repository.ProjectImageTypeRepository;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ImgServiceImpl implements ImgService {
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectImageTypeRepository projectImageTypeRepository;

    public boolean deleteImageFiles(List<ProjectImage> images, String S_imageUrl) {
        boolean result = true;
        for (ProjectImage img : images) {
            File file = new File(img.getUrl());
            if (file.exists()) {
                result = result && file.delete();  // 파일 삭제
            } else {
                result = false;
            }
        }
        File S_file = new File(S_imageUrl);
        if (S_file.exists()) {
            result = result && S_file.delete();  // 파일 삭제
        } else {
            result = false;
        }

        // 3. 이미지 파일 삭제가 성공했을 경우, DB에서 해당 이미지 정보 삭제
        if (result) {
            // 데이터베이스에서 ProjectImage 엔티티 삭제
            for (ProjectImage img : images) {
                projectImageRepository.delete(img);  // DB에서 이미지 삭제
            }
        }

        return result;  // 파일이 존재하지 않으면 false 반환
    }

    public void saveImages(Project project, List<MultipartFile> images) throws IOException {
        String uploadDirectory = "C:/damdda/projects/" + project.getId();
        File uploadDir = new File(uploadDirectory);

        List<ProjectImage> projectImages = new ArrayList<>();

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // 경로 없으면 생성
        }

        for (int i = 0; i < images.size(); i++) {
            MultipartFile file = images.get(i);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destinationFile = new File(uploadDirectory + "/" + fileName);

            // 파일 저장
            file.transferTo(destinationFile);

            // 첫 번째 이미지를 썸네일로 설정
            if (i == 0) {
                String thumbnailFileName = "thumbnail_" + fileName;
                File thumbnailFile = new File(uploadDirectory + "/" + thumbnailFileName);

                // 이미지 리사이징 및 압축하여 썸네일 생성
                Thumbnails.of(destinationFile)
                        .size(200, 200)  // 썸네일 크기 설정 (200x200 예시)
                        .outputFormat("jpg")  // 출력 포맷 설정 (필요 시)
                        .toFile(thumbnailFile);

                // 프로젝트에 썸네일 경로 저장
                project.setThumbnailUrl("/uploads/projects/" + project.getId() + "/" + thumbnailFileName);
                projectRepository.save(project);  // 썸네일 URL 저장
            }

            // 이미지 타입 설정 (썸네일과 일반 이미지)
            ProjectImageType imageType = (i == images.size()) ? projectImageTypeRepository.findById(2L).orElse(null)
                    : projectImageTypeRepository.findById(1L).orElse(null);

            // 이미지 엔티티 저장
            ProjectImage projectImage = ProjectImage.builder()
                    .project(project)
                    .url("/uploads/projects/" + project.getId() + "/" + fileName)
                    .fileName(fileName)
                    .ord(i)
                    .imageType(imageType)
                    .build();

            projectImageRepository.save(projectImage);

            // 6. 프로젝트의 이미지 리스트에 추가
            projectImages.add(projectImage);  // 이미지 리스트에 추가
        }

        project.setProjectImages(projectImages);
        // 7. 프로젝트 저장 (이미지 리스트 포함)
        projectRepository.save(project);  // 프로젝트의 projectImages 리스트 업데이트
    }


}
