/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.tables.AmpLocationFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.web.pages.OnePager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

/**
 * Location section of the one pager form
 * 
 * @author mpostelnicu@dgateway.org since Oct 7, 2010
 * @see OnePager
 */
public class AmpLocationFormSectionFeature extends AmpFormSectionFeaturePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8671173324087460636L;

	/**
	 * @param id
	 * @param fmName
	 * @param regionalFundingFeature 
	 * @throws Exception
	 */
	public AmpLocationFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am, AmpRegionalFundingFormSectionFeature regionalFundingFeature) throws Exception {
		super(id, fmName, am);
		this.fmType = AmpFMTypes.MODULE;
		
		AmpCategorySelectFieldPanel implementationLevel = new AmpCategorySelectFieldPanel(
				"implementationLevel",
				CategoryConstants.IMPLEMENTATION_LEVEL_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.IMPLEMENTATION_LEVEL_KEY),
				CategoryConstants.IMPLEMENTATION_LEVEL_NAME, true, true, null, AmpFMTypes.FEATURE);
		add(implementationLevel);

		final AmpCategorySelectFieldPanel implementationLocation = new AmpCategorySelectFieldPanel(
				"implementationLocation",
				CategoryConstants.IMPLEMENTATION_LOCATION_KEY,
				new AmpCategoryValueByKeyModel(
						new PropertyModel<Set<AmpCategoryValue>>(am,
								"categories"),
						CategoryConstants.IMPLEMENTATION_LOCATION_KEY),
				CategoryConstants.IMPLEMENTATION_LOCATION_NAME, true, true,
				null, implementationLevel.getChoiceModel(), AmpFMTypes.FEATURE);
		implementationLocation.setOutputMarkupId(true);
		add(implementationLocation);

		// add behavior to update implementation location when implementation
		// level choice changes
		implementationLevel.getChoiceContainer().add(
				new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = -8419230552388122030L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						target.addComponent(implementationLocation);
					}
				});

		// add location table
		AmpLocationFormTableFeature locationsTable = new AmpLocationFormTableFeature(
				"locationsTable", "Locations", am, regionalFundingFeature, implementationLocation);
		add(locationsTable);
	}

}
