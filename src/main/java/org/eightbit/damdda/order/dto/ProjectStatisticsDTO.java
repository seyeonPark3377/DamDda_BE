package org.eightbit.damdda.order.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProjectStatisticsDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long totalSupportAmount;   // 총 후원 금액
    private Long totalSupporters;      // 후원자 수
    private long remainingDays;        // 남은 기간
    private Long targetFunding;      // 후원자 수

}
