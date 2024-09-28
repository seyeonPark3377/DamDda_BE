package org.eightbit.damdda.admin.repository;


import org.eightbit.damdda.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<Member, Long> {
}
