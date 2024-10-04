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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ImgServiceImpl implements ImgService {

    @Value("${org.eightbit.damdda.path}")
    private String basePath;

    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectImageTypeRepository projectImageTypeRepository;

    @Override
    public boolean deleteImageFiles(List<ProjectImage> images) {
        boolean result = true;
        // 첫 번째 파일의 폴더 경로를 추출
        if (!images.isEmpty()) {
            String folderPath = basePath + images.get(0).getUrl().replace("/files", ""); // 기본 경로 설정
            folderPath = folderPath.substring(0, folderPath.lastIndexOf("/")); // 파일명 제외하고 폴더 경로만 추출
            File directory = new File(folderPath);


            for (ProjectImage img : images) {
                String filePath = basePath + img.getUrl().replace("/files", "");  // img.getUrl()이 상대 경로라 가정
                File file = new File(filePath);

                if (file.exists()) {
                    boolean isDelete = file.delete();
                    result = result && isDelete; // 파일 삭제
                    if (isDelete){
                        projectImageRepository.delete(img);
                    }
                } else {
                    result = false;
                }
            }

//            // 3. 이미지 파일 삭제가 성공했을 경우, DB에서 해당 이미지 정보 삭제
//            if (result) {
//                // 데이터베이스에서 ProjectImage 엔티티 삭제
//                // DB에서 이미지 삭제
//                projectImageRepository.deleteAll(images);
//
//            }

            // 폴더가 존재하고, 폴더 안에 파일이 없는 경우 삭제
            if (directory.exists() && directory.isDirectory() && directory.list().length == 0) {
                if (directory.delete()) {
                    System.out.println("빈 폴더 삭제 완료: " + directory.getAbsolutePath());
                } else {
                    System.out.println("폴더 삭제 실패: " + directory.getAbsolutePath());
                }
            } else {
                System.out.println("폴더가 비어 있지 않거나 존재하지 않습니다: " + directory.getAbsolutePath());
            }

        }

        return result;  // 파일이 존재하지 않으면 false 반환
    }


    @Override
    public void saveImages(Project project, List<MultipartFile> productImages, List<MultipartFile> descriptionImages) {

        String uploadDirectory = basePath + "/projects/" + project.getId();
        File uploadDir = new File(uploadDirectory);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // 경로 없으면 생성
        }

        for (int i = 0; i < productImages.size(); i++) {
            try {
                MultipartFile file = productImages.get(i);
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
                            .size(300, 300)  // 썸네일 크기 설정 (200x200 예시)
//                            .outputFormat("jpg")  // 출력 포맷 설정 (필요 시)
                            .toFile(thumbnailFile);

                    String thumbnailUrl = "files/projects/" + project.getId() + "/" + thumbnailFileName;
                    // 프로젝트에 썸네일 경로 저장
                    project.setThumbnailUrl(thumbnailUrl);
                    projectRepository.save(project);  // 썸네일 URL 저장

                    // 이미지 타입 설정 (썸네일과 일반 이미지)
                    ProjectImageType imageType = projectImageTypeRepository.findById(1L).orElse(null);

                    // 이미지 엔티티 저장
                    ProjectImage projectImage = ProjectImage.builder()
                            .project(project)
                            .url(thumbnailUrl)
                            .fileName(thumbnailFileName)
                            .ord(i)
                            .imageType(imageType)
                            .build();

                    projectImageRepository.save(projectImage);
                }

                // 이미지 타입 설정 (썸네일과 일반 이미지)
                ProjectImageType imageType = projectImageTypeRepository.findById(2L).orElse(null);

                // 이미지 엔티티 저장
                ProjectImage projectImage = ProjectImage.builder()
                        .project(project)
                        .url("files/projects/" + project.getId() + "/" + fileName)
                        .fileName(fileName)
                        .ord(i)
                        .imageType(imageType)
                        .build();

                projectImageRepository.save(projectImage);

            } catch (IOException e) {
                // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
                e.printStackTrace();
            }


        }


        for (int i = 0; i < descriptionImages.size(); i++) {
            try {
                MultipartFile file = descriptionImages.get(i);
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadDirectory + "/" + fileName);

                // 파일 저장
                file.transferTo(destinationFile);

                // 이미지 타입 설정 (썸네일과 일반 이미지)
                ProjectImageType imageType = projectImageTypeRepository.findById(3L).orElse(null);

                // 이미지 엔티티 저장
                ProjectImage projectImage = ProjectImage.builder()
                        .project(project)
                        .url("files/projects/" + project.getId() + "/" + fileName)
                        .fileName(fileName)
                        .ord(i)
                        .imageType(imageType)
                        .build();

                projectImageRepository.save(projectImage);

            } catch (IOException e) {
                // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
                e.printStackTrace();
            }

        }

    }
}