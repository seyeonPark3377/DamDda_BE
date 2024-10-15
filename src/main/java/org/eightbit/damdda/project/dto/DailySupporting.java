package org.eightbit.damdda.project.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DailySupporting {
    private Timestamp supportedAt;
    private Integer totalPrice;
}
