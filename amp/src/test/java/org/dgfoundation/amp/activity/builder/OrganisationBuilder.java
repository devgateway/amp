package org.dgfoundation.amp.activity.builder;

import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class OrganisationBuilder {

    private AmpOrganisation organisation;

    public OrganisationBuilder() {
        organisation = new AmpOrganisation();
    }

    public OrganisationBuilder withOrganisationName(String name) {
        organisation.setName(name);

        return this;
    }

    public AmpOrganisation getOrganisation() {
        return organisation;
    }
}
