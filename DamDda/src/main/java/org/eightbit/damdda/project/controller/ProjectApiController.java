package org.eightbit.damdda.project.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.dto.ProjectRegistDTO;
import org.eightbit.damdda.project.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
@Log4j2
@RequiredArgsConstructor
public class ProjectApiController {

    @GetMapping("/index")
    public String index(Model model) {
        return "This is the project index page.";
    }

    private final ProjectService projectService;

    @PostMapping("/register")
    public String registerPost(@RequestBody ProjectRegistDTO projectRegistDTO, String submit, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 유효성 검사 실패 시 처리
        if (bindingResult.hasErrors()) {
            log.info("has errors..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "error";  // 유효성 검증 실패 시 에러 페이지로 이동
        }
        // 프로젝트 ID 변수 선언
        Long projectId = null;
        log.info(submit + "submit!!----------------------------------------------------------");
        log.info(projectRegistDTO + "projectRegistDTO!!------==============================");
        // submit 값에 따른 처리
        if (submit.equals("저장")) {
            projectId = projectService.register(projectRegistDTO, false);
        } else if (submit.equals("제출")) {
            projectId = projectService.register(projectRegistDTO, true);
        } else {
            redirectAttributes.addFlashAttribute("errors", "Invalid submit action.");
            return "error";  // submit 값이 잘못된 경우 에러 페이지로 이동
        }

        // projectId 리턴
        return "projectId: " + projectId + "\n" +  projectService.findById(projectId);
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
