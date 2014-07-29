package org.digijava.kernel.ampapi.endpoints.util;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;

public class FilterUtil {
    private static Logger logger = Logger.getLogger(FilterUtil.class);
    /**
     * 
     * @param filter
     * @param filterName
     * @param minExpectedValueSize
     * @param maxExpectedValueSize
     * @param expectedValues
     */
    public static void validateOneFilters(FiltersParams filter,
            String filterName, Integer minExpectedValueSize,
            Integer maxExpectedValueSize, List<String> expectedValues) {
        if (filter.getParams().size() != 1) {
            // we should only receive one filter
            getException("Only one filter expected");
        } else {
            FilterParam f = filter.getParams().get(0);
            if (!f.getFilterName().equals(filterName)) {
                // we should receive adminLevel filter
            } else {
                if (f.getFilterValue() == null
                        || f.getFilterValue().size() > minExpectedValueSize
                        || f.getFilterValue().size() < maxExpectedValueSize) {
                    // we shouldn't receive more than one value
                } else {
                    // we validate the content of values to be equals to the
                    // list we receive as parameter
                    for (String filterValue : f.getFilterValue()) {
                        if (!QueryUtil.getAdminLevels().contains(filterValue)) {
                            // we trhow the exception
                        }
                    }
                }
            }
        }
    }

    public static void getException(String message) {
        logger.debug(message);
        throw new WebApplicationException();
    }

}
