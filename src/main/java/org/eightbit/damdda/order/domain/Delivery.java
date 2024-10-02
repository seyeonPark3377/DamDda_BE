package org.eightbit.damdda.order.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "deliveries")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long deliveryId;

    private String deliveryName;
    private String deliveryPhoneNumber;
    private String deliveryEmail;
    private String deliveryAddress;
    private String deliveryDetailedAddress;
    private String deliveryPostcode;
    private String deliveryMessage;
}

