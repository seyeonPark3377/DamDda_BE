package org.eightbit.damdda.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String loginId;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private String imageUrl;

    @Column(nullable = false)
    private String address;

    private String detailedAddress;

    private int postCode;

    @Builder
    public Member(String loginId, String password, String nickname, String name, String email, String phoneNumber, String imageUrl, String address, String detailedAddress, int postCode) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.postCode = postCode;
    }

}



