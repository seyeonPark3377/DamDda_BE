package org.eightbit.damdda.noticeandqna.repository;

import org.eightbit.damdda.noticeandqna.domain.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaReplyRepository extends JpaRepository<QnaReply, Long> {
}