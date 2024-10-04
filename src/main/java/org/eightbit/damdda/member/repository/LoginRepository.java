package org.eightbit.damdda.member.repository;

import org.eightbit.damdda.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginIdAndPassword(String loginId, String password);
}
