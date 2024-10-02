package org.eightbit.damdda.member.service;

import org.eightbit.damdda.member.domain.Member;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);
    Member getById(Long memberId);

}
