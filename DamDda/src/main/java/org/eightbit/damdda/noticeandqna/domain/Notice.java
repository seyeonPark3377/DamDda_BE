package org.eightbit.damdda.noticeandqna.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.project.domain.Project;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

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

