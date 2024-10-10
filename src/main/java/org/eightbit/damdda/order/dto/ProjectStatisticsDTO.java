package org.eightbit.damdda.order.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Builder
public class ProjectStatisticsDTO {
    private Timestamp startDate;
    private Timestamp endDate;
    private Long totalSupportAmount;   // 총 후원 금액
    private Long totalSupporters;      // 후원자 수
    private long remainingDays;        // 남은 기간
    private Long targetFunding;      // 후원자 수

}
