package org.eightbit.damdda.member.service;

import org.eightbit.damdda.member.dto.LoginDTO;
import org.eightbit.damdda.member.dto.MemberDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;

public interface LoginService{
//    MemberDTO login(LoginDTO loginDTO, HttpSession session);
//    UserDetails loadUserByLoginIdAndPassword(String loginId, String password);
//    void logout(HttpSession session);
    void searchId();
//    UserDetails loadUserByUsername(String loginId);
}
