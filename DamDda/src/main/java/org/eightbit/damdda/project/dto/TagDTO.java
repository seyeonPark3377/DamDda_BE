package org.eightbit.damdda.project.dto;

import lombok.*;
import org.eightbit.damdda.project.domain.Project;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagDTO {
    private String name;
    private int usageFrequency;
    private List<Long> projectIds;
}