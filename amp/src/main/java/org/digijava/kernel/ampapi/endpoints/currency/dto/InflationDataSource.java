package org.digijava.kernel.ampapi.endpoints.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;

public class InflationDataSource {
    
    @JsonProperty(CurrencyEPConstants.ID)
    @ApiModelProperty(example = "123")
    private Long id;
    
    @JsonProperty(CurrencyEPConstants.NAME)
    @ApiModelProperty(value = "the name of the source", example = "FRED-GNPDEF")
    private String name;
    
    @JsonProperty(CurrencyEPConstants.DESCRIPTION)
    @ApiModelProperty(value = "the description of the source", example = "Gross National Product: "
            + "Implicit Price Deflator (USD) provide by US. Bureau of Economic Analysis")
    private String description;
    
    @JsonProperty(CurrencyEPConstants.SELECTED)
    @ApiModelProperty("if the current data source was chosen to provide inflation rates")
    private Boolean selected;
    
    @JsonProperty(EPConstants.SETTINGS)
    @ApiModelProperty(value = "settings info with the currency and frequency")
    private InflationDataSourceSettings settings;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getSelected() {
        return selected;
    }
    
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    public InflationDataSourceSettings getSettings() {
        return settings;
    }
    
    public void setSettings(InflationDataSourceSettings settings) {
        this.settings = settings;
    }
}
