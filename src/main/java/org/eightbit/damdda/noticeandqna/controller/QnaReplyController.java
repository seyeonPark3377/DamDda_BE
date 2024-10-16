package org.eightbit.damdda.noticeandqna.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.eightbit.damdda.noticeandqna.dto.QnaQuestionDTO;
import org.eightbit.damdda.noticeandqna.dto.QnaReplyDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/damdda/qna/question")
public class QnaReplyController {

    @PostMapping
    @Operation(summary = "Q&A 답글 생성", description = "Q&A 질문에 대한 답글을 생성합니다.")
    public ResponseEntity<QnaReplyDTO> registerQna(@RequestBody QnaReplyDTO qnaReplyDTO) {
        System.out.println(qnaReplyDTO);
        return null;
    }

    @GetMapping
    @Operation(summary = "Q&A 답글 조회", description = "Q&A 질문에 대한 답글을 조회합니다.")
    public ResponseEntity<Page<QnaReplyDTO>> getQna(@RequestParam String parentReplyId) {
        System.out.println(parentReplyId);
        return null;
    }

    @DeleteMapping("/{qnaQuestionId}")
    @Operation(summary = "Q&A 답글 삭제", description = "Q&A 질문에 대한 답글을 삭제합니다.")
    public ResponseEntity<Map<String, Boolean>> deleteQna(@PathVariable("qnaQuestionId") String parentReplyId){
        System.out.println(parentReplyId);
        return null;
    }

    @PutMapping("/{qnaQuestionId}")
    @Operation(summary = "Q&A 답글 수정", description = "Q&A 질문에 대한 답글을 수정합니다.")
    public ResponseEntity<QnaReplyDTO> modifyQna(@RequestBody QnaReplyDTO qnaReplyDTO, @PathVariable("qnaQuestionId") String parentReplyId){
        System.out.println(parentReplyId);
        System.out.println(qnaReplyDTO);
        return null;
    }
}