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
        ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<>();
        if (organisation.getAcronym() != null) {
            builder.put("acronym", organisation.getAcronym());
        }
        if (organisation.getOrgGrpId() != null && organisation.getOrgGrpId().getName() != null) {
            builder.put("organization_group", organisation.getOrgGrpId().getName());
        }
        if (organisation.getBudgetOrgCode() != null) {
            builder.put("budget_organization_code", organisation.getBudgetOrgCode());
        }
        if (organisation.getCountry() != null && organisation.getCountry().getTemplate() != null) {
            builder.put("template", organisation.getCountry().getTemplate().getId());
        }
        return builder.build();
    }
}
