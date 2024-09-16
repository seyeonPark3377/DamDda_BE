package org.eightbit.damdda.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.eightbit.damdda.project.domain.Project;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@JsonTypeName("projectDetailParentsDTO")
public class ProjectDetailParentsDTO extends Project {
    @JsonProperty("category")
    private String category;
    @JsonProperty("supporterCount")
    private int supporterCount;
    @JsonProperty("startDate")
    private LocalDateTime startDate;
    @JsonProperty("images")
    private List<ProjectImageDTO> images;

}

