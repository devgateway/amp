package org.digijava.kernel.ampapi.endpoints.util;

/**
 * Enum that describes the different types of filter.
 * Filering by them is done client-side in all-filters-collection.js, func _cleanUpAfterAdd
 * 
 * @author eperez
 *
 */
public enum FilterType {
    ALL,
    DASHBOARD,
    REPORTS,
    GIS,
    TAB,
    GPI_REPORTS
};
