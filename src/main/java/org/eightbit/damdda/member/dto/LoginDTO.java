package org.eightbit.damdda.member.dto;

import lombok.*;
import org.eightbit.damdda.member.domain.Member;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {

    private String loginId;
    private String password;
    private String name;
    private String email;

}