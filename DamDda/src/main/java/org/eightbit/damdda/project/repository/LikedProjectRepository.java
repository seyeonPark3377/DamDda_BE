package org.eightbit.damdda.project.repository;

import org.eightbit.damdda.project.domain.LikedProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedProjectRepository extends JpaRepository<LikedProject, Long> {

}
