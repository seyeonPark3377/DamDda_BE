package org.eightbit.damdda.member.service;

import org.eightbit.damdda.member.dto.LoginDTO;
import org.eightbit.damdda.member.dto.MemberDTO;

import javax.servlet.http.HttpSession;

public interface LoginService {
    MemberDTO login(LoginDTO loginDTO, HttpSession session);
    void logout(HttpSession session);
    void searchId();
}
