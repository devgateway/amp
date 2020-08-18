package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension;

/**
 * 
 * an <i>ni_all_locations_with_levels</i>-backed <strong>continuum</strong> dimension consisting of (country[level=0],
 * region[level=1], zone[level=2], district[level=3], communal_section[level=4])
 * @author Dolghier Constantin
 *
 */
public final class LocationsDimension extends SqlSourcedNiDimension {
    
    public final static LocationsDimension instance = new LocationsDimension("locs");
    
    private LocationsDimension(String name) {
        super(name, "ni_all_locations_with_levels", Arrays.asList("adm_level_0_id", "adm_level_1_id",
                "adm_level_2_id", "adm_level_3_id", "adm_level_4_id"));
    }
    
    public static final int ADM_LEVEL_0 = 0;
    public static final int ADM_LEVEL_1 = 1;
    public static final int ADM_LEVEL_2 = 2;
    public static final int ADM_LEVEL_3 = 3;
    public static final int ADM_LEVEL_4 = 4;
    public static final int LEVEL_RAW = NiDimension.LEVEL_ALL_IDS;
    

    @Override
    protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg, boolean pledgeColumn) {
        if (pledgeColumn)
            return new PercentagesCorrector("v_pledges_location_percentages", "pledge_id", "location_percentage", null);
        return new PercentagesCorrector("amp_activity_location", "amp_activity_id", "location_percentage", null);
    }
}
