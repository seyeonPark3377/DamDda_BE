package org.eightbit.damdda.order.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;

    private String paymentStatus;
    private String paymentMethod;
    private String paymentPrice;
}

