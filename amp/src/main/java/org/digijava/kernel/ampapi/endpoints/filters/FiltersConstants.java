/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Filters Constants
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersConstants {
    
    // filters IDs 
    public static final String COMPUTED_YEAR = "computedYear";
    public static final String CURRENT = "current";
    
    /** filters IDs to Name mapping */
    public static final Map<String, String> ID_NAME_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(COMPUTED_YEAR, "Computed Year");
        put(CURRENT, "Current");
    }});
    
    // groups
    public static final String OTHER = "Other";
    
    /** filters IDs to main Group (Tab) mapping */
    public static final Map<String, String> ID_GROUP_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put(COMPUTED_YEAR, OTHER);
    }});
    
    
    
}
