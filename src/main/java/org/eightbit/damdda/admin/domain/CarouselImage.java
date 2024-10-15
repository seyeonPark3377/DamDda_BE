package org.eightbit.damdda.admin.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "carousel_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarouselImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adminImageUrl;
}

