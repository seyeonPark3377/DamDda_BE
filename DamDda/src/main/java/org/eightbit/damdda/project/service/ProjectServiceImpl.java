package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.admin.domain.AdminApproval;
import org.eightbit.damdda.admin.repository.AdminApprovalRepository;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.repository.MemberRepository;
import org.eightbit.damdda.order.Repository.SupportingProjectRepository;
import org.eightbit.damdda.project.domain.*;
import org.eightbit.damdda.project.dto.*;
import org.eightbit.damdda.project.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {


    //    private final ProjectDocumentRepository projectDocumentRepository;
//    private final ProjectImageRepository projectImageRepository;
    private final ProjectRepository projectRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final ImgService imgService;
    private final DocService docService;
    private final ProjectDocumentRepository projectDocumentRepository;
    private final ProjectImageRepository projectImageRepository;
    private final MemberRepository memberRepository;
    private final SupportingProjectRepository supportingProjectRepository;
    private final AdminApprovalRepository adminApprovalRepository;
    private final LikedProjectRepository likedProjectRepository;
//    private final TagRepository tagRepository;
//    private final CategoryRepository categoryRepository;
//    private Member member = new Member();


//    public List<Project> getProjectsByIds(List<Long> projectIds) {
//        return projectRepository.findAllById(projectIds);
//    }


    @Override
    public PageResponseDTO<ProjectBoxHostDTO> getListProjectBoxHostDTO(Long memberId, PageRequestDTO pageRequestDTO) {
        Pageable pageable =
                PageRequest.of(pageRequestDTO.getPage() <= 0 ?
                                0 : pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                        Sort.by("id").ascending());
        Page<Project> result = projectRepository.listOfProjectBoxHost(memberId, pageable);


        List<ProjectBoxHostDTO> dtoList = result.getContent().stream()
                .map(project -> {
                    AdminApproval adminApproval = adminApprovalRepository.findByProjectId(project.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Approval not found for projectId: " + project.getId()));

                    return ProjectBoxHostDTO.builder()
                            .title(project.getTitle())
                            .description(project.getDescription())
                            .thumbnailUrl(project.getThumbnailUrl())
                            .fundsReceive(project.getFundsReceive())
                            .targetFunding(project.getTargetFunding())
                            .nickName(project.getMember().getNickname())
                            .endDate(project.getEndDate())
                            .approval(adminApproval.getApproval())
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<ProjectBoxHostDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();

    }


    public ProjectDetailHostDTO readProjectDetailHost(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(projectId);
        List<String> productImages = projectImages.stream()
                .filter(projectImage -> projectImage.getImageType().getImageType().equals("PRODUCT_IMAGE"))
                .map(ProjectImage::getUrl)
                .collect(Collectors.toList());

        List<Tag> tags = project.getTags();
        List<String> tagDTOs = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        AdminApproval adminApproval = adminApprovalRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found for projectId: " + project.getId()));



        ProjectDetailHostDTO projectDetailHostDTO = ProjectDetailHostDTO.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .fundsReceive(project.getFundsReceive())
                .targetFunding(project.getTargetFunding())
                .category(project.getCategory().getName())
                .nickName(project.getMember().getNickname())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .supporterCnt(supportingProjectRepository.countByProject(project))
                .approval(adminApproval.getApproval())
                .rejectMessage(adminApproval.getApprovalText())
                .likerCnt(likedProjectRepository.countByProjectId(projectId))
                .productImages(productImages)
                .tags(tagDTOs)
                .build();

        return projectDetailHostDTO;
    }

    public ProjectResponseDetailDTO readProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(projectId);
        List<String> productImages = projectImages.stream()
                .filter(projectImage -> projectImage.getImageType().getImageType().equals("PRODUCT_IMAGE"))
                .map(ProjectImage::getUrl)
                .collect(Collectors.toList());

        List<String> descriptionImages = projectImages.stream()
                .filter(projectImage -> projectImage.getImageType().getImageType().equals("PRODUCT_DESCRIPTION_IMAGE"))
                .map(ProjectImage::getUrl)
                .collect(Collectors.toList());
//        List<String> productImages = new ArrayList<>();
//        List<String> descriptionImages = new ArrayList<>();
//        for(ProjectImage projectImage : projectImages){
//            if(projectImage.getImageType().getImageType().equals("PRODUCT_IMAGE")){
//                productImages.add(projectImage.getUrl());
//            } else if(projectImage.getImageType().getImageType().equals("PRODUCT_DESCRIPTION_IMAGE")){
//                descriptionImages.add(projectImage.getUrl());
//            }
//        }
        List<Tag> tags = project.getTags();
        List<String> tagDTOs = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        project.setViewCnt(project.getViewCnt() + 1);
//        project.setSupporterCnt(supportingProjectRepository.countByProject(project));

        ProjectResponseDetailDTO projectResponseDetailDTO = ProjectResponseDetailDTO.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .descriptionDetail(project.getDescriptionDetail())
                .fundsReceive(project.getFundsReceive())
                .targetFunding(project.getTargetFunding())
                .category(project.getCategory().getName())
                .nickName(project.getMember().getNickname())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .supporterCnt(supportingProjectRepository.countByProject(project))
                .likeCnt(likedProjectRepository.countByProjectId(projectId))
//                .likeCnt(project.getLikeCnt())
                .thumbnailUrl(project.getThumbnailUrl())
                .productImages(productImages)
                .descriptionImages(descriptionImages)
                .tags(tagDTOs)
                .build();

        return projectResponseDetailDTO;

    }

    @Override
    public String delProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Category delCategory = categoryService.delProjectFromCategory(projectId, project.getCategory().getName());
        List<Tag> delTags = tagService.delProjectFromTags(project);


        boolean delImg = imgService.deleteImageFiles(projectImageRepository.findAllByProjectId(projectId));
        project.setThumbnailUrl(null);

        docService.deleteDocFiles(projectDocumentRepository.findByProjectId(projectId));

        if (delImg) {

            // 삭제 시간을 현재 시간으로 설정
//            project.setDeletedAt(Timestamp.from(Instant.now()));
            try {
                // DateEntity 클래스에서 deletedAt 필드를 가져옴
                Field deletedAtField = DateEntity.class.getDeclaredField("deletedAt");
                deletedAtField.setAccessible(true);  // private 필드에 접근 가능하도록 설정

                // 현재 시간으로 deletedAt 필드 설정
                deletedAtField.set(project, Timestamp.from(Instant.now()));

                // 변경 사항 저장
                projectRepository.save(project);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();  // 예외 처리
            }

            // 변경 사항을 저장하여 소프트 삭제 수행
            projectRepository.save(project);
//            projectRepository.delete(project);
        }
        return delCategory.getName();
    }

    @Override
    public Long register(Long memberId,
                         ProjectDetailDTO projectDetailDTO,
                         boolean submit,
                         List<MultipartFile> productImages,
                         List<MultipartFile> descriptionImages,
                         List<MultipartFile> docs) {

        Category category = categoryService.registerCategory(projectDetailDTO.getCategory());

        List<Tag> tags = tagService.registerTags(projectDetailDTO.getTags());

        Member member = memberRepository.getById(memberId);

        // 1. 프로젝트 생성 및 저장 (ID 생성)
        Project project = Project.builder()
                .member(member)
                .tags(tags)
                .category(category)
                .title(projectDetailDTO.getTitle())
                .description(projectDetailDTO.getDescription())
                .descriptionDetail(projectDetailDTO.getDescriptionDetail())
                .startDate(projectDetailDTO.getStartDate())
                .endDate(projectDetailDTO.getEndDate())
                .targetFunding(projectDetailDTO.getTargetFunding())
                .fundsReceive(0L)
//                .supporterCnt(0L)  // 기본값 0
                .viewCnt(0L)       // 기본값 0
                .likeCnt(0L)       // 기본값 0
                .thumbnailUrl("")  // 기본값은 빈 문자열로 설정
                .submitAt(submit ? Timestamp.valueOf(LocalDateTime.now()) : null)  // 제출 시간 설정
                .build();


        // 프로젝트를 저장해 ID를 생성
        project = projectRepository.save(project);
        final Long projectId = project.getId();

        // 2. 카테고리 설정
        category = categoryService.addProjectToCategory(projectId, projectDetailDTO.getCategory());  // 카테고리 등록 서비스 호출
        project.setCategory(category);  // 카테고리 설정


        // 3. 태그 설정
        tags = tagService.addProjectToTags(projectDetailDTO.getTags(), projectId);
        project.setTags(tags);  // 프로젝트에 태그 추가


        imgService.saveImages(project, productImages, descriptionImages);

        docService.saveDocs(project, docs);

        // 5. 최종 프로젝트 저장
        return project.getId();
    }

    @Override
    public Long updateProject(ProjectDetailDTO projectDetailDTO,
                              Long projectId,
                              boolean submit,
                              List<MultipartFile> productImages,
                              List<MultipartFile> descriptionImages,
                              List<MultipartFile> docs) {


        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Category delCategory = categoryService.delProjectFromCategory(projectId, project.getCategory().getName());
//        Category newCategory = categoryService.registerCategory(projectDetailDTO.getCategory());
        Category newCategory = categoryService.addProjectToCategory(projectId, projectDetailDTO.getCategory());

        List<Tag> delTags = tagService.delProjectFromTags(project);
//        List<Tag> newTags = tagService.registerTags(projectDetailDTO.getTags());
        List<Tag> newTags = tagService.addProjectToTags(projectDetailDTO.getTags(), projectId);

        Boolean delImg = imgService.deleteImageFiles(projectImageRepository.findAllByProjectId(projectId));
        project.setThumbnailUrl(null);

        docService.deleteDocFiles(projectDocumentRepository.findByProjectId(projectId));

        project.setTags(newTags);
        project.setCategory(newCategory);
        project.setTitle(projectDetailDTO.getTitle());
        project.setDescription(projectDetailDTO.getDescription());
        project.setDescriptionDetail(projectDetailDTO.getDescriptionDetail());
        project.setStartDate(projectDetailDTO.getStartDate());
        project.setEndDate(projectDetailDTO.getEndDate());
        project.setTargetFunding(projectDetailDTO.getTargetFunding());
//        project.setFundsReceive(0L);  // 기본값 0
//        project.setSupporterCnt(0L);  // 기본값 0
//        project.setViewCnt(0L);       // 기본값 0
//        project.setLikeCnt(0L);       // 기본값 0
//        project.setThumbnailUrl("");  // 기본값은 빈 문자열로 설정
        project.setSubmitAt(submit ? Timestamp.valueOf(LocalDateTime.now()) : null);  // 제출 시간 설정

        imgService.saveImages(project, productImages, descriptionImages);

        docService.saveDocs(project, docs);

        // 5. 최종 프로젝트 저장
        return project.getId();
    }


    @Override
    public Project findById(Long id) {
        Optional<Project> result = projectRepository.findById(id);

        return result.orElseThrow();
    }


}
