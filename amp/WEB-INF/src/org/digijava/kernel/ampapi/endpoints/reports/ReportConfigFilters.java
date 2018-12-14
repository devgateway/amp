package org.digijava.kernel.ampapi.endpoints.reports;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

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

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
