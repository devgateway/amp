package org.digijava.kernel.ampapi.endpoints.indicator.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AmpOutcomeDTO {
    private Long id;
    private String name;
    private String description;
    private List<AmpOutputDTO> outputs;
    private List<Long> outputIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<AmpOutputDTO> getOutputs() { return outputs; }
    public void setOutputs(List<AmpOutputDTO> outputs) { this.outputs = outputs; }
    @JsonIgnore
    public List<Long> getOutputIds() {
        return outputIds;
    }

    @JsonProperty
    public void setOutputIds(List<Long> outputIds) {
        this.outputIds = outputIds;
    }

}
