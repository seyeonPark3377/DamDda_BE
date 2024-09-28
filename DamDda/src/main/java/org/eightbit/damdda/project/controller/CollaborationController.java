package org.eightbit.damdda.project.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.dto.CollaborationDTO;
import org.eightbit.damdda.project.dto.CollaborationDetailDTO;
import org.eightbit.damdda.project.dto.PageRequestDTO;
import org.eightbit.damdda.project.dto.PageResponseDTO;
import org.eightbit.damdda.project.service.CollaborationService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/collab")
@RequiredArgsConstructor
@Log4j2
public class CollaborationController {

    private final CollaborationService collaborationService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Server is up and running");
    }
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestBody Map<String,String> payload) throws IOException {
        String fileName = payload.get("fileName");
        log.info(fileName);
        S3ObjectInputStream inputStream = collaborationService.downloadFIle(fileName);
        byte[] bytes = IOUtils.toByteArray(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())).build());
        return new ResponseEntity<>(bytes,headers,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CollaborationDetailDTO collaborationDetailDTO, @RequestParam Long project_id) throws JsonProcessingException {
        collaborationService.register(collaborationDetailDTO,project_id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/readDetail/{cno}")
    public ResponseEntity<?> readDetail(@PathVariable Long cno) throws JsonProcessingException {
        CollaborationDetailDTO detailDTO = collaborationService.readDetail(cno);
        return new ResponseEntity<>(detailDTO, HttpStatus.OK);
    }

    @GetMapping("/readList")
    public ResponseEntity<?> read(@RequestBody PageRequestDTO pageRequestDTO) {
        PageResponseDTO<CollaborationDTO> collaborationDTOPageResponseDTO = collaborationService.read(pageRequestDTO);
        return new ResponseEntity<>(collaborationDTOPageResponseDTO,HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long cno,@RequestParam Long user_id) throws JsonProcessingException {
        Integer response = collaborationService.delete(cno,user_id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/approval")
    public ResponseEntity<?> approvalRequest(@RequestParam List<Long> cnoList) {
        collaborationService.approvalRequest(cnoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reject")
    public ResponseEntity<?> rejectRequest(@RequestParam List<Long> cnoList) {
        collaborationService.rejectRequest(cnoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
