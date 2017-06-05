package org.digijava.kernel.ampapi.endpoints.common.valueproviders;

import com.google.common.collect.ImmutableMap;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author Octavian Ciubotaru
 */
public class OrganisationValueProvider extends GenericInterchangeableValueProvider<AmpOrganisation> {

    public OrganisationValueProvider() {
        super(AmpOrganisation.class, "name");
    }

    @Override
    public Object getExtraInfo(AmpOrganisation organisation) {
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();
        if (organisation.getAcronym() != null) {
            builder.put("acronym", organisation.getAcronym());
        }
        if (organisation.getOrgGrpId().getName() != null) {
            builder.put("organization_group", organisation.getOrgGrpId().getName());
        }
        return builder.build();
    }
}
