package org.eightbit.damdda.admin.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Admin extends BaseEntity {

    private String loginId;
    private String password;
}
