package org.eightbit.damdda.project.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.json.JSONParser;
import org.eightbit.damdda.project.domain.Collaboration;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.CollaborationDTO;
import org.eightbit.damdda.project.dto.CollaborationDetailDTO;
import org.eightbit.damdda.project.dto.PageRequestDTO;
import org.eightbit.damdda.project.dto.PageResponseDTO;
import org.eightbit.damdda.project.repository.CollaborationRepository;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CollaborationServiceImpl implements CollaborationService{

    @Value("${cloud.aws.credentials.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;
    private final CollaborationRepository collaborationRepository;
    private final ProjectRepository projectRepository;

    //파일 업로드
    @Override
    @Transactional
    public String uploadFile(MultipartFile file) throws IOException{
        String fileName = "board/"+UUID.randomUUID()+"_"+file.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,file.getInputStream(),null);
        amazonS3.putObject(putObjectRequest);
        return fileName;
    }

    //파일 다운로드
    @Override
    @Transactional
    public S3ObjectInputStream downloadFIle(String fileName){
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
        S3Object s3Object = amazonS3.getObject(getObjectRequest);
        return s3Object.getObjectContent();
    }

    //파일 삭제 메서드
    @Override
    @Transactional
    public void deleteFile(String fileName){
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    //협업 제안
    @Override
    @Transactional
    public void register(@Valid CollaborationDetailDTO collab, Long project_id) throws JsonProcessingException {
        log.info(collab);
        //project_id를 이용해서 project 객체를 찾아냄.
        Project project = projectRepository.findById(project_id).orElseThrow(()->new EntityNotFoundException("project not found")); //long 타입으로 바꿔야 한다.
        //매핑 collaborationdto -> collaboration
        Collaboration collaboration = collabDtoToEntiy(collab, project);
        //파일 업로드 처리
        if(collab.getCollabDocList() != null && !collab.getCollabDocList().isEmpty()){
            List<String> fileList = new ArrayList<>();
            for(Object file : collab.getCollabDocList()){
                try{
                    String fileName = uploadFile((MultipartFile) file);
                    fileList.add(fileName);
                }catch(IOException e){
                    log.info(e.getMessage());
                }
            }
            collaboration.setCollabDocList(fileList);
        }
        collaborationRepository.save(collaboration);
    }

    //협업 상세 보기
    @Override
    @Transactional
    public CollaborationDetailDTO readDetail(Long rno) throws JsonProcessingException {
        Collaboration collaboration = collaborationRepository.findById(rno).orElseThrow(()-> new EntityNotFoundException("collaboration not found"));
        //엔티티로 바꾸기
        CollaborationDetailDTO collaborationDetailDTO = collabEntityToDto(collaboration);
        return collaborationDetailDTO;
    }

    //협업 리스트 보기
    @Override
    @Transactional
    public PageResponseDTO<CollaborationDTO> read(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Collaboration> collaborationPage = collaborationRepository.findAllWhereReceiverDeletedAtIsNull(pageable);

        //엔티티를 dto로 변환.
        List<CollaborationDTO> dtoList = collaborationPage.stream().map(collaboration-> {
            return collabEntityToDtoList(collaboration);
        }).collect(Collectors.toList());

        return PageResponseDTO.<CollaborationDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)collaborationPage.getTotalElements())
                .build();
    }

    //협업 삭제하기
    @Override
    @Transactional
    public int delete(long id, Long user_id) throws JsonProcessingException {
        Optional<Collaboration> collaborationOpt = collaborationRepository.findById(id);
        Collaboration collaboration = collaborationOpt.orElseThrow();
        if(collaboration.getUserId() == user_id){ //협업 제안자가 삭제를 한다면
            // 연관된 파일 삭제
            collaboration.addSenderDeletedAt();
            for(String collabDoc :collaboration.getCollabDocList() ) {
                deleteFile(collabDoc); //ncp에서 제거.
            }
            collaboration.removeCollabDocList();
        }else{ //협업 제안을 받은 사람이 삭제를 한다면 > 게시판에서만 삭제됨.
            collaboration.addReceiverDeletedAt();
        }

        collaborationRepository.deleteById(id);
        return 1;
    }

    //협업 승인
    @Override
    @Transactional
    public void approvalRequest(List<Long> idList) {
        collaborationRepository.changeApproval("승인",idList);
    }

    //협업 거절
    @Override
    @Transactional
    public void rejectRequest(List<Long> idList) {
        collaborationRepository.changeApproval("거절",idList);
    }


}

