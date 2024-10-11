package org.eightbit.damdda.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.repository.MemberRepository;
import org.eightbit.damdda.member.repository.RegisterRepository;
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
    private final RegisterRepository registerRepository;
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
        if (member.isPresent()) {
            return MemberDTO.of(member.get());
        }else {
            return null;
        }
    }

    @Override
    public MemberDTO confirmPw(String loginId, String password) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String encodedPassword = member.getPassword();
            System.out.println("passwordEncoder : " + passwordEncoder);
            if(passwordEncoder.matches(password, encodedPassword)) {
                MemberDTO memberDTO = MemberDTO.of(member);
                memberDTO.setPassword(null);
                return memberDTO;
            }
        }
        return null;
    }
    @Transactional
    @Override
    public MemberDTO updateMember(MemberDTO memberDTO) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(memberDTO.getLoginId());
        Member member = optionalMember.get();

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        memberDTO.setPassword(encoder.encode(memberDTO.getPassword()));
        memberDTO.setNickname(memberDTO.getNickname());
        memberDTO.setEmail(memberDTO.getEmail());
        memberDTO.setPhoneNumber(memberDTO.getPhoneNumber());
        memberDTO.setAddress(memberDTO.getAddress());
        memberDTO.setDetailedAddress(memberDTO.getDetailedAddress());
        memberDTO.setPostCode(memberDTO.getPostCode());

        System.out.println(member);
        System.out.println(memberDTO.toEntity());

        this.memberRepository.save(memberDTO.toEntity());

//        String userId = SecurityContextHolder.getPrincipal().getLoginId();

        return memberDTO;
    }


}
