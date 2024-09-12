package org.eightbit.damdda.qna.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "qna_reply")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QnaReply extends DateEntity {
    @ManyToOne
    private QnaBoard qnaBoard;
    @ManyToOne
    private Member member;
    @ManyToOne
    private QnaReply qnaReply;

    private String content;
}

