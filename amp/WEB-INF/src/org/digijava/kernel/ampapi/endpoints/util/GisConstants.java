/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * GIS related constants
 * 
 * @author Nadejda Mandrescu
 */
public class GisConstants {
    
    public static final String USE_ICONS_FOR_SECTORS_IN_PROJECT_LIST =  "Use icons for Sectors in Project List";
    public static final String PROJECT_SITES = "Project sites";
    public static final String DOWNLOAD_MAP_SELECTOR = "Download Map selector";
    
    public static final String ADM0 = "adm-0";
    public static final String ADM1 = "adm-1";
    public static final String ADM2 = "adm-2";
    public static final String ADM3 = "adm-3";
    public static final String ADM4 = "adm-4";

    public static final String GIS_STRUCTURE_POLYGON = "Polygon";
    public static final String GIS_STRUCTURE_POINT = "Point";
    public static final String GIS_STRUCTURE_POLYLINE = "Polyline";
    
    public static final String GIS_STRUCTURE_COLOR_DELIMITER = ":";
    public static final String HEX_PATTERN_REGEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    public static final Pattern HEX_PATTERN = Pattern.compile(GisConstants.HEX_PATTERN_REGEX);
    
    /**
     * Set of measures that can be used in GIS module as funding type options.
     * The priority doesn't matter anymore, but still adding some order via LinkedHashSet just for display.
     */
    public static final Set<String> FUNDING_TYPES = new LinkedHashSet<String>() {{
        add(MeasureConstants.ACTUAL_COMMITMENTS);
        add(MeasureConstants.ACTUAL_DISBURSEMENTS);
        add(MeasureConstants.ACTUAL_EXPENDITURES);
        add(MeasureConstants.PLANNED_COMMITMENTS);
        add(MeasureConstants.PLANNED_DISBURSEMENTS);
        add(MeasureConstants.PLANNED_EXPENDITURES);
        add(MeasureConstants.BILATERAL_SSC_COMMITMENTS);
        add(MeasureConstants.BILATERAL_SSC_DISBURSEMENTS);
        add(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS);
        add(MeasureConstants.TRIANGULAR_SSC_DISBURSEMENTS);
    }};
    
    public static final Map<String, HardCodedCategoryValue> ADM_TO_IMPL_CATEGORY_VALUE = new HashMap<String, HardCodedCategoryValue>() {{
       put(ADM0, CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY); 
       put(ADM1, CategoryConstants.IMPLEMENTATION_LOCATION_REGION);
       put(ADM2, CategoryConstants.IMPLEMENTATION_LOCATION_ZONE);
       put(ADM3, CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT);
    }};
    
    public static final Map<String, String> IMPL_CATEGORY_VALUE_TO_ADM = new HashMap<String, String>() {{
        put(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey(), ADM0);
        put(CategoryConstants.IMPLEMENTATION_LOCATION_REGION.getValueKey(), ADM1);
        put(CategoryConstants.IMPLEMENTATION_LOCATION_ZONE.getValueKey(), ADM2);
        put(CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT.getValueKey(), ADM3);
    }};
    
}
