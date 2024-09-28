package org.eightbit.damdda.project.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eightbit.damdda.project.domain.Collaboration;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.CollaborationDTO;
import org.eightbit.damdda.project.dto.CollaborationDetailDTO;
import org.eightbit.damdda.project.dto.PageRequestDTO;
import org.eightbit.damdda.project.dto.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface CollaborationService {

    String uploadFile(MultipartFile file) throws IOException;
    S3ObjectInputStream downloadFIle(String fileName);
    void deleteFile(String fileName);
    void register(CollaborationDetailDTO collab,Long project_id) throws JsonProcessingException;
    CollaborationDetailDTO readDetail(Long rno) throws JsonProcessingException;
    PageResponseDTO<CollaborationDTO> read(PageRequestDTO pageRequestDTO);
    int delete(long id, Long user_id) throws JsonProcessingException;
    void approvalRequest(List<Long> idList);
    void rejectRequest(List<Long> idList);

    default Collaboration collabDtoToEntiy(CollaborationDetailDTO collabDTO, Project project){
       return  Collaboration.builder()
                .userId(collabDTO.getUser_id())
                .project(project)
                .collaborationText(collabDTO.getContent())
                .name(collabDTO.getTitle())
                .email(collabDTO.getEmail())
                .phoneNumber(collabDTO.getPhoneNumber())
                .build();
    }

    default CollaborationDetailDTO collabEntityToDto(Collaboration collaboration) throws JsonProcessingException {
        return CollaborationDetailDTO.builder()
                .phoneNumber(collaboration.getPhoneNumber())
                .email(collaboration.getEmail())
                .collabDocList(collaboration.getCollabDocList().stream().map(String.class::cast).collect(Collectors.toList()))
                .content(collaboration.getCollaborationText())
                .user_id(collaboration.getUserId())
                .build();
    }

    default CollaborationDTO collabEntityToDtoList(Collaboration collaboration){
        return CollaborationDTO.builder()
                .id(collaboration.getId())
                .title(collaboration.getProject().getTitle())
                .approval("대기") //확인해보기
                .CollaborateDate(collaboration.getSavedAt())
                .name(collaboration.getName())
                .build();
    }
}
