package org.eightbit.damdda.project.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDTO {
    private MultipartFile file;
    private String url;
    private int ord;
}
