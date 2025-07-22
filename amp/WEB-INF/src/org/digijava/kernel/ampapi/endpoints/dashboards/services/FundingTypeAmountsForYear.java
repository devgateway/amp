package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class FundingTypeAmountsForYear {

    @JsonProperty("Year")
    private String year;

    private List<FundingTypeAmount> values;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<FundingTypeAmount> getValues() {
        return values;
    }

    public void setValues(List<FundingTypeAmount> values) {
        this.values = values;
    }
}
