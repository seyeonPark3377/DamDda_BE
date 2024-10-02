package org.eightbit.damdda.member.dto;


import lombok.*;

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
    private String imageUrl;
    private Timestamp createdAt;
    private Timestamp deletedAt;
}
