package org.eightbit.damdda.order.domain;

import lombok.*;
import org.eightbit.damdda.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "deliveries")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Delivery extends BaseEntity {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String detailedAddress;
    private Integer postCode;
    private String message;
}

