package org.eightbit.damdda.project.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.dto.*;
import org.eightbit.damdda.project.service.LikedProjectService;
import org.eightbit.damdda.project.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

// pr완료
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.35:3000", "http://127.0.0.1:3000", "http://223.130.156.95", "http://223.130.156.95"})
@RestController
@RequestMapping("/api/projects")
@Log4j2
@RequiredArgsConstructor
public class ProjectApiController {

    private final ProjectService projectService;
    private final LikedProjectService likedProjectService;


    @GetMapping("/projects")
    public PageResponseDTO<ProjectBoxDTO> getProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long memberId,
            @RequestParam(defaultValue = "전체") String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String progress,
            @RequestParam(required = false) String[] sort,
            PageRequestDTO pageRequestDTO) {
        List<String> sortConditions = sort != null ? Arrays.asList(sort) : List.of();
        PageResponseDTO<ProjectBoxDTO> sortedProjects = projectService.getProjects(pageRequestDTO, memberId, page, size, category, search, progress, sortConditions);
        return sortedProjects;
    }

    @GetMapping("/write/{projectId}")
    public ProjectRegisterDetailDTO getWriteProject(@PathVariable Long projectId) {
        ProjectRegisterDetailDTO dto = projectService.getProjectDetail(projectId);
        return dto;
    }

    @GetMapping("/write")
    public List<WritingProjectDTO> getWritingProjects(Long memberId) {
        return projectService.getWritingProjectDTO(memberId);
    }

    @GetMapping("/like")
    public PageResponseDTO<ProjectBoxDTO> getLikedProjectList(@RequestParam("memberId") Long memberId,
                                                              PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ProjectBoxDTO> projectBoxDTO = projectService.getListProjectBoxLikeDTO(memberId, pageRequestDTO);
        return projectBoxDTO;
    }

    @GetMapping(value = "/myproject")
    public PageResponseDTO<ProjectBoxHostDTO> getMyProjectList(@RequestParam("memberId") Long memberId,
                                                               PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ProjectBoxHostDTO> projectBoxHostDTO = projectService.getListProjectBoxHostDTO(memberId, pageRequestDTO);
        return projectBoxHostDTO;
    }


    @GetMapping("/{projectId}")
    public ProjectResponseDetailDTO readProjectDetail(@RequestParam("memberId") Long memberId,
                                                      @PathVariable Long projectId) {
        ProjectResponseDetailDTO projectResponseDetailDTO = projectService.readProjectDetail(projectId, memberId);
        return projectResponseDetailDTO;
    }

    @GetMapping("/myproject/{projectId}")
    public ProjectDetailHostDTO readProjectDetailHost(@RequestParam("memberId") Long memberId,
                                                      @PathVariable Long projectId) {
        return projectService.readProjectDetailHost(projectId, memberId);
    }

    @PostMapping("/like")
    public Long registerLikedProject(@RequestParam Long memberId,
                                     @RequestParam Long projectId) {
        return likedProjectService.insertLikedProject(projectId, memberId);
    }

    @DeleteMapping("/like")
    public void deleteLikedProject(@RequestParam Long memberId,
                                   @RequestParam Long projectId) {
        likedProjectService.deleteLikedProject(projectId, memberId);
    }


    //@PostMapping("/register")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long registerPost(@RequestParam("memberId") Long memberId,
                             @RequestPart("projectDetailDTO") ProjectDetailDTO projectDetailDTO,
                             @RequestParam(value = "submit", required = false) String submit,
                             @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages,
                             @RequestPart(value = "descriptionImages", required = false) List<MultipartFile> descriptionImages,
                             @RequestPart(value = "docs", required = false) List<MultipartFile> docs,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        Long projectId = null;
        if (submit.equals("저장")) {
            projectId = projectService.register(memberId, projectDetailDTO, false, productImages, descriptionImages, docs);
        } else if (submit.equals("제출")) {
            projectId = projectService.register(memberId, projectDetailDTO, true, productImages, descriptionImages, docs);
        } else {
            redirectAttributes.addFlashAttribute("errors", "Invalid submit action.");
        }
        return projectId;

    }


    @PutMapping("/register/{projectId}")
    public String registerPut(@PathVariable Long projectId,
                              @RequestPart(value = "productImagesMeta", required = false) List<FileDTO> productImagesMeta,
                              @RequestPart(value = "descriptionImagesMeta", required = false) List<FileDTO> descriptionImagesMeta,
                              @RequestPart(value = "docsMeta", required = false) List<FileDTO> docsMeta,
                              @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages,
                              @RequestPart(value = "descriptionImages", required = false) List<MultipartFile> descriptionImages,
                              @RequestPart(value = "docs", required = false) List<MultipartFile> docs,
                              @RequestPart(value = "updateProductImage", required = false) List<FileDTO> updateProductImage,
                              @RequestPart(value = "updateDescriptionImage", required = false) List<FileDTO> updateDescriptionImage,
                              @RequestPart(value = "updateDocs", required = false) List<FileDTO> updateDocs,
                              @Valid @RequestPart("projectDetailDTO") ProjectDetailDTO projectDetailDTO,
                              String submit,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("has errors..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "error";  // 유효성 검증 실패 시 에러 페이지로 이동
        }


        // submit 값에 따른 처리
        if (submit.equals("저장")) {
            projectId = projectService.updateProject(projectDetailDTO, projectId, false, productImagesMeta, descriptionImagesMeta, descriptionImagesMeta, productImages, descriptionImages, descriptionImages, updateProductImage, updateDescriptionImage, updateDocs);
        } else if (submit.equals("제출")) {
            projectId = projectService.updateProject(projectDetailDTO, projectId, true, productImagesMeta, descriptionImagesMeta, docsMeta, productImages, descriptionImages, descriptionImages, updateProductImage, updateDescriptionImage, updateDocs);
        } else {
            redirectAttributes.addFlashAttribute("errors", "Invalid submit action.");
            return "error";  // submit 값이 잘못된 경우 에러 페이지로 이동
        }
        // projectId 리턴
        return "projectId: " + projectId + "\n" + projectService.findById(projectId);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> registerDelete(@PathVariable Long projectId) {

        try {
            // 프로젝트가 존재하는지 확인
            projectService.findById(projectId);
            projectService.delProject(projectId);
            return ResponseEntity.ok("Project deleted successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}