package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a dimension consisting of (TOP_SECTOR[level=0], SUB_SECTOR[level=1], SUB_SUB_SECTOR[level=2]) 
 * @author Dolghier Constantin
 *
 */
public final class SectorsDimension extends SqlSourcedNiDimension<String> {
	
	public final static SectorsDimension instance = new SectorsDimension("Sectors dimension");
	
	private SectorsDimension(String name) {
		super(name, "amp_location_cache", Arrays.asList("country_id", "region_id", "zone_id", "district_id"));
	}
	
	public final static int LEVEL_COUNTRY = 0;
	public final static int LEVEL_REGION = 1;
	public final static int LEVEL_ZONE = 2;
	public final static int LEVEL_DISTRICT = 3;
}