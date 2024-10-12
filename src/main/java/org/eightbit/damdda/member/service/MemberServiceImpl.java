package org.eightbit.damdda.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        return member.map(MemberDTO::of).orElse(null);
    }

    @Override
    public MemberDTO confirmPw(String loginId, String password) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String encodedPassword = member.getPassword();
            if(passwordEncoder.matches(password, encodedPassword)) {
                MemberDTO memberDTO = MemberDTO.of(member);
                memberDTO.setPassword(null);
                return memberDTO;
            }
        }
        return null;
    }

    @Override
    public Boolean deleteMember(Long id) {
//        memberRepository.
        return null;
    }

    @Transactional
    @Override
    public MemberDTO updateMember(MemberDTO memberDTO) {
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberDTO.setNickname(memberDTO.getNickname());
        memberDTO.setEmail(memberDTO.getEmail());
        memberDTO.setPhoneNumber(memberDTO.getPhoneNumber());
        memberDTO.setAddress(memberDTO.getAddress());
        memberDTO.setDetailedAddress(memberDTO.getDetailedAddress());
        memberDTO.setPostCode(memberDTO.getPostCode());

        this.memberRepository.save(memberDTO.toEntity());
        return memberDTO;
    }

}
