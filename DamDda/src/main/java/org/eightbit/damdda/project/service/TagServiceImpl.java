package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.Tag;
import org.eightbit.damdda.project.dto.TagDTO;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.eightbit.damdda.project.repository.TagRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    @PersistenceContext
    private EntityManager entityManager;  // EntityManager를 통해 엔티티를 관리


    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;

    public List<Tag> registerTags(List<TagDTO> tags) {
        List<String> tagNames = tags.stream()
                .map(tagDTO -> registerTag(tagDTO.getName()))  // 각각의 태그 등록
                .collect(Collectors.toList());

        return tagNames.stream()
                .map(tagName -> getTag(tagName))  // 태그 등록 서비스 호출
                .collect(Collectors.toList());
    }

    @Override
    public String registerTag(String tagName) {
        // 태그가 존재하는지 확인
        Tag tag = tagRepository.findByName(tagName);

        if (tag == null) {
            // 태그가 없으면 새로 생성 (usageFrequency는 0, 전달받은 projectIds로 프로젝트 엔티티 생성)
            // 프로젝트가 존재하는지 확인

            tag = Tag.builder()
                    .name(tagName)
                    .usageFrequency(0)  // 새로 등록이므로 사용 빈도는 0
                    .build();

            // 태그 저장
            tagName = tagRepository.save(tag).getName();
            log.info("Tag created: {}", tag);

        }
        return tagName;
    }

    @Override
    // 태그 등록 및 프로젝트에 태그 추가
    public List<Tag> addProjectToTags(List<TagDTO> tagDTOs, Long projectId) {
        registerTags(tagDTOs);
        // 프로젝트가 존재하는지 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Tag> projectTags = project.getTags();

        for(TagDTO tagDTO : tagDTOs) {

        }

        // 각 TagDTO의 태그 이름을 추출하고 등록한 후, 결과를 리스트로 반환
        List<String> tagNames = tagDTOs.stream()
                .map(tagDTO -> addProjectToTag(tagDTO.getName(), projectId))  // 각각의 태그 등록
                .collect(Collectors.toList());

        return tagNames.stream()
                .map(tagName -> getTag(tagName))  // 태그 등록 서비스 호출
                .collect(Collectors.toList());
    }


    @Override
    public String addProjectToTag(String tagName, Long projectId) {
        // 태그가 존재하는지 확인
        Tag tag = tagRepository.findByName(tagName);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // 프로젝트를 영속성 컨텍스트에서 분리(detach)
//        entityManager.detach(project);


        if (tag.getProjects() == null || tag.getProjects().isEmpty()) {
            log.info("-------tag안에 프로젝트 비어있음----------");
//            tag.setProjects(Collections.singletonList(project));
//            tag.setUsageFrequency(1);

            List<Project> projectList = new ArrayList<>();
            projectList.add(project);

            tag.setProjects(projectList);
            // 태그가 존재하면 usageFrequency +1
            tag.setUsageFrequency(1);

//            Tag updatedTag = Tag.builder()
//                    .name(tag.getName())
//                    .usageFrequency(1)
//                    .projects(projectList)
//                    .build();
//            tagRepository.save(updatedTag);

        } else {
//            // 프로젝트 객체를 영속성 컨텍스트에서 분리 (detach)하여 독립적으로 처리
//            entityManager.detach(project);
            // 기존 프로젝트 목록에 프로젝트 추가 (중복 방지 가능)

            log.info("-------태그 안에 프로젝트 없어서 추가----------" + tag.getName() + tag.getProjects());

            // 프로젝트 리스트 복사 후 추가 (새 리스트 생성)
            List<Project> updatedProjects = new ArrayList<>(tag.getProjects());
            updatedProjects.add(project);

//                List<Project> projects = tag.getProjects();
//                projects.add(project);
            tag.setProjects(updatedProjects);
            // 태그가 존재하면 usageFrequency +1
            tag.setUsageFrequency(tag.getUsageFrequency() + 1);

            log.info("Tag updated: {}", tag);

        }

        return tagName;

    }

    @Override
    // 태그 등록 및 프로젝트에 태그 추가
    public List<Tag> delProjectFromTags(Project project) {

        List<Tag> projectTags = project.getTags();

        // 태그 리스트를 DTO 리스트로 변환
        List<TagDTO> tagDTOs = projectTags.stream()
                .map(tag -> TagDTO.builder()
                        .name(tag.getName())
                        .usageFrequency(tag.getUsageFrequency())
                        .build())
                .collect(Collectors.toList());


        // 각 TagDTO의 태그 이름을 추출하고 등록한 후, 결과를 리스트로 반환
        List<String> tagNames = projectTags.stream()
                .map(tagDTO -> delProjectFromTag(tagDTO.getName(), project))  // 각각의 태그 등록
                .collect(Collectors.toList());

        return tagNames.stream()
                .map(tagName -> getTag(tagName))  // 태그 등록 서비스 호출
                .collect(Collectors.toList());
    }

    @Override
    public String delProjectFromTag(String tagName, Project project){
        // 태그가 존재하는지 확인
        Tag tag = tagRepository.findByName(tagName);

        // 프로젝트 리스트 복사 후 추가 (새 리스트 생성)
        List<Project> updatedProjects = new ArrayList<>(tag.getProjects());
        updatedProjects.remove(project);

        tag.setProjects(updatedProjects);
        tag.setUsageFrequency(tag.getUsageFrequency() - 1);
//
//        Tag updatedTag = Tag.builder()
//                .id(tag.getId())
//                .name(tag.getName())
//                .usageFrequency(tag.getUsageFrequency() - 1)
//                .projects(updatedProjects)
//                .build();
//        tagRepository.save(updatedTag);

        log.info("Tag updated: {}", tag);

        return tagName;
    }

    @Override
    public TagDTO getTagDTO(String tagName) {

        Tag tag = tagRepository.findByName(tagName);

        if (tag == null) {
            return null;
        }

        // List<Project>에서 프로젝트 ID만 추출하여 List<Long>으로 변환
        List<Long> projectIds = tag.getProjects().stream()
                .map(project -> project.getId())  // 프로젝트의 ID만 추출
                .collect(Collectors.toList());


        return TagDTO.builder()
                .name(tag.getName())
                .usageFrequency(tag.getUsageFrequency())
                .projectIds(projectIds)  // 추출된 프로젝트 ID 리스트 설정
                .build();
    }


    @Override
    public Tag getTag(String tagName) {
        return tagRepository.findByName(tagName);
    }

}
