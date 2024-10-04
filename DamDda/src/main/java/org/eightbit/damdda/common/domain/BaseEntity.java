
package org.eightbit.damdda.common.domain;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter //추가
@MappedSuperclass
@EntityListeners(value={AuditingEntityListener.class})
public abstract class BaseEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
