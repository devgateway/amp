package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

public final class OrganisationsDimension extends SqlSourcedNiDimension<String> {
	
	public final static OrganisationsDimension instance = new OrganisationsDimension("Organisations dimension");
	
	public final static int LEVEL_ORGANISATION = 2;
	public final static int LEVEL_ORGANISATION_GROUP = 1;
	public final static int LEVEL_ORGANISATION_TYPE = 0;
	
	private OrganisationsDimension(String name) {
		super(name, "v_ni_all_orgs_dimension", Arrays.asList("org_type_id", "org_grp_id", "org_id"));
	}
}
