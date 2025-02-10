package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

@JsonPropertyOrder({"id", "value", "ampCategoryClass", "index", "deleted", "defaultUsedValue"})
public class AmpCategoryValueDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("value")
    private String value;

    @JsonProperty("ampCategoryClass")
    private AmpCategoryClass ampCategoryClass;

    @JsonProperty("index")
    private Integer index;

    @JsonProperty("deleted")
    private Boolean deleted;

    @JsonProperty("defaultUsedValue")
    private AmpCategoryValue defaultUsedValue;

    public AmpCategoryValueDTO() {
    }

    public AmpCategoryValueDTO(final AmpCategoryValue ampCategoryValue) {
        this.id = ampCategoryValue.getId();
        this.value = ampCategoryValue.getValue();
        this.ampCategoryClass = ampCategoryValue.getAmpCategoryClass();
        this.index = ampCategoryValue.getIndex();
        this.deleted = ampCategoryValue.getDeleted();
        this.defaultUsedValue = ampCategoryValue.getDefaultUsedValue();
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

    public AmpCategoryClass getAmpCategoryClass() {
        return ampCategoryClass;
    }

    public void setAmpCategoryClass(AmpCategoryClass ampCategoryClass) {
        this.ampCategoryClass = ampCategoryClass;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public AmpCategoryValue getDefaultUsedValue() {
        return defaultUsedValue;
    }

    public void setDefaultUsedValue(AmpCategoryValue defaultUsedValue) {
        this.defaultUsedValue = defaultUsedValue;
    }
}
