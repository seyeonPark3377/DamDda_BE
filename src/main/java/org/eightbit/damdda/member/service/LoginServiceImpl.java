package org.eightbit.damdda.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eightbit.damdda.member.domain.Member;
//import org.eightbit.damdda.member.dto.LoginDTO;
//import org.eightbit.damdda.member.dto.MemberDTO;
import org.eightbit.damdda.member.domain.User;
import org.eightbit.damdda.member.repository.LoginRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;

//    @Override
//    public MemberDTO login(LoginDTO loginDTO, HttpSession session) {
//        Optional<Member> member = loginRepository.findByLoginIdAndPassword(loginDTO.getLoginId(), loginDTO.getPassword());
//        if(member.isPresent()) {
//            session.setAttribute("loginMember", member.get());
//            return MemberDTO.of(member.get());
//        }else {
//            throw new IllegalArgumentException("error");
//        }
//    }



    @Override
    public String findId(String name, String email) {
        Optional<Member> findMember = loginRepository.findByNameAndEmail(name, email);
        Member member = findMember.orElseThrow();
        return member.getLoginId();
    }
}
