package org.eightbit.damdda.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(Long memberId){
        return memberRepository.findById(memberId);
    }

    @Override
    public Member getById(Long memberId){
        return memberRepository.getById(memberId);
    }

    @Override
    public MemberDTO getMember(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            return MemberDTO.of(member.get());
        }else {
            return null;
        }
    }

//    @Override
//    public MemberDTO updateMember(String loginId, MemberDTO updateMemberDTO) {
//        Member member = memberRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalArgumentException("not Found"));
//        member.updateInfo(updateMemberDTO.toEntity());
//        return null;
//    }


}
