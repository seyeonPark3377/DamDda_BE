package org.eightbit.damdda.member.service;

import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface MemberService {
    String uploadFile(MultipartFile file) throws IOException;
    void deleteFIle(String fileName);
    Optional<Member> findById(Long memberId);
    Member getById(Long memberId);
    MemberDTO getMember(String loginId);
//    MemberDTO updateMember(String loginID, MemberDTO updateMemberDTO);
}
