package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a dimension consisting of (country[level=0], region[level=1], zone[level=2], district[level=3]) 
 * @author Dolghier Constantin
 *
 */
public final class LocationsDimension extends SqlSourcedNiDimension {
	
	public final static LocationsDimension instance = new LocationsDimension("locs");
	
	private LocationsDimension(String name) {
		super(name, "ni_all_locations_with_levels", Arrays.asList("country_id", "region_id", "zone_id", "district_id"));
	}
	
	public final static int LEVEL_COUNTRY = 0;
	public final static int LEVEL_REGION = 1;
	public final static int LEVEL_ZONE = 2;
	public final static int LEVEL_DISTRICT = 3;

	@Override
	protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg) {
		return new PercentagesCorrector("amp_activity_location", "amp_activity_id", "location_percentage", null);
	}
}
