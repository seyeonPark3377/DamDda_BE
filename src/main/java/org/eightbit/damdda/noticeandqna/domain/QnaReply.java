//package org.eightbit.damdda.noticeandqna.domain;
//
//import lombok.*;
//import org.eightbit.damdda.common.domain.DateEntity;
//import org.eightbit.damdda.member.domain.Member;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "qna_replies")
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//public class QnaReply extends DateEntity {
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(nullable = false)
//    private QnaQuestion qnaQuestion;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(nullable = false)
//    private Member member;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    private QnaReply parentReply;
//
//    @Column(length = 300, nullable = false)
//    private String content;
//
//    @Column(nullable = false)
//    private int depth;
//
//    @Column(nullable = false)
//    private int orderPosition;
//}
