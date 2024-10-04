package org.eightbit.damdda.project.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.eightbit.damdda.project.dto.CollaborationDTO;
import org.eightbit.damdda.project.dto.CollaborationDetailDTO;
import org.eightbit.damdda.project.dto.PageRequestDTO;
import org.eightbit.damdda.project.dto.PageResponseDTO;
import org.eightbit.damdda.project.service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collab")
@RequiredArgsConstructor
@Log4j2
public class CollaborationController {

    private final CollaborationService collaborationService;


    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String fileName) throws IOException {
        S3ObjectInputStream inputStream = collaborationService.downloadFIle(fileName);
        byte[] bytes = IOUtils.toByteArray(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())).build());
        return new ResponseEntity<>(bytes,headers,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestParam("jsonData") String jsonDataStr,
                                       @RequestParam("collabDocList") List<MultipartFile> collabDocList,
                                       @RequestParam Long project_id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CollaborationDetailDTO collaborationDetailDTO = mapper.registerModule(new JavaTimeModule()).readValue(jsonDataStr, CollaborationDetailDTO.class);

        collaborationDetailDTO.setCollabDocList(convertToObjectList(collabDocList));

        collaborationService.register(collaborationDetailDTO,project_id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/readDetail/{cno}")
    public ResponseEntity<?> readDetail(@PathVariable Long cno) throws JsonProcessingException {
        CollaborationDetailDTO detailDTO = collaborationService.readDetail(cno);
        return new ResponseEntity<>(detailDTO, HttpStatus.OK);
    }

    @GetMapping("/readList")
    public ResponseEntity<?> read(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
        PageResponseDTO<CollaborationDTO> collaborationDTOPageResponseDTO = collaborationService.read(pageRequestDTO);
        return new ResponseEntity<>(collaborationDTOPageResponseDTO,HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long cno,@RequestParam Long user_id) throws JsonProcessingException {
        Integer response = collaborationService.delete(cno,user_id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/approval")
    public ResponseEntity<?> approvalRequest(@RequestBody List<Long> cnoList) {
        collaborationService.approvalRequest(cnoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reject")
    public ResponseEntity<?> rejectRequest(@RequestBody List<Long> cnoList) {
        collaborationService.rejectRequest(cnoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //multipart -> object로 변환하는 함수.
    private List<Object> convertToObjectList(List<MultipartFile> multipartFiles) {
        List<Object> objectList = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            objectList.add(file);
        }
        return objectList;
    }

}
