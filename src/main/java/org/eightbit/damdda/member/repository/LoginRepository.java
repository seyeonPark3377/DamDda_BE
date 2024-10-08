package org.eightbit.damdda.member.repository;

import org.eightbit.damdda.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface LoginRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
}
