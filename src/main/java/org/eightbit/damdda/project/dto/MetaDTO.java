package org.eightbit.damdda.project.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MetaDTO {
    private String url;
    private int ord;
}
