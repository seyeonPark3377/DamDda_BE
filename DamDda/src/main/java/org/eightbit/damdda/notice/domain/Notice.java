package org.eightbit.damdda.notice.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notice extends DateEntity {

    @ManyToOne
    private Project project;

    private String content;

    @LastModifiedDate
    private Timestamp modifiedAt;
}

