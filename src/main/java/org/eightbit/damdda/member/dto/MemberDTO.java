package org.eightbit.damdda.member.dto;


import lombok.*;
import org.eightbit.damdda.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String detailedAddress;
    // int -> Integer로 변경
    private Integer postCode;
    //    private MultipartFile imageUrl;
    private String imageUrl;
    private Timestamp deletedAt;

    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .detailedAddress(member.getDetailedAddress())
                .postCode(member.getPostCode())
                .imageUrl(member.getImageUrl())
                .build();
    }

    // 빌더 수정
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
                .imageUrl(imageUrl)
                .deletedAt(deletedAt)
                .build();
    }
}