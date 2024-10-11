package org.eightbit.damdda.member.dto;

import lombok.*;
import org.eightbit.damdda.member.domain.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PasswordModifyDTO {

    private String loginId;
    private String password;
    private String name;
    private String email;

    public Member toEntity(){
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }

}
