package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a dimension consisting of (TOP_SECTOR[level=0], SUB_SECTOR[level=1], SUB_SUB_SECTOR[level=2]) 
 * @author Dolghier Constantin
 *
 */
public final class SectorsDimension extends SqlSourcedNiDimension {
	
	public final static SectorsDimension instance = new SectorsDimension("sectors");
	
	private SectorsDimension(String name) {
		super(name, "ni_all_sectors_with_levels", Arrays.asList("id0", "id1", "id2"));
	}
	
	public final static int LEVEL_ROOT = 0;
	public final static int LEVEL_SUBSECTOR = 1;
	public final static int LEVEL_SUBSUBSECTOR = 2;

	@Override
	protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg) {
		String schemeName = dimUsg.instanceName;
		return new PercentagesCorrector("amp_activity_sector", "amp_activity_id", "sector_percentage", () -> String.format("classification_config_id = (SELECT id FROM amp_classification_config WHERE name='%s')", schemeName));
	}
}