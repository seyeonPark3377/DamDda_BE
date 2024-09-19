package org.eightbit.damdda.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.project.domain.Category;
import org.eightbit.damdda.project.domain.Project;
import org.eightbit.damdda.project.dto.CategoriesDTO;
import org.eightbit.damdda.project.repository.CategoryRepository;
import org.eightbit.damdda.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Category registerCategory(String name) {
        // 카테고리 이름으로 조회
        Category category = categoryRepository.findByName(name);

        log.info(category + "-----------------------");
        // 카테고리가 존재하지 않으면 새로 생성
        if (category == null) {
            category = Category.builder()
                    .name(name)
                    .build();

            log.info("categories 초기 등록 완료!" + "-----------------------" + category);
            Category category1 = categoryRepository.save(category);
            log.info("category-----" + category1 + "------");
        }

        return category;  // 존재하거나 새로 등록된 카테고리를 반환
    }


    @Override
    public Category addProjectToCategory(Long projectId, String categoryName) {
        registerCategory(categoryName);
        // 카테고리 조회
        Category category = categoryRepository.findByName(categoryName);

        if (category == null) {
            throw new RuntimeException("Category not found: " + categoryName);
        }

        // 프로젝트가 존재하는지 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (category.getProjects() == null || category.getProjects().isEmpty()){
            // 중복 방지: 카테고리에 이미 해당 프로젝트가 없는 경우에만 추가
            category.setProjects(Collections.singletonList(project));  // 업데이트

        } else {
            if (!category.getProjects().contains(project)) {
                List<Project> projects = category.getProjects();
                projects.add(project);
                category.setProjects(projects);  // 업데이트
            }
        }
        return category;
    }

    @Override
    public Category delProjectFromCategory(Long projectId, String categoryName) {
        // 카테고리 조회
        Category category = categoryRepository.findByName(categoryName);

        if (category == null) {
            throw new RuntimeException("Category not found: " + categoryName);
        }

        // 프로젝트가 존재하는지 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));


        if (category.getProjects().contains(project)) {
            List<Project> projects = category.getProjects();
            projects.remove(project);
            category.setProjects(projects);  // 업데이트
        }

        return category;
    }


    @Override
    public CategoriesDTO getCategoryDTO(String categoryName) {
        // 카테고리 조회
        Category category = categoryRepository.findByName(categoryName);

        if (category == null) {
            throw new RuntimeException("Category not found: " + categoryName);
        }

        // 프로젝트 ID만 추출하여 List<Long>으로 변환
        List<Long> projectIds = category.getProjects().stream()
                .map(Project::getId)
                .collect(Collectors.toList());

        // Category를 CategoryDTO로 변환
        return CategoriesDTO.builder()
                .name(category.getName())
                .projectIds(projectIds)
                .build();
    }

}
