package org.eightbit.damdda.project.repository;
//이거 필요 없을 듯

import org.eightbit.damdda.project.domain.ProjectTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTagRepository  extends JpaRepository<ProjectTag, Long> {

}
