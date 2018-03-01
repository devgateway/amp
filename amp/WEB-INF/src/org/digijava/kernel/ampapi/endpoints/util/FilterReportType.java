package org.digijava.kernel.ampapi.endpoints.util;

/**
 * Enum that describes the different report types of a filter (where it can be visible).
 * 
 * The filter is visible in
 * DONOR - Donor Reports
 * PLEDGE - Pledge Reports
 * DONOR_PLEDGE - Donor Reports + show pledges
 */
public enum FilterReportType {
    DONOR,
    PLEDGE,
    DONOR_PLEDGE
};
