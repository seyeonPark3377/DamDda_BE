package org.eightbit.damdda.admin.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "admins")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
}
