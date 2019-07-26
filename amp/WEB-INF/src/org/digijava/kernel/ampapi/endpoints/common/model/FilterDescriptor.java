package org.digijava.kernel.ampapi.endpoints.common.model;

import java.util.List;

import org.digijava.kernel.ampapi.endpoints.dto.FilterValue;

/**
 * @author Octavian Ciubotaru
 */
public class FilterDescriptor {

    private String filterId;

    private String name;

    private List<FilterValue> values;

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilterValue> getValues() {
        return values;
    }

    public void setValues(List<FilterValue> values) {
        this.values = values;
    }
}
