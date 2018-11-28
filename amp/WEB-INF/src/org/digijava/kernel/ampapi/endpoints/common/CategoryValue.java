package org.digijava.kernel.ampapi.endpoints.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiParam;

/**
 * @author Viorel Chihai
 */
public class CategoryValue {
    
    public static class NameOriginalView {
    }
    
    public static class LabelValueView {
    }
    
    @JsonProperty(EPConstants.ID)
    @ApiParam(value = "id", example = "123")
    private Long id;
    
    @JsonProperty(EPConstants.ORIGINAL_NAME)
    @ApiParam(value = "name (original)", example = "Category Value Name")
    @JsonView(CategoryValue.NameOriginalView.class)
    private String originalName;
    
    @JsonProperty(EPConstants.NAME)
    @ApiParam(value = "name (translated)", example = "Category Value Name translated")
    @JsonView(CategoryValue.NameOriginalView.class)
    private String name;
    
    @JsonProperty(EPConstants.VALUE)
    @ApiParam(value = "value", example = "Category Value Name")
    @JsonView(CategoryValue.LabelValueView.class)
    private String value;
    
    @JsonProperty(EPConstants.LABEL)
    @ApiParam(value = "label", example = "Category Value Name translated")
    @JsonView(CategoryValue.LabelValueView.class)
    private String label;
    
    public CategoryValue(Long id, String originalName, String name) {
        this.id = id;
        this.originalName = originalName;
        this.value = originalName;
        this.name = name;
        this.label = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalName() {
        return originalName;
    }
    
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
