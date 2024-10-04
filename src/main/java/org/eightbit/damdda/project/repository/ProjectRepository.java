package org.eightbit.damdda.project.repository;

import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom  {
//    @Query("SELECT p FROM Project p WHERE p.deletedAt IS NULL")
//    List<Project> findAllActiveProjects();

//    @Query("select p from Project p where p.member.id = :memberId" )
//    Page<Project> listOfProjectBoxHost(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select p from Project p where p.member.id = :memberId and p.deletedAt is null and p.submitAt is NOT null")
    Page<Project> listOfProjectBoxHost(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select p from Project p where p.member.id = :memberId and p.deletedAt is null")
    Project findByMemberId(@Param("memberId") Long memberId);

    Page<Project> findAllByDeletedAtIsNull(Pageable pageable);

    List<Project> findAllByMemberIdAndSubmitAtIsNullAndDeletedAtIsNull(Long memberId);
//    public Project findByMemberId(Long memberId);

//    @Query("SELECT p FROM Project p " +
//            "WHERE p.deletedAt IS NULL " +
//            "ORDER BY (p.fundsReceive / p.targetFunding) DESC")
//    Page<Project> findAllSortedByFundingRatio(Pageable pageable);

    @Query("SELECT p FROM Project p "
            + "WHERE p.deletedAt IS NULL "  // 삭제된 항목 필터
            + "AND (:category IS NULL OR :category = '전체' OR p.category.name = :category) "  // 카테고리 필터
            + "AND (:search IS NULL OR p.title LIKE %:search% "
            + "OR p.description LIKE %:search% "
            + "OR p.descriptionDetail LIKE %:search% "
            + "OR EXISTS (SELECT t FROM Tag t WHERE t MEMBER OF p.tags AND t.name LIKE %:search%)) "  // 검색어 필터 (태그 포함)
            + "AND (:progress IS NULL OR "
            + "     (:progress = 'all') OR "
            + "     (:progress = 'ongoing' AND CURRENT_TIMESTAMP BETWEEN p.startDate AND p.endDate) OR "  // 진행 중 필터
            + "     (:progress = 'upcoming' AND p.startDate > CURRENT_TIMESTAMP) OR "  // 예정 필터
            + "     (:progress = 'completed' AND p.endDate < CURRENT_TIMESTAMP)) " // 완료된 필터
            + "ORDER BY (p.fundsReceive / p.targetFunding) DESC"
    )
    List<Project> findAllSortedByFundingRatio(@Param("category") String category,
                                              @Param("search") String search,
                                              @Param("progress") String progress);


//    List<Project> findSortedAndFilteredProjects(List<Long> approvedProjectIds, Pageable pageable);


}
