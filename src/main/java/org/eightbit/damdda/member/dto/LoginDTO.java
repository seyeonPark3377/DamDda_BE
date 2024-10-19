package org.eightbit.damdda.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {

    private String loginId;
    private String password;
    private String name;
    private String email;

}