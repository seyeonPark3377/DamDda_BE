package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectDocument;
import org.eightbit.damdda.project.dto.FileDTO;
import org.eightbit.damdda.project.repository.ProjectDocumentRepository;
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
public class DocServiceImpl implements DocService {

    @Value("${org.eightbit.damdda.path}")
    private String basePath;

    private final ProjectRepository projectRepository;
    private final ProjectDocumentRepository projectDocumentRepository;



    public boolean deleteDocFiles(List<ProjectDocument> docs){
        log.info("delete doc files" + docs);
        boolean result = true;

        for (ProjectDocument doc : docs) {
            String filePath = basePath + doc.getUrl().replace("files", "");  // img.getUrl()이 상대 경로라 가정
            File file = new File(filePath);

            if (file.exists()) {
                boolean isDelete = file.delete();
                result = result && isDelete; // 파일 삭제
                if (isDelete){
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

    public void saveDocs(Project project, List<FileDTO> docs){
        String uploadDirectory = basePath + "/projects/" + project.getId();
        File uploadDir = new File(uploadDirectory);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // 경로 없으면 생성
        }

        for (int i = 0; i < docs.size(); i++) {
            try {
                MultipartFile file = docs.get(i).getFile();
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadDirectory + "/" + fileName);

                // 파일 저장
                file.transferTo(destinationFile);

                // 이미지 엔티티 저장
                ProjectDocument projectDocument = ProjectDocument.builder()
                        .project(project)
                        .url("files/projects/" + project.getId() + "/" + fileName)
                        .fileName(fileName)
                        .ord(docs.get(i).getOrd())
                        .build();

                projectDocumentRepository.save(projectDocument);

            } catch (IOException e) {
                // 예외 처리 로직 작성 (로그 기록 또는 사용자에게 알림 등)
                e.printStackTrace();
            }
        }
    }
}

