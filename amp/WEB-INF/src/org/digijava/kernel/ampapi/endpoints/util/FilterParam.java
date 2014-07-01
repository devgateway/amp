package org.digijava.kernel.ampapi.endpoints.util;

import java.util.List;


/**
 * C
 * @author jdeanquin@developmentgateway.com
 *
 */
public class FilterParam {
    /**
     * Name of the filter
     */
    private String filterName;
    /*
     * value what will be use by the endpoint to filter
     */
    private List<String> filterValue;
    public FilterParam(){
        
    }
    
    public FilterParam(final String filterName, List<String> filterValue) {
        this.filterName = filterName;
        this.filterValue = filterValue;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<String> getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(List<String> filterValue) {
        this.filterValue = filterValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((filterName == null) ? 0 : filterName.hashCode());
        result = prime * result
                + ((filterValue == null) ? 0 : filterValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FilterParam other = (FilterParam) obj;
        if (filterName == null) {
            if (other.filterName != null)
                return false;
        } else if (!filterName.equals(other.filterName))
            return false;
        if (filterValue == null) {
            if (other.filterValue != null)
                return false;
        } else if (!filterValue.equals(other.filterValue))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FilterParam [filterName=" + filterName + ", filterValue="
                + filterValue + "]";
    }

}