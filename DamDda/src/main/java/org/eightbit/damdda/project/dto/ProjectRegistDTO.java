package org.eightbit.damdda.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;


public class ProjectRegistDTO extends ProjectDetailChildDTO{
    private List<String> registDocList;
    private Timestamp submitDate;
}
