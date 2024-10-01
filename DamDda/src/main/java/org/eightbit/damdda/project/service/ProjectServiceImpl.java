package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.admin.domain.AdminApproval;
import org.eightbit.damdda.admin.service.AdminApprovalService;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.service.MemberService;
import org.eightbit.damdda.order.service.SupportingProjectService;
import org.eightbit.damdda.project.domain.LikedProject;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.domain.ProjectImage;
import org.eightbit.damdda.project.domain.Tag;
import org.eightbit.damdda.project.dto.*;
import org.eightbit.damdda.project.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {


    //    private final ProjectDocumentRepository projectDocumentRepository;
//    private final ProjectImageRepository projectImageRepository;
    private final TagService tagService;
    private final ImgService imgService;
    private final DocService docService;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;
    private final AdminApprovalService adminApprovalService;
    private final SupportingProjectService supportingProjectService;
    private final ProjectRepository projectRepository;
    private final LikedProjectRepository likedProjectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectDocumentRepository projectDocumentRepository;
//    private final CategoryRepository categoryRepository;


//    private final TagRepository tagRepository;
//    private final CategoryRepository categoryRepository;
//    private Member member = new Member();


//    public List<Project> getProjectsByIds(List<Long> projectIds) {
//        return projectRepository.findAllById(projectIds);
//    }

    @Override
    public Long getMemberId(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("해당 아이디와 일치하는 프로젝트 없음! Project not found with ID: " + projectId));
        return project.getMember().getId();
    }

    @Override
    public PageResponseDTO<ProjectBoxDTO> getProjects(PageRequestDTO pageRequestDTO, Long memberId, int page, int size, String category, String search, String progress, List<String> sortConditions) {
        PageRequest pageable = PageRequest.of(page - 1, size);  // PageRequest를 사용해 페이지와 크기를 지정
        log.info(progress + "1111111111111111111111111");

        Page<Project> projects;
        if (!sortConditions.isEmpty() && "fundsReceive".equals(sortConditions.get(0))) {
            // sort 조건 중 첫 번째가 "fundsReceive"일 때
            List<Project> sortProjects = projectRepository.findAllSortedByFundingRatio(category, search, progress);
            // 페이지 번호에 맞는 결과를 가져오기 위한 subList
//            int start = (int) pageable.getOffset();
//            int end = Math.min((start + pageable.getPageSize()), sortProjects.size());
            projects = new PageImpl<>(sortProjects, pageable, sortProjects.size());
        } else {
            // 그 외의 경우
            projects = projectRepository.findProjects(memberId, category, search, progress, sortConditions, pageable);
        }

//                Page<Project> projects = projectRepository.findProjects(memberId, category, search, progress, sortConditions, pageable);

                     log.info(progress + "1111111111111111111111111");

        final List<Long> likedProjectId;
        if (memberId != null) {
            likedProjectId = likedProjectRepository.findAllByMemberId(memberId).stream()
                    .map(likedProject -> likedProject.getProject().getId())
                    .collect(Collectors.toList());
            log.info(likedProjectId);
        } else {
            likedProjectId = new ArrayList<>();  // null인 경우 빈 리스트로 초기화
        }


        // 3. approval이 1인 AdminApproval 항목 조회
        List<AdminApproval> approvedAdminApprovals = adminApprovalService.findAllByApproval(1);

        // 4. approval이 1인 프로젝트 ID 목록 생성
        Set<Long> approvedProjectIds = approvedAdminApprovals.stream()
                .map(adminApproval -> adminApproval.getProject().getId())
                .collect(Collectors.toSet());

        // 5. Project에서 approval이 1인 프로젝트들만 필터링하여 DTO 변환
        List<ProjectBoxDTO> dtoList = projects.getContent().stream()
                .filter(project -> approvedProjectIds.contains(project.getId()))  // approval이 1인 것만 필터링
                .map(project -> ProjectBoxDTO.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .description(project.getDescription())
                        .thumbnailUrl(project.getThumbnailUrl())
                        .fundsReceive(project.getFundsReceive())
                        .targetFunding(project.getTargetFunding())
                        .nickName(project.getMember().getNickname())
                        .endDate(project.getEndDate())
                        .Liked(likedProjectId.contains(project.getId()))  // 좋아요 여부는 기본적으로 false
                        .build())
                .collect(Collectors.toList());

//        log.info("11111111111111111111111111111111111"+dtoList.size());



// 서비스 레이어에서 페이지네이션 적용
        int start = (page - 1) * size;
        int end = Math.min(start + size, dtoList.size());
        List<ProjectBoxDTO> paginatedList = dtoList.subList(start, end);

// PageResponseDTO로 반환
        return PageResponseDTO.<ProjectBoxDTO>withAll()
                .pageRequestDTO(pageRequestDTO)  // 페이지 요청 정보
                .dtoList(paginatedList)  // 페이지네이션된 DTO 리스트
                .total(dtoList.size())   // 전체 데이터 수
                .build();
    }


    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public PageResponseDTO<ProjectBoxDTO> findSortedProjects(Long memberId, PageRequestDTO pageRequestDTO, List<String> sortConditions) {
//        // 1. Pageable 설정 (페이지네이션 + 동적 정렬 적용)
//        Pageable pageable = PageRequest.of(
//                pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
//                pageRequestDTO.getSize(),
//                getSort(sortConditions));  // 동적 정렬 처리
//
//        final List<Long> likedProjectId;
//        if (memberId != null) {
//            likedProjectId = likedProjectRepository.findAllByMemberId(memberId).stream()
//                    .map(likedProject -> likedProject.getProject().getId())
//                    .collect(Collectors.toList());
//        } else {
//            likedProjectId = new ArrayList<>();  // null인 경우 빈 리스트로 초기화
//        }
//        // 2. 모든 프로젝트 페이징 조회 (deletedAt이 null인 경우만 조회)
//        Page<Project> projects = projectRepository.findAllByDeletedAtIsNull(pageable);
//
//        // 3. approval이 1인 AdminApproval 항목 조회
//        List<AdminApproval> approvedAdminApprovals = adminApprovalService.findAllByApproval(1);
//
//        // 4. approval이 1인 프로젝트 ID 목록 생성
//        Set<Long> approvedProjectIds = approvedAdminApprovals.stream()
//                .map(adminApproval -> adminApproval.getProject().getId())
//                .collect(Collectors.toSet());
//
//        // 5. Project에서 approval이 1인 프로젝트들만 필터링하여 DTO 변환
//        List<ProjectBoxDTO> dtoList = projects.getContent().stream()
//                .filter(project -> approvedProjectIds.contains(project.getId()))  // approval이 1인 것만 필터링
//                .map(project -> ProjectBoxDTO.builder()
//                        .title(project.getTitle())
//                        .description(project.getDescription())
//                        .thumbnailUrl(project.getThumbnailUrl())
//                        .fundsReceive(project.getFundsReceive())
//                        .targetFunding(project.getTargetFunding())
//                        .nickName(project.getMember().getNickname())
//                        .endDate(project.getEndDate())
//                        .Liked(likedProjectId.contains(project.getId()))  // 좋아요 여부는 기본적으로 false
//                        .build())
//                .collect(Collectors.toList());
//
//        // 6. PageResponseDTO로 반환
//        return PageResponseDTO.<ProjectBoxDTO>withAll()
//                .pageRequestDTO(pageRequestDTO)  // 페이지 요청 정보
//                .dtoList(dtoList)  // 필터링된 DTO 리스트
//                .total(dtoList.size())
//                .build();
//    }

    // 동적 정렬 설정
//    private Sort getSort(List<String> sortConditions) {
//        Sort sort = Sort.unsorted();
//        for (String condition : sortConditions) {
//            switch (condition) {
////                case "fundsReceive":
////                    sort = sort.and(Sort.by(Sort.Direction.DESC, "fundsReceive"));
////                    break;
//                case "targetFunding":
//                    sort = sort.and(Sort.by(Sort.Direction.DESC, "targetFunding"));
//                    break;
//                case "viewCnt":
//                    sort = sort.and(Sort.by(Sort.Direction.DESC, "viewCnt"));
//                    break;
//                case "supporterCnt":
//                    sort = sort.and(Sort.by(Sort.Direction.DESC, "supporterCnt"));
//                    break;
//                case "likeCnt":
//                    sort = sort.and(Sort.by(Sort.Direction.DESC, "likeCnt"));
//                    break;
//                case "registDate":
//                    sort = sort.and(Sort.by(Sort.Direction.ASC, "createdAt"));
//                    break;
//                case "endDate":
//                    sort = sort.and(Sort.by(Sort.Direction.ASC, "endDate"));
//                    break;
//            }
//        }
//        return sort;
//    }


    @Override
    public PageResponseDTO<ProjectBoxDTO> getListProjectBoxLikeDTO(Long memberId, PageRequestDTO pageRequestDTO) {
        Pageable pageable =
                PageRequest.of(pageRequestDTO.getPage() <= 0 ?
                                0 : pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                        Sort.by("id").ascending());


        Page<LikedProject> likedProjects = likedProjectRepository.findAllByMember_Id(memberId, pageable);

        List<AdminApproval> approvedAdminApprovals = adminApprovalService.findAllByApproval(1);

        // 3. approval이 1인 프로젝트 ID 목록 생성
        Set<Long> approvedProjectIds = approvedAdminApprovals.stream()
                .map(adminApproval -> adminApproval.getProject().getId())
                .collect(Collectors.toSet());

        // 4. LikedProject에서 approval이 1인 프로젝트들만 필터링
        List<ProjectBoxDTO> dtoList = likedProjects.getContent().stream()
                .map(LikedProject::getProject)
                .filter(project -> approvedProjectIds.contains(project.getId()))  // approval이 1인 것만 필터링
                .map(project -> ProjectBoxDTO.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .description(project.getDescription())
                        .thumbnailUrl(project.getThumbnailUrl())
                        .fundsReceive(project.getFundsReceive())
                        .targetFunding(project.getTargetFunding())
                        .nickName(project.getMember().getNickname())
                        .endDate(project.getEndDate())
                        .Liked(true)
                        .build())
                .collect(Collectors.toList());


        return PageResponseDTO.<ProjectBoxDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(dtoList.size())
                .build();

    }


    @Override
    public PageResponseDTO<ProjectBoxHostDTO> getListProjectBoxHostDTO(Long memberId, PageRequestDTO pageRequestDTO) {
        Pageable pageable =
                PageRequest.of(pageRequestDTO.getPage() <= 0 ?
                                0 : pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                        Sort.by("id").ascending());

        Page<Project> result = projectRepository.listOfProjectBoxHost(memberId, pageable);


        final List<Long> likedProjectId;
        if (memberId != null) {
            likedProjectId = likedProjectRepository.findAllByMemberId(memberId).stream()
                    .map(likedProject -> likedProject.getProject().getId())
                    .collect(Collectors.toList());
        } else {
            likedProjectId = new ArrayList<>();  // null인 경우 빈 리스트로 초기화
        }



        List<ProjectBoxHostDTO> dtoList = result.getContent().stream()
                .map(project -> {
                    AdminApproval adminApproval = adminApprovalService.findByProjectId(project.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Approval not found for projectId: " + project.getId()));

                    return ProjectBoxHostDTO.builder()
                            .id(project.getId())
                            .title(project.getTitle())
                            .description(project.getDescription())
                            .thumbnailUrl(project.getThumbnailUrl())
                            .fundsReceive(project.getFundsReceive())
                            .targetFunding(project.getTargetFunding())
                            .nickName(project.getMember().getNickname())
                            .endDate(project.getEndDate())
                            .Liked(likedProjectId.contains(project.getId()))  // 좋아요 여부는 기본적으로 false
                            .approval(adminApproval.getApproval())
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<ProjectBoxHostDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)

                .total((int) result.getTotalElements())  // 전체 프로젝트 수를 설정

                .build();

    }

//    @Override
//    public PageResponseDTO<ProjectBoxDTO> getProjectsSortedByFundingRatio(String category, String search, String progress, Long memberId, PageRequestDTO pageRequestDTO) {
////        PageRequest pageable = PageRequest.of(page - 1, size);  // PageRequest를 사용해 페이지와 크기를 지정
//        Pageable pageable =
//                PageRequest.of(pageRequestDTO.getPage() <= 0 ?
//                                0 : pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
//
//        final List<Long> likedProjectId;
//        if (memberId != null) {
//            likedProjectId = likedProjectRepository.findAllByMemberId(memberId).stream()
//                    .map(likedProject -> likedProject.getProject().getId())
//                    .collect(Collectors.toList());
//        } else {
//            likedProjectId = new ArrayList<>();  // null인 경우 빈 리스트로 초기화
//        }
//
//        log.info(category + search +  progress + "1111111111111111111111111111111111111");
//        Page<Project> projects = projectRepository.findAllSortedByFundingRatio(category, search, progress);
//        log.info(projects.getSize() + "dldldlld1111111111111111111111111111111111111");
//
//                List<AdminApproval> approvedAdminApprovals = adminApprovalService.findAllByApproval(1);
//
//        // 3. approval이 1인 프로젝트 ID 목록 생성
//        Set<Long> approvedProjectIds = approvedAdminApprovals.stream()
//                .map(adminApproval -> adminApproval.getProject().getId())
//                .collect(Collectors.toSet());
//
//        // 4. LikedProject에서 approval이 1인 프로젝트들만 필터링
//        List<ProjectBoxDTO> dtoList = projects.getContent().stream()
//                .filter(project -> approvedProjectIds.contains(project.getId()))  // approval이 1인 것만 필터링
//                .map(project -> ProjectBoxDTO.builder()
//                        .id(project.getId())
//                        .title(project.getTitle())
//                        .description(project.getDescription())
//                        .thumbnailUrl(project.getThumbnailUrl())
//                        .fundsReceive(project.getFundsReceive())
//                        .targetFunding(project.getTargetFunding())
//                        .nickName(project.getMember().getNickname())
//                        .endDate(project.getEndDate())
//                        .Liked(likedProjectId.contains(project.getId()))  // 좋아요 여부는 기본적으로 false
//                        .build())
//                .collect(Collectors.toList());
//
//        log.info(projects.getSize() + "dd22222222222222211111111111111111111111111111111111111111111111111112222222222222222222222222222222222");
//        log.info(dtoList.size() + "dd22222222222222211111111111111111111111111111111111111111111111111112222222222222222222222222222222222");
//
//
//        return PageResponseDTO.<ProjectBoxDTO>withAll()
//                .pageRequestDTO(pageRequestDTO)
//                .dtoList(dtoList)
//                .total(dtoList.size())
//                .build();
//
//    }


    @Override
    public ProjectDetailHostDTO readProjectDetailHost(Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));


        final List<Long> likedProjectId;
        if (memberId != null) {
            likedProjectId = likedProjectRepository.findAllByMemberId(memberId).stream()
                    .map(likedProject -> likedProject.getProject().getId())
                    .collect(Collectors.toList());
        } else {
            likedProjectId = new ArrayList<>();  // null인 경우 빈 리스트로 초기화
        }


        List<ProjectImage> projectImages = projectImageRepository.findAllByProjectIdOrderByOrd(projectId);
        List<String> productImages = projectImages.stream()
                .filter(projectImage -> projectImage.getImageType().getImageType().equals("PRODUCT_IMAGE"))
                .map(ProjectImage::getUrl)
                .collect(Collectors.toList());

        List<Tag> tags = project.getTags();
        List<String> tagDTOs = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        AdminApproval adminApproval = adminApprovalService.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found for projectId: " + project.getId()));



        ProjectDetailHostDTO projectDetailHostDTO = ProjectDetailHostDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .fundsReceive(project.getFundsReceive())
                .targetFunding(project.getTargetFunding())
                .category(project.getCategory().getName())
                .nickName(project.getMember().getNickname())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
//                .supporterCnt(supportingProjectService.countByProject(project))
                .supporterCnt(project.getSupporterCnt())
                .approval(adminApproval.getApproval())
                .rejectMessage(adminApproval.getApprovalText())
                .likerCnt(project.getLikeCnt())
                .Liked(likedProjectId.contains(project.getId()))  // 좋아요 여부는 기본적으로 false
                .productImages(productImages)
                .tags(tagDTOs)
                .build();

        return projectDetailHostDTO;
    }

    @Override
    public ProjectResponseDetailDTO readProjectDetail(Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));


        final List<Long> likedProjectId;
        if (memberId != null) {
            likedProjectId = likedProjectRepository.findAllByMemberId(memberId).stream()
                    .map(likedProject -> likedProject.getProject().getId())
                    .collect(Collectors.toList());
        } else {
            likedProjectId = new ArrayList<>();  // null인 경우 빈 리스트로 초기화
        }


        // deletedAt이 null이 아니면 예외를 발생시켜서 처리
        if (project.getDeletedAt() != null) {
            throw new RuntimeException("삭제된 프로젝트입니다.");
        } else {

            List<ProjectImage> projectImages = projectImageRepository.findAllByProjectId(projectId);

            log.info(projectImages);
            List<String> productImages = projectImages.stream()
                    .filter(projectImage -> projectImage.getImageType().getImageType().equals("product"))

                    .map(ProjectImage::getUrl)
                    .collect(Collectors.toList());

            List<String> descriptionImages = projectImages.stream()

                    .filter(projectImage -> projectImage.getImageType().getImageType().equals("description"))

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
//        project.setSupporterCnt(supportingProjectService.countByProject(project));

            ProjectResponseDetailDTO projectResponseDetailDTO = ProjectResponseDetailDTO.builder()
                    .id(project.getId())
                    .title(project.getTitle())
                    .description(project.getDescription())
                    .descriptionDetail(project.getDescriptionDetail())
                    .fundsReceive(project.getFundsReceive())
                    .targetFunding(project.getTargetFunding())
                    .category(project.getCategory().getName())
                    .nickName(project.getMember().getNickname())
                    .startDate(project.getStartDate())
                    .endDate(project.getEndDate())
//                    .supporterCnt(supportingProjectService.countByProject(project))
                    .supporterCnt(project.getSupporterCnt())
                    .likeCnt(project.getLikeCnt())
                    .thumbnailUrl(project.getThumbnailUrl())
                    .productImages(productImages)
                    .Liked(likedProjectId.contains(project.getId()))  // 좋아요 여부는 기본적으로 false
                    .descriptionImages(descriptionImages)
                    .tags(tagDTOs)
                    .build();

            return projectResponseDetailDTO;

        }

    }

    @Override
    public void delProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

