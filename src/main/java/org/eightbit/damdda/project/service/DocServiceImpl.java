package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectDocument;
import org.eightbit.damdda.project.dto.FileDTO;
import org.eightbit.damdda.project.repository.ProjectDocumentRepository;
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
public class DocServiceImpl implements DocService {

    private final ProjectDocumentRepository projectDocumentRepository;
    @Value("${org.eightbit.damdda.path}")
    private String basePath;

    public boolean deleteDocFiles(List<ProjectDocument> docs) {
        boolean result = true;

        for (ProjectDocument doc : docs) {
            String filePath = basePath + doc.getUrl().replace("files", "");  // img.getUrl()이 상대 경로라 가정
            File file = new File(filePath);

            if (file.exists()) {
                boolean isDelete = file.delete();
                result = result && isDelete; // 파일 삭제
                if (isDelete) {
                    projectDocumentRepository.delete(doc);
                }
            } else {
                result = false;
            }
        }

        if (result) {
            projectDocumentRepository.deleteAll(docs);
        }

        return result;  // 파일이 존재하지 않으면 false 반환
    }

    public void saveDocs(Project project, List<FileDTO> docs) {
        String uploadDirectory = basePath + "/projects/" + project.getId();
        File uploadDir = new File(uploadDirectory);

        if (!uploadDir.exists()) {
            // 경로가 없으면 디렉터리 생성
            if (!uploadDir.mkdirs()) {
                // 디렉터리 생성 실패 시 로그 또는 예외 처리
                throw new RuntimeException("Failed to create the directory: " + uploadDir.getAbsolutePath());
            }
        }


        for (FileDTO doc : docs) {
            try {
                MultipartFile file = doc.getFile();
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadDirectory + "/" + fileName);

                // 파일 저장
                file.transferTo(destinationFile);

                // 이미지 엔티티 저장
                ProjectDocument projectDocument = ProjectDocument.builder()
                        .project(project)
                        .url("files/projects/" + project.getId() + "/" + fileName)
                        .fileName(fileName)
                        .ord(doc.getOrd())
                        .build();

                projectDocumentRepository.save(projectDocument);

            } catch (IOException e) {
                // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
                throw new RuntimeException(e);
            }
        }
    }
}

