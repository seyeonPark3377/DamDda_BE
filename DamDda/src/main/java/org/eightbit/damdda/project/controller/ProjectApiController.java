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
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.35:3000", "http://127.0.0.1:3000"})
@RestController
@RequestMapping("/api/projects")
@Log4j2
@RequiredArgsConstructor
public class ProjectApiController {

    private final ProjectService projectService;
    private final LikedProjectService likedProjectService;


//    @GetMapping("/index")
//    public String index(Model model) {
//        return "This is the project index page.";
//    }


//    @GetMapping("")
//    public PageResponseDTO<ProjectBoxDTO> getSort(@RequestParam("memberId") Long memberId,
//                                                  @RequestParam("sortConditions") List<String> sortConditions,
//                                                  PageRequestDTO pageRequestDTO){
//        PageResponseDTO<ProjectBoxDTO> sortedProjects;
//        if(sortConditions.get(0).equals("fundsReceive")){
//            sortedProjects =  projectService.getProjectsSortedByFundingRatio(memberId, pageRequestDTO);
//        } else{
//            sortedProjects =  projectService.findSortedProjects(memberId, pageRequestDTO, sortConditions);
//        }
//        return sortedProjects;
//    }

    @GetMapping("/projects")
    public PageResponseDTO<ProjectBoxDTO> getProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long memberId,
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String progress,
            @RequestParam(required = false) String[] sort,
            PageRequestDTO pageRequestDTO
    ) {
        List<String> sortConditions = sort != null ? Arrays.asList(sort) : List.of();

        PageResponseDTO<ProjectBoxDTO> sortedProjects = projectService.getProjects(pageRequestDTO, memberId, page, size, category, search, progress, sortConditions);
//        PageResponseDTO<ProjectBoxDTO> sortedProjects;
//        if (!sortConditions.isEmpty() && "fundsReceive".equals(sortConditions.get(0))) {
//            // sort 조건 중 첫 번째가 "fundsReceive"일 때
//            sortedProjects = projectService.getProjectsSortedByFundingRatio(category, search, progress,  memberId, pageRequestDTO);
//        } else {
//            // 그 외의 경우
//            sortedProjects = projectService.getProjects(pageRequestDTO, memberId, page, size, category, search, progress, sortConditions);
//        }

        log.info("getProjects"+progress);
        log.info(sortedProjects);

        return sortedProjects;
    }

    @GetMapping("/like")
    public PageResponseDTO<ProjectBoxDTO> getLikedProjectList(@RequestParam("memberId") Long memberId,
                                                      PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ProjectBoxDTO> projectBoxDTO = projectService.getListProjectBoxLikeDTO(memberId, pageRequestDTO);

        return projectBoxDTO;
    }

    @GetMapping(value = "/myproject")
    public PageResponseDTO<ProjectBoxHostDTO> getMyProjectList(@RequestParam("memberId") Long memberId,
//                                                               @RequestParam("page") int page, // 페이지 번호 직접 받기
//                                                               @RequestParam("size") int size){  // 페이지 크기 직접 받기
                                                               PageRequestDTO pageRequestDTO) {
//        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, null, null, null); // type, keyword, link를 null로 설정
        PageResponseDTO<ProjectBoxHostDTO> projectBoxHostDTO = projectService.getListProjectBoxHostDTO(memberId, pageRequestDTO);
        log.info(projectBoxHostDTO + "11111111111111111111111111111111111111111111111111");
        return projectBoxHostDTO;
    }


    @GetMapping("/{projectId}")
    public ProjectResponseDetailDTO readProjectDetail(@RequestParam("memberId") Long memberId,
                                                      @PathVariable Long projectId) {

        log.info("readProjectDetail"+projectId);
        ProjectResponseDetailDTO projectResponseDetailDTO = projectService.readProjectDetail(projectId, memberId);
        log.info("readProjectDetail"+projectResponseDetailDTO);
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
    public String registerPost(@RequestParam("memberId")  Long memberId,
                               @RequestPart("projectDetailDTO")  ProjectDetailDTO projectDetailDTO,
                               @RequestParam("submit") String submit,
                               @RequestPart("productImages") List<MultipartFile> productImages,
                               @RequestPart("descriptionImages") List<MultipartFile> descriptionImages,
                               @RequestPart("docs") List<MultipartFile> docs,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
    // 유효성 검사 실패 시 처리
    if (bindingResult.hasErrors()) {
        log.info("has errors..........");
        redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        return "error";  // 유효성 검증 실패 시 에러 페이지로 이동
    }
    // 프로젝트 ID 변수 선언
    Long projectId = null;
    log.info(submit + "submit!!----------------------------------------------------------");
    log.info(projectDetailDTO + "projectRegistDTO!!------==============================");
    // submit 값에 따른 처리
    if (submit.equals("저장")) {
        projectId = projectService.register(memberId, projectDetailDTO, false, productImages, descriptionImages, docs);
    } else if (submit.equals("제출")) {
        projectId = projectService.register(memberId, projectDetailDTO, true, productImages, descriptionImages, docs);
    } else {
        redirectAttributes.addFlashAttribute("errors", "Invalid submit action.");
        return "error";  // submit 값이 잘못된 경우 에러 페이지로 이동
    }

    // projectId 리턴
    return "projectId: " + projectId + "\n" + projectService.findById(projectId);

    }

    @PutMapping("/register/{projectId}")
    public String registerPut(@PathVariable Long projectId,
                              @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages,
                              @RequestPart(value = "descriptionImages", required = false) List<MultipartFile> descriptionImages,
                              @RequestPart(value = "docs", required = false) List<MultipartFile> docs,
                              @Valid @RequestPart("projectDetailDTO") ProjectDetailDTO projectDetailDTO,
                              String submit,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        log.info("projectId : " +  projectId);
        log.info("productImages : " +  productImages);
        log.info("descriptionImages : " +  descriptionImages);
        log.info("docs : " +  docs);
        log.info("projectDetailDTO : " +  projectDetailDTO);
        log.info("submit : " +  submit);

        // 유효성 검사 실패 시 처리
        if (bindingResult.hasErrors()) {
            log.info("has errors..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "error";  // 유효성 검증 실패 시 에러 페이지로 이동
        }
        log.info(submit + "submit!!----------------------------------------------------------");
        log.info(projectDetailDTO + "projectRegistDTO!!------==============================");
        // submit 값에 따른 처리
        if (submit.equals("저장")) {
            projectId = projectService.updateProject(projectDetailDTO, projectId, false, productImages, descriptionImages, docs);
        } else if (submit.equals("제출")) {
            projectId = projectService.updateProject(projectDetailDTO, projectId, true, productImages, descriptionImages, docs);
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


//
//    private final BoardService boardService;
//
//    // /board/list
//    @GetMapping("/list")
//    public void list(PageRequestDTO pageRequestDTO, Model model){
//        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
//
//        //PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);
//
//        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
//
//        log.info(responseDTO);
//
//        model.addAttribute("responseDTO", responseDTO);
//    }
//
//    @GetMapping("/register")
//    public void registerGET(){
//    }


//    @PostMapping("/register")
//    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult,
//                               RedirectAttributes redirectAttributes){
//        log.info("board POST register.....");
//
//        // @Valid처리를 통해 BoardDTO의 제약사항에 위배되면
//        // 아래 에러가 발생한다.
//        if(bindingResult.hasErrors()){
//            log.info("has errors..........");
//            // redirect전송 시 처음 1번 "errors"값을 꺼내도록 전송한다.
//            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            return "redirect:/board/register";
//        }
//
//        log.info(boardDTO);
//
//        Long bno = boardService.register(boardDTO);
//        redirectAttributes.addFlashAttribute("result", bno);
//
//        return "redirect:/board/list";
//    }

}