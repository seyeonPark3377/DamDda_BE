package org.eightbit.damdda.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.admin.domain.AdminApproval;
import org.eightbit.damdda.admin.repository.AdminApprovalRepository;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminApprovalServiceImpl implements AdminApprovalService {


    private final AdminApprovalRepository adminApprovalRepository;
//    private final ProjectService projectService;

    // 추가된 부분
    @Override
    public void submitProject(Project project) {
        AdminApproval adminApproval = AdminApproval.builder()
                .project(project)
                .approval(0)
                .approvalText("프로젝트 승인 대기 중...")
                .build();
        adminApprovalRepository.save(adminApproval);
    }

    @Override
    public Optional<AdminApproval> findByProjectId(Long projectId){
        return adminApprovalRepository.findByProjectId(projectId);
    }

    @Override
    public List<AdminApproval> findAllByApproval(Integer approval){
        return adminApprovalRepository.findAllByApproval(approval);
    }

}
