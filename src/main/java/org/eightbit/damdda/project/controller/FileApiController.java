package org.eightbit.damdda.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.35:3000"})

@RestController
@RequestMapping("/files/projects")
@Log4j2
@RequiredArgsConstructor
public class FileApiController {

    @Value("${org.eightbit.damdda.path}")
    private String basePath;  // 실제 파일이 저장된 경로

    @GetMapping("/{projectId}/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String projectId, @PathVariable String fileName) {
        log.info("Request file " + fileName);
        try {
            // 파일 경로 생성
            Path filePath = Paths.get(basePath).resolve("projects").resolve(projectId).resolve(fileName).normalize();
            // 파일 리소스를 생성
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                // 파일의 MIME 타입 추출
                String contentType = Files.probeContentType(filePath);

                // 기본 contentType이 null인 경우 설정
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                log.info("Served file " + fileName);
                // ResponseEntity로 파일과 헤더 반환
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + UriUtils.encode(resource.getFilename(), StandardCharsets.UTF_8) + "\"")
                        .body(resource);
            } else {
                log.info(fileName + " not found");
                throw new RuntimeException(fileName + " not found" + "파일을 찾을 수 없거나 읽을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("잘못된 파일 경로입니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는 동안 오류가 발생했습니다.", e);
        }
    }
}