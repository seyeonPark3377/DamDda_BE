package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.repository.MemberRepository;
import org.eightbit.damdda.project.domain.LikedProject;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.repository.LikedProjectRepository;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.eightbit.damdda.project.service.LikedProjectService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class LikedProjectServiceImpl implements LikedProjectService {

    private final LikedProjectRepository likedProjectRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertLikedProject(Long projectId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        LikedProject likedProject = LikedProject.builder()
                .member(member)
                .project(project)
                .build();

        return likedProjectRepository.save(likedProject).getId();
    }

    @Override
    public void deleteLikedProject(Long projectId, Long memberId) {
        likedProjectRepository.deleteByProjectIdAndMemberId(projectId, memberId);
    }
}
