package org.eightbit.damdda.noticeandqna.service;

import lombok.RequiredArgsConstructor;
import org.eightbit.damdda.common.exception.custom.UnauthorizedAccessException;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.noticeandqna.domain.QnaQuestion;
import org.eightbit.damdda.noticeandqna.domain.QnaReply;
import org.eightbit.damdda.noticeandqna.dto.QnaReplyDTO;
import org.eightbit.damdda.noticeandqna.repository.QnaReplyRepository;
import org.eightbit.damdda.security.util.SecurityContextUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaReplyServiceImpl implements QnaReplyService {

    private final QnaReplyRepository qnaReplyRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public QnaReplyDTO saveQnaReply(QnaReplyDTO qnaReplyDTO) {
        Long qnaReplyId = qnaReplyDTO.getId();
        Long memberId = SecurityContextUtil.getAuthenticatedMemberId();

        qnaReplyDTO.setMemberId(memberId);

        Member existingMember = Member.builder().id(memberId).build();

        QnaQuestion existingQnaQuestion = QnaQuestion.builder().id(qnaReplyDTO.getQnaQuestionId()).build();

        QnaReply existingQnaReply = null;
        Long parentReplyId = qnaReplyDTO.getParentReplyId();
        if (parentReplyId != null) {
            existingQnaReply = QnaReply.builder().id(parentReplyId).build();
        }

        QnaReply qnaReply = QnaReply.builder()
                .id(qnaReplyDTO.getId())
                .member(existingMember)
                .qnaQuestion(existingQnaQuestion)
                .parentReply(existingQnaReply)
                .content(qnaReplyDTO.getContent())
                .depth(qnaReplyDTO.getDepth())
                .orderPosition(qnaReplyDTO.getOrderPosition())
                .build();

        if(qnaReplyId != null && qnaReplyRepository.existsById(qnaReplyId)) {
            validateQnaReply(memberId, qnaReplyId);
        }

        QnaReply savedQnaReply = qnaReplyRepository.save(qnaReply);

        return modelMapper.map(savedQnaReply, QnaReplyDTO.class);
    }

    @Override
    public boolean softDeleteQnaReply(Long qnaReplyId) {
        Long memberId = SecurityContextUtil.getAuthenticatedMemberId();
        validateQnaReply(memberId, qnaReplyId);

        int deleteResult = qnaReplyRepository.softDeleteQnaReply(qnaReplyId);

        if(deleteResult == 0) {
            throw new NoSuchElementException("QnaReply with ID " + qnaReplyId + " not found");
        }

        return true;

    }

    @Override
    public List<QnaReplyDTO> getQnaReplies(Long qnaQuestionId) {
        List<QnaReply> qnaReplies = qnaReplyRepository.findAllByDeletedAtIsNullAndQnaQuestionId(qnaQuestionId);

        return qnaReplies.stream()
                .map(qnaReply -> modelMapper.map(qnaReply, QnaReplyDTO.class))  // 각 엔티티를 DTO로 변환.
                .collect(Collectors.toList());
    }

    @Override
    public void validateQnaReply(Long memberId, Long qnaReplyId) {
        Long replyerId = qnaReplyRepository.findById(qnaReplyId).orElseThrow().getMember().getId();

        if(replyerId == null) {
            throw new NoSuchElementException("Author not found for the given reply");
        }
        if (!memberId.equals(replyerId)) {
            throw new UnauthorizedAccessException("Member ID unauthorized for qna reply " + qnaReplyId);
        }

    }
}