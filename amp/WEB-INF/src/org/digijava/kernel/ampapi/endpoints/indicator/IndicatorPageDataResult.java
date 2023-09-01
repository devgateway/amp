package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndicatorPageDataResult {
    
    @JsonProperty(IndicatorEPConstants.PAGE)
    private PageInformation page;
    
    @JsonProperty(IndicatorEPConstants.DATA)
    private List<Indicator> data;
    
    public IndicatorPageDataResult(PageInformation page, List<Indicator> data) {
        this.page = page;
        this.data = data;
    }
    
    public PageInformation getPage() {
        return page;
    }
    
    public void setPage(PageInformation page) {
        this.page = page;
    }
    
    public List<Indicator> getData() {
        return data;
    }
    
    public void setData(List<Indicator> data) {
        this.data = data;
    }
    
    
}