//        Category delCategory = categoryService.delProjectFromCategory(projectId, project.getCategory().getName());
        List<Tag> delTags = tagService.delProjectFromTags(project);


        boolean delImg = imgService.deleteImageFiles(projectImageRepository.findAllByProjectId(projectId));
        project.setThumbnailUrl(null);

        docService.deleteDocFiles(projectDocumentRepository.findAllByProjectId(projectId));

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
    }

    @Override
    public Long register(Long memberId,
                         ProjectDetailDTO projectDetailDTO,
                         boolean submit,
                         List<MultipartFile> productImages,
                         List<MultipartFile> descriptionImages,
                         List<MultipartFile> docs) {

//        Category category = categoryService.registerCategory(projectDetailDTO.getCategory());

        List<Tag> tags = tagService.registerTags(projectDetailDTO.getTags());

        Member member = memberService.getById(memberId);

        // 1. 프로젝트 생성 및 저장 (ID 생성)
        Project project = Project.builder()
                .member(member)
                .tags(tags)
                .category(categoryRepository.findByName(projectDetailDTO.getCategory()))
                .title(projectDetailDTO.getTitle())
                .description(projectDetailDTO.getDescription())
                .descriptionDetail(projectDetailDTO.getDescriptionDetail())
                .startDate(projectDetailDTO.getStartDate())
                .endDate(projectDetailDTO.getEndDate())
                .targetFunding(projectDetailDTO.getTargetFunding())
                .fundsReceive(0L)
                .supporterCnt(0L)  // 기본값 0
                .viewCnt(0L)       // 기본값 0
                .likeCnt(0L)       // 기본값 0
                .thumbnailUrl("")  // 기본값은 빈 문자열로 설정
                .submitAt(submit ? Timestamp.valueOf(LocalDateTime.now()) : null)  // 제출 시간 설정
                .build();


        // 프로젝트를 저장해 ID를 생성
        project = projectRepository.save(project);
        final Long projectId = project.getId();

//        // 2. 카테고리 설정
//        category = categoryService.addProjectToCategory(projectId, projectDetailDTO.getCategory());  // 카테고리 등록 서비스 호출
//        project.setCategory(category);  // 카테고리 설정


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

//        Category delCategory = categoryService.delProjectFromCategory(projectId, project.getCategory().getName());
//        Category newCategory = categoryService.registerCategory(projectDetailDTO.getCategory());
//        Category newCategory = categoryService.addProjectToCategory(projectId, projectDetailDTO.getCategory());

        List<Tag> delTags = tagService.delProjectFromTags(project);
//        List<Tag> newTags = tagService.registerTags(projectDetailDTO.getTags());
        List<Tag> newTags = tagService.addProjectToTags(projectDetailDTO.getTags(), projectId);

        Boolean delImg = imgService.deleteImageFiles(projectImageRepository.findAllByProjectId(projectId));
        project.setThumbnailUrl(null);

        docService.deleteDocFiles(projectDocumentRepository.findAllByProjectId(projectId));

        project.setTags(newTags);
//        project.setCategory(newCategory);
        project.setCategory(categoryRepository.findByName(projectDetailDTO.getCategory()));
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

        if ((productImages != null && !productImages.isEmpty()) || (descriptionImages != null && !descriptionImages.isEmpty())) {
            // productImages나 descriptionImages 중 하나라도 null이 아니고 빈 배열이 아닌 경우에만 실행
            imgService.saveImages(project, productImages, descriptionImages);
        }
//        imgService.saveImages(project, productImages, descriptionImages);
        if (docs != null && !docs.isEmpty()) {
            docService.saveDocs(project, docs);
        }

        // 5. 최종 프로젝트 저장
        return project.getId();
    }


    @Override
    public Project findById(Long id) {
        Optional<Project> result = projectRepository.findById(id);

        return result.orElseThrow();
    }


}
