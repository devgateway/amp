package org.digijava.kernel.ampapi.endpoints.util;

/**
 * Enum that describes the different report types of a filter (where it can be visible).
 * 
 * The filter is visible in
 * DONOR - Donor Reports
 * PLEDGE - Pledge Reports
 * ALL - Reports with all filters
 */
public enum FilterReportType {
    DONOR("D"),
    PLEDGE("P"),
    ALL("ALL");
    
    private final String type;
    
    FilterReportType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
};
