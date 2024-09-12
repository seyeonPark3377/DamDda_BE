package org.eightbit.damdda.member.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.common.domain.DateEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member extends DateEntity {

    private String loginId;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phoneNumber;
    private String imageUrl;
}

