package org.eightbit.damdda.project.repository;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.project.domain.Category;
import org.eightbit.damdda.project.domain.RewardOption;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

public interface RewardOptionRepository extends JpaRepository<RewardOption, Long> {

}

