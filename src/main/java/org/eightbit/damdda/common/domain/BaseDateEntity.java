package org.eightbit.damdda.common.domain;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

// TODO: 삭제 필요.
@Getter
@MappedSuperclass
@EntityListeners(value={AuditingEntityListener.class})
public abstract class BaseDateEntity {

    @CreatedDate
    @Column(updatable = false)
    private Timestamp createdAt;

    private Timestamp deletedAt;
}