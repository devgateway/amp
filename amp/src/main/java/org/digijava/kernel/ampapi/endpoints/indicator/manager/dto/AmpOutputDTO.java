package org.digijava.kernel.ampapi.endpoints.indicator.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AmpOutputDTO {
    private Long id;
    private String name;
    private String description;
    private List<Long> outcomeIds;
    private List<AmpOutcomeDTO> outcomes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @JsonIgnore
    public List<Long> getOutcomeIds() { return outcomeIds; }
    @JsonProperty
    public void setOutcomeIds(List<Long> outcomeIds) { this.outcomeIds = outcomeIds; }
    public List<AmpOutcomeDTO> getOutcomes() { return outcomes; }
    public void setOutcomes(List<AmpOutcomeDTO> outcomes) { this.outcomes = outcomes; }
}
