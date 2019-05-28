package org.dgfoundation.amp.activity.builder;

import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author Octavian Ciubotaru
 */
public class ComponentFundingBuilder {

    private ComponentBuilder componentBuilder;
    private AmpComponentFunding funding;

    ComponentFundingBuilder(ComponentBuilder componentBuilder, AmpComponentFunding funding) {
        this.componentBuilder = componentBuilder;
        this.funding = funding;
    }

    public ComponentFundingBuilder withOrg(AmpOrganisation org) {
        funding.setReportingOrganization(org);
        return this;
    }

    public ComponentBuilder addFunding() {
        componentBuilder.component.getFundings().add(funding);
        return componentBuilder;
    }
}
