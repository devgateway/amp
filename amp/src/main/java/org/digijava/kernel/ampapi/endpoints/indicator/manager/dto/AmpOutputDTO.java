package org.digijava.kernel.ampapi.endpoints.indicator.manager.dto;

import java.util.List;

public class AmpOutputDTO {
    private Long id;
    private String name;
    private String description;
    private List<Long> outcomeIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Long> getOutcomeIds() { return outcomeIds; }
    public void setOutcomeIds(List<Long> outcomeIds) { this.outcomeIds = outcomeIds; }
}

