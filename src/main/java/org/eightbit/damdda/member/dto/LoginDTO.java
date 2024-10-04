package org.eightbit.damdda.member.dto;

import lombok.*;
import org.eightbit.damdda.member.domain.Member;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String loginId;
    private String password;
    private String name;
    private String email;

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}
