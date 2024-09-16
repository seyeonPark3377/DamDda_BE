package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Category;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectDocument;
import org.eightbit.damdda.project.domain.Tag;
import org.eightbit.damdda.project.dto.ProjectDTO;
import org.eightbit.damdda.project.dto.ProjectRegistDTO;
import org.eightbit.damdda.project.repository.*;
import org.springframework.stereotype.Service;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDocumentRepository projectDocumentRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;

//    private Member member = new Member();

    @Override
    public Long register(ProjectRegistDTO projectRegistDTO, boolean submit){
        log.info(submit);
        log.info(projectRegistDTO);
        log.info("service----------------------------------------");

        // projeectRegisterDTO to ProjectDocument / ProjectImage / Project / Tag


        Project project = Project.builder()
//                .member(member)
//                .category(projectRegistDTO.getCategory())
//                .tags(projectRegistDTO.getTags())
                .title(projectRegistDTO.getTitle())
                .description(projectRegistDTO.getDescription())
                .startDate(projectRegistDTO.getStartDate())
                .endDate(projectRegistDTO.getEndDate())
                .targetFunding(projectRegistDTO.getTargetFunding())
                .fundsReceive(projectRegistDTO.getFundsReceive())
                .supporterCount(projectRegistDTO.getSupporterCount())
                .viewCount(0L)
                .likeCount(projectRegistDTO.getLikeCount())
                .thumbnailUrl(projectRegistDTO.getThumbnailUrl())
                .submitAt(submit ? Timestamp.valueOf(LocalDateTime.now()) : null)
                .build();
        log.info("project----------------------------------------");

        return projectRepository.save(project).getId();
    }

    @Override
    public Project findById(Long id) {
        Optional<Project> result = projectRepository.findById(id);

        return result.orElseThrow();
    }


}
