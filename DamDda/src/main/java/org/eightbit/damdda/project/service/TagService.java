package org.eightbit.damdda.project.service;

import org.eightbit.damdda.project.domain.Tag;
import org.eightbit.damdda.project.dto.TagDTO;

import java.util.List;

public interface TagService {
    public List<Tag> registerTags(List<TagDTO> tags);
    public String registerTag(String tagName);
    public List<Tag> addProjectToTags(List<TagDTO> tagDTOs, Long projectId);
    public String addProjectToTag(String tagName, Long projectId);
    public TagDTO getTagDTO(String tagName);
    public Tag getTag(String tagName);
}
