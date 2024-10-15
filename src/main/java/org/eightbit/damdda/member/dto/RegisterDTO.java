package org.eightbit.damdda.member.dto;

import lombok.*;
import org.eightbit.damdda.member.domain.Member;

@Data
@NoArgsConstructor
public class RegisterDTO {
    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    private int postCode;

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .detailedAddress(detailedAddress)
                .postCode(postCode)
                .build();
    }

    @Builder
    public RegisterDTO(String loginId, String password, String nickname, String name, String email, String phoneNumber, String address, String detailedAddress, int postCode) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.postCode = postCode;
    }
}