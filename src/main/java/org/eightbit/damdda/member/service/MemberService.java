package org.eightbit.damdda.member.service;

import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);
    Member getById(Long memberId);
    MemberDTO getMember(String loginId);
    MemberDTO updateMember(MemberDTO memberDTO);
    MemberDTO confirmPw(String loginId, String password);
}
