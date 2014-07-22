package org.digijava.kernel.ampapi.endpoints.util;

import java.util.List;
/**
 * Class that holds parameters sent by clients to the endpoints
 * @author jdeanquin@developmentgateway.org
 *
 */
public class FiltersParams {
    
    private List<FilterParam> params;

    public FiltersParams() {
    }

    public FiltersParams(final List<FilterParam> params) {
        this.params = params;
    }

    public List<FilterParam> getParams() {
        return params;
    }

    public void setParams(List<FilterParam> params) {
        this.params = params;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((params == null) ? 0 : params.hashCode());
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
        FiltersParams other = (FiltersParams) obj;
        if (params == null) {
            if (other.params != null)
                return false;
        } else if (!params.equals(other.params))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FiltersParams [params=" + params + "]";
    }



}
