package org.eightbit.damdda.admin.dto;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CarouselImageDTO {
    private Long imageId;
    private String imageUrl;
}
