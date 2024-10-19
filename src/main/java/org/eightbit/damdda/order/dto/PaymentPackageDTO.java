package org.eightbit.damdda.order.dto;

import lombok.*;
import org.eightbit.damdda.order.dto.PaymentRewardDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class PaymentPackageDTO {

    private Long id;
    private String name;
    private Integer price;
    private Integer count;
    private List<PaymentRewardDTO> rewardList;

}