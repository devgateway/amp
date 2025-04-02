package org.dgfoundation.amp.nireports.amp.dimensions;

import java.util.Arrays;

import org.dgfoundation.amp.nireports.amp.PercentagesCorrector;
import org.dgfoundation.amp.nireports.amp.SqlSourcedNiDimension;

/**
 * 
 * a <i>ni_all_orgs_dimension</i>-backed dimension consisting of (orgType[level = 0], orgGroup[level = 1], org[level = 2])
 * @author Dolghier Constantin
 *
 */
public final class OrganisationsDimension extends SqlSourcedNiDimension {
    
    public final static OrganisationsDimension instance = new OrganisationsDimension("orgs");
    
    public final static int LEVEL_ORGANISATION = 2;
    public final static int LEVEL_ORGANISATION_GROUP = 1;
    public final static int LEVEL_ORGANISATION_TYPE = 0;
    
    private OrganisationsDimension(String name) {
        super(name, "ni_all_orgs_dimension", Arrays.asList("org_type_id", "org_grp_id", "org_id"));
    }

    @Override
    protected PercentagesCorrector buildPercentagesCorrector(NiDimensionUsage dimUsg, boolean pledgeColumn) {
        if (pledgeColumn)
            return null;
        String roleCode = dimUsg.instanceName;
        return new PercentagesCorrector("amp_org_role", "activity", "percentage", () -> String.format("role = (SELECT amp_role_id FROM amp_role WHERE role_code='%s')", roleCode));
    }
}
