package org.eightbit.damdda.project.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.35:3000"})

@RestController
@RequestMapping("/files/projects")
@Log4j2
@RequiredArgsConstructor
public class FileApiController {

    @Value("${org.eightbit.damdda.path}")
    private String basePath;  // 실제 파일이 저장된 경로
    @Value("${cloud.aws.credentials.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    @GetMapping("/{projectId}/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String projectId, @PathVariable String fileName) {
        String filePath = "projects/" + projectId + "/" + fileName;

        boolean download = false;
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, filePath));
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        Resource resource = new InputStreamResource(inputStream);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build(); // 파일이 없을 경우 404 반환
        }

        HttpHeaders headers = new HttpHeaders();
        if (download) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            UriUtils.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8) + "\"")
                    .body(resource);
        }
        headers.add("Content-Type", s3Object.getObjectMetadata().getContentType());
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
//        try {
//            // 파일 경로 생성
//            Path filePath = Paths.get(basePath).resolve("projects").resolve(projectId).resolve(fileName).normalize();
//            // 파일 리소스를 생성
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists() && resource.isReadable()) {
//                // 파일의 MIME 타입 추출
//                String contentType = Files.probeContentType(filePath);
//
//                // 기본 contentType이 null인 경우 설정
//                if (contentType == null) {
//                    contentType = "application/octet-stream";
//                }
//                // ResponseEntity로 파일과 헤더 반환
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + UriUtils.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8) + "\"")
//                        .body(resource);
//            } else {
//                throw new RuntimeException(fileName + " not found" + "파일을 찾을 수 없거나 읽을 수 없습니다.");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("잘못된 파일 경로입니다.", e);
//        } catch (IOException e) {
//            throw new RuntimeException("파일을 읽는 동안 오류가 발생했습니다.", e);
//        }
//    }
}