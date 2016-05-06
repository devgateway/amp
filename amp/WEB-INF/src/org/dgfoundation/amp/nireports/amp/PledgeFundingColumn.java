package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.HashSet;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;

/**
 * class for fetching any of the MTEF columns/measures
 * @author Dolghier Constantin
 *
 */
public class PledgeFundingColumn extends AmpFundingColumn {

	public PledgeFundingColumn() {
		super(ENTITY_PLEDGE_FUNDING, "v_ni_pledges_funding",
			TrivialMeasureBehaviour.getInstance(), 
			new HashSet<>(Arrays.asList("disaster_response_code")));
	}
}
