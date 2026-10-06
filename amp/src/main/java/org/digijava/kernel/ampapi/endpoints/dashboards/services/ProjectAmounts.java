package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class ProjectAmounts {

    private Integer totalRecords;

    private List<ProjectAmount> values;

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<ProjectAmount> getValues() {
        return values;
    }

    public void setValues(List<ProjectAmount> values) {
        this.values = values;
    }
}
