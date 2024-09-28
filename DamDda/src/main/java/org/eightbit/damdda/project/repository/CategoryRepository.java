package org.eightbit.damdda.project.repository;

import org.eightbit.damdda.project.domain.Category;
import org.eightbit.damdda.project.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
