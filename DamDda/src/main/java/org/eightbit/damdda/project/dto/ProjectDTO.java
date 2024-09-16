package org.eightbit.damdda.project.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.eightbit.damdda.member.domain.Member;
import org.eightbit.damdda.project.domain.Category;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProjectDetailParentsDTO.class, name = "projectDetailParentsDTO")
})
public class ProjectDTO {

    private String title;
    private String description;
    private Long fundsReceive;
    private Long targetFunding;
    private LocalDateTime endDate;
    private String nickName;
    private boolean Liked;
    private String thumbnailUrl;
}
