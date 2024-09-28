package org.eightbit.damdda.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
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

}
