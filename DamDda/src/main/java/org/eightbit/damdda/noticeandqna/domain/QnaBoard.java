package org.eightbit.damdda.noticeandqna.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Project;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qna_boards")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QnaBoard extends DateEntity {
    @ManyToOne
    private Project project;
    @ManyToOne
    private Member member;

    private String title;
    private String content;
    private Boolean isHidden;

}

