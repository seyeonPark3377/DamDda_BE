package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectImage;
import org.eightbit.damdda.project.domain.ProjectImageType;
import org.eightbit.damdda.project.dto.FileDTO;
import org.eightbit.damdda.project.repository.ProjectImageRepository;
import org.eightbit.damdda.project.repository.ProjectImageTypeRepository;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
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
        log.info(images);
        boolean result = true;
        // 첫 번째 파일의 폴더 경로를 추출
        if (!images.isEmpty()) {
            String folderPath = basePath + images.get(0).getUrl().replace("files", ""); // 기본 경로 설정
            folderPath = folderPath.substring(0, folderPath.lastIndexOf("/")); // 파일명 제외하고 폴더 경로만 추출
            File directory = new File(folderPath);


            for (ProjectImage img : images) {
                String filePath = basePath + img.getUrl().replace("files", "");  // img.getUrl()이 상대 경로라 가정
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
//
//    @Override
//    public void saveThumbnailImages(Project project, FileDTO productImage) {
//        String uploadDirectory = basePath + "/projects/" + project.getId();
//        File uploadDir = new File(uploadDirectory);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();  // 경로 없으면 생성
//        }
//        try {
//            MultipartFile file = productImage.getFile();
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            File destinationFile = new File(uploadDirectory + "/" + fileName);
//
//                String thumbnailFileName = "thumbnail_" + fileName;
//                File thumbnailFile = new File(uploadDirectory + "/" + thumbnailFileName);
//
//                // 이미지 리사이징 및 압축하여 썸네일 생성
//                Thumbnails.of(destinationFile)
//                        .size(300, 300)  // 썸네일 크기 설정 (200x200 예시)
////                            .outputFormat("jpg")  // 출력 포맷 설정 (필요 시)
//                        .toFile(thumbnailFile);
//
//                String thumbnailUrl = "files/projects/" + project.getId() + "/" + thumbnailFileName;
//                // 프로젝트에 썸네일 경로 저장
//                project.setThumbnailUrl(thumbnailUrl);
//                projectRepository.save(project);  // 썸네일 URL 저장
//
//                // 이미지 타입 설정 (썸네일과 일반 이미지)
//                ProjectImageType imageType = projectImageTypeRepository.findById(2L).orElse(null);
//
//                // 이미지 엔티티 저장
//                ProjectImage projectImage = ProjectImage.builder()
//                        .project(project)
//                        .url(thumbnailUrl)
//                        .fileName(thumbnailFileName)
//                        .ord(0)
//                        .imageType(imageType)
//                        .build();
//
//                projectImageRepository.save(projectImage);
//        } catch (IOException e) {
//            // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
//            e.printStackTrace();
//        }
//    }

    @Override
    public String saveThumbnailImages(Project project, ProjectImage thumbnailImage) {
        String uploadDirectory = basePath + "/projects/" + project.getId();
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // 경로 없으면 생성
        }
        try {
            // ProjectImage 엔티티에서 이미지 파일 경로(URL)를 가져옴
            String imageUrl = thumbnailImage.getUrl();
            imageUrl = basePath + imageUrl.replace("files", "");
            log.info("imageUrl: " + imageUrl);
            File originalImageFile = new File(imageUrl);

            if (!originalImageFile.exists()) {
                throw new FileNotFoundException("Original image file not found: " + imageUrl);
            }

            // 원본 이미지 파일의 이름을 기반으로 썸네일 파일명 생성
            String fileName = originalImageFile.getName();
            String thumbnailFileName = "thumbnail_" + fileName;
            File thumbnailFile = new File(uploadDirectory + "/" + thumbnailFileName);

            // 이미지 리사이징 및 압축하여 썸네일 생성
            Thumbnails.of(originalImageFile)
                    .size(300, 300)  // 썸네일 크기 설정
                    .outputQuality(0.8)  // 압축 품질 설정
                    .toFile(thumbnailFile);  // 압축된 썸네일 파일 저장

            ProjectImageType imageType = projectImageTypeRepository.findById(2L).orElse(null);

            // 이미지 엔티티 저장
            ProjectImage newThumbnailImage = ProjectImage.builder()
                    .project(project)
                    .url("files/projects/" + project.getId() + "/" + thumbnailFileName)
                    .fileName(thumbnailFileName)
                    .ord(0)
                    .imageType(imageType)
                    .build();
            log.info("projectImage : " + newThumbnailImage);
            projectImageRepository.save(newThumbnailImage);


            // 이후 repository를 통해 projectImage를 저장 가능
             projectImageRepository.save(newThumbnailImage);
             return newThumbnailImage.getUrl();
        } catch (IOException e) {
            // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public void saveImages(Project project, List<FileDTO> Images, Long ImageTypeId) {

        String uploadDirectory = basePath + "/projects/" + project.getId();
        File uploadDir = new File(uploadDirectory);


        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // 경로 없으면 생성
        }

        for (FileDTO image : Images) {
            try {
                MultipartFile file = image.getFile();
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadDirectory + "/" + fileName);

                // 파일 저장
                file.transferTo(destinationFile);

                // 이미지 타입 설정 (썸네일과 일반 이미지)
                ProjectImageType imageType = projectImageTypeRepository.findById(ImageTypeId).orElse(null);

                // 이미지 엔티티 저장
                ProjectImage projectImage = ProjectImage.builder()
                        .project(project)
                        .url("files/projects/" + project.getId() + "/" + fileName)
                        .fileName(fileName)
                        .ord(image.getOrd())
                        .imageType(imageType)
                        .build();
                log.info("projectImage : " + projectImage);
                projectImageRepository.save(projectImage);

            } catch (IOException e) {
                // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
                e.printStackTrace();
            }


        }

    }
}