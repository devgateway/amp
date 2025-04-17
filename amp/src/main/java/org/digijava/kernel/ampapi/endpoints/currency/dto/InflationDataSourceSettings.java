package org.digijava.kernel.ampapi.endpoints.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.dgfoundation.amp.currency.IRFrequency;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyEPConstants;

public class InflationDataSourceSettings {
    
    @JsonProperty(CurrencyEPConstants.CURRENCY_CODE)
    @ApiModelProperty(value = "currency code", example = "USD")
    private String currencyCode;
    
    @JsonProperty(CurrencyEPConstants.FREQUENCY)
    @ApiModelProperty(value = "inflation rates frequency", example = "Q")
    private IRFrequency frequency;
    
    @JsonProperty(CurrencyEPConstants.API_TOKEN)
    @ApiModelProperty(example = "3df0333014282b8a2a7e3de345eae3e2")
    private String apiToken;
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public IRFrequency getFrequency() {
        return frequency;
    }
    
    public void setFrequency(IRFrequency frequency) {
        this.frequency = frequency;
    }
    
    public String getApiToken() {
        return apiToken;
    }
    
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
