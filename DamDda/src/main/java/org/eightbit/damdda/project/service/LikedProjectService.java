package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.LikedProject;

public interface LikedProjectService {
    Long insertLikedProject(Long projectId, Long memberId);
    void deleteLikedProject(Long projectId, Long memberId);
}
