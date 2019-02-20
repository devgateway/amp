package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;

/**
 * @author Viorel Chihai
 */
public class CategoryValueLabel {
    
    @JsonProperty(EPConstants.ID)
    @ApiParam(example = "123")
    private Long id;
    
    @JsonProperty(EPConstants.VALUE)
    @ApiParam(example = "Category Value Name")
    private String value;
    
    @JsonProperty(EPConstants.LABEL)
    @ApiParam(example = "Category Value Name translated")
    private String label;
    
    public CategoryValueLabel(Long id, String value, String label) {
        this.id = id;
        this.value = value;
        this.label = label;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
}
