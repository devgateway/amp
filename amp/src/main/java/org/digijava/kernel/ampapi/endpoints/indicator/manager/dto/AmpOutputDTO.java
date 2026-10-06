package org.digijava.kernel.ampapi.endpoints.indicator.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AmpOutputDTO {
    private Long id;
    private String name;
    private String description;
    private Long outcomeId;
    private AmpOutcomeDTO outcome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @JsonIgnore
    public Long getOutcomeId() { return outcomeId; }
    @JsonProperty
    public void setOutcomeId(Long outcomeId) { this.outcomeId = outcomeId; }
    public AmpOutcomeDTO getOutcome() { return outcome; }
    public void setOutcome(AmpOutcomeDTO outcome) { this.outcome = outcome; }
}
