package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "payments")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment extends BaseEntity {
    private String paymentStatus;
    private String paymentMethod;
}

