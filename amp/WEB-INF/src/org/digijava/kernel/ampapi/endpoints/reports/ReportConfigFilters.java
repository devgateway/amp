package org.digijava.kernel.ampapi.endpoints.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Funny object for the sole purpose of having a reference to filters.
 *
 * TODO refactor an remove, will have to touch gis code on frontend
 *
 * @author Octavian Ciubotaru
 */
public class ReportConfigFilters {

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.FiltersPH")
    private Map<String, Object> filters;

    @JsonProperty("include-location-children")
    private Boolean includeLocationChildren;

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Boolean getIncludeLocationChildren() {
        return includeLocationChildren;
    }

    public void setIncludeLocationChildren(Boolean includeLocationChildren) {
        this.includeLocationChildren = includeLocationChildren;
    }
}
