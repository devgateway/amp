package org.digijava.kernel.ampapi.endpoints.dashboards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;

/**
 * @author Octavian Ciubotaru
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashboardFormParameters extends SettingsAndFiltersParameters {

    private Integer offset;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
