package org.eightbit.damdda.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EntityListeners(value={AuditingEntityListener.class})
public class Member {
    // nullable 삭제

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String loginId;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(updatable = false)
    private String name;

    private String email;

    private String phoneNumber;

    private String imageUrl;

    private String address;

    private String detailedAddress;

    // int -> Integer로 변경
    private Integer postCode;

    @CreatedDate
    @Column(updatable = false)
    private Timestamp createdAt;

    private Timestamp deletedAt;

}