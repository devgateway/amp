/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.web.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.dgfoundation.amp.onepager.components.features.sections.AmpComponentsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContactsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpContractingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpCrossCuttingIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpInternalIdsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIssuesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpLocationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpMEFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPIFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpPlanningFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpProgramFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRegionalObservationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpRelatedOrganizationsFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpResourcesFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.sections.AmpSectorsFormSectionFeature;

/**
 * @author mpostelnicu@dgateway.org since Sep 22, 2010
 */
public class OnePager extends OnePagerWrapper {
	private static Logger logger = Logger.getLogger(OnePager.class);

	public OnePager(PageParameters parameters) {
		super(parameters);

		try {
			AmpIdentificationFormSectionFeature identificationFeature = new AmpIdentificationFormSectionFeature(
					"identification", "Identification", am);
			activityForm.add(identificationFeature);

			AmpInternalIdsFormSectionFeature internalIdsFeature = new AmpInternalIdsFormSectionFeature(
					"internalIds", "Activity Internal IDs", am);
			activityForm.add(internalIdsFeature);

			AmpPlanningFormSectionFeature planningFeature = new AmpPlanningFormSectionFeature(
					"planning", "Planning", am);
			activityForm.add(planningFeature);

			AmpRegionalFundingFormSectionFeature regionalFundingFeature = new AmpRegionalFundingFormSectionFeature(
					"regionalFunding", "Regional Funding", am);
			activityForm.add(regionalFundingFeature);

			
			AmpLocationFormSectionFeature locationFeature = new AmpLocationFormSectionFeature(
					"location", "Location", am,regionalFundingFeature);
			activityForm.add(locationFeature);

			AmpProgramFormSectionFeature programFeature = new AmpProgramFormSectionFeature(
					"program", "Program", am);
			activityForm.add(programFeature);

			AmpCrossCuttingIssuesFormSectionFeature crossCuttingIssues = new AmpCrossCuttingIssuesFormSectionFeature(
					"crossCuttingIssues", "Cross Cutting Issues", am);
			activityForm.add(crossCuttingIssues);

			AmpSectorsFormSectionFeature sectorsFeature = new AmpSectorsFormSectionFeature(
					"sectors", "Sectors", am);
			activityForm.add(sectorsFeature);

			AmpDonorFundingFormSectionFeature donorFundingFeature = new AmpDonorFundingFormSectionFeature(
					"donorFunding", "Donor Funding", am);
			activityForm.add(donorFundingFeature);

			AmpRelatedOrganizationsFormSectionFeature relatedOrganizations = new AmpRelatedOrganizationsFormSectionFeature(
					"relatedOrganizations", "Related Organizations", am);
			activityForm.add(relatedOrganizations);
			AmpComponentsFormSectionFeature components = new AmpComponentsFormSectionFeature("components", "Components", am);
			activityForm.add(components);
			
			AmpIssuesFormSectionFeature issues = new AmpIssuesFormSectionFeature("issues", "Issues Section", am);
			activityForm.add(issues);
			
			AmpRegionalObservationsFormSectionFeature regionalObs = new AmpRegionalObservationsFormSectionFeature("regionalObs", "Regional Observations", am);
			activityForm.add(regionalObs);

			AmpContactsFormSectionFeature contacts = new AmpContactsFormSectionFeature("contacts", "Contacts", am);
			activityForm.add(contacts);
			
			AmpContractingFormSectionFeature contracts = new AmpContractingFormSectionFeature("contracts", "Contracts", am);
			activityForm.add(contracts);
			
			AmpMEFormSectionFeature me = new AmpMEFormSectionFeature("me", "M&E", am);
			activityForm.add(me);

			AmpPIFormSectionFeature pi = new AmpPIFormSectionFeature("pi", "Paris Indicators", am);
			activityForm.add(pi);
			
			AmpResourcesFormSectionFeature resources = new AmpResourcesFormSectionFeature("resources", "Related Documents", am);
			activityForm.add(resources);

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
}
