package org.eightbit.damdda.project.repository;

import org.eightbit.damdda.project.domain.Collaboration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {

    @Query("SELECT c FROM Collaboration c WHERE c.receiverDeletedAt IS NULL")
    Page<Collaboration> findAllWhereReceiverDeletedAtIsNull(Pageable pageable);

    @Query("update Collaboration c set c.approval =:approval where c.id in :idList")
    void changeApproval(@Param("approval") String approval, @Param("idList") List<Long> idList);

}
