package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension;

/**
 * 
 * an <i>ni_all_locations_with_levels</i>-backed <strong>continuum</strong> dimension consisting of (country[level=0], region[level=1], zone[level=2], district[level=3]) 
 * @author Dolghier Constantin
 *
 */
public final class LocationsDimension extends SqlSourcedNiDimension {
    
    public final static LocationsDimension instance = new LocationsDimension("locs");
    
    private LocationsDimension(String name) {
        super(name, "ni_all_locations_with_levels",
                Arrays.asList("country_id", "region_id", "zone_id", "district_id", "communal_section_id"));
    }
    
    public static final int LEVEL_COUNTRY = 0;
    public static final int LEVEL_REGION = 1;
    public static final int LEVEL_ZONE = 2;
    public static final int LEVEL_DISTRICT = 3;
    public static final int LEVEL_COMMUNAL_SECTION = 4;
    public static final int LEVEL_RAW = NiDimension.LEVEL_ALL_IDS;
    

    @Override
    protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg, boolean pledgeColumn) {
        if (pledgeColumn)
            return new PercentagesCorrector("v_pledges_location_percentages", "pledge_id", "location_percentage", null);
        return new PercentagesCorrector("amp_activity_location", "amp_activity_id", "location_percentage", null);
    }
}
