package org.eightbit.damdda.noticeandqna.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.DateEntity;
import org.eightbit.damdda.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "")
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