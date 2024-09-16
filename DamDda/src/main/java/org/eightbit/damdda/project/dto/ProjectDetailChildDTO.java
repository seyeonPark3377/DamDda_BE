package org.eightbit.damdda.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("ProjectDetailChildDTO")
public class ProjectDetailChildDTO extends ProjectDetailParentsDTO {
    @JsonProperty("likeCount")
    private int likeCount;
    @JsonProperty("tags")
    private List<TagDTO> tags;

}
