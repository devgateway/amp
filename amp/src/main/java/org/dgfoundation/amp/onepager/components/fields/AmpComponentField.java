/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.AmpFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpComponentsFundingSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentIdentificationFormTableFeature;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.helper.Constants;

/**
 * @author aartimon@dginternational.org
 * since Oct 27, 2010
 */
public class AmpComponentField extends AmpFeaturePanel<Boolean>{

	private static final long serialVersionUID = 0L;

	public AmpComponentField(String id,	IModel<AmpActivityVersion> activityModel, 
			final IModel<AmpComponent> componentModel, String fmName){
		super(id,fmName, true);
		this.fmType = AmpFMTypes.MODULE;
		
		final PropertyModel<Set<AmpComponentFunding>> componentsSetModel=new 
				PropertyModel<Set<AmpComponentFunding>>(activityModel, "componentFundings");
		if (componentsSetModel.getObject() == null)
			componentsSetModel.setObject(new HashSet());
		
		AmpComponent c = componentModel.getObject();
		if (c.getTitle() == null || c.getTitle().trim().compareTo("") == 0){
			c.setTitle(TranslatorUtil.getTranslation("New Component"));
		}
		
		final Label componentNameLabel = new Label("componentName", new PropertyModel<String>(componentModel, "title"));
		componentNameLabel.setOutputMarkupId(true);
		add(componentNameLabel);

		try {
			AmpComponentIdentificationFormTableFeature ident = new AmpComponentIdentificationFormTableFeature("info", activityModel, componentModel, componentsSetModel, "Component Information", componentNameLabel);
			add(ident);
			
			AmpComponentsFundingSubsectionFeature commitments = new AmpComponentsFundingSubsectionFeature("commitments", activityModel, 
					componentModel, componentsSetModel, "Components Commitments", 
					Constants.COMMITMENT);
			commitments.setOutputMarkupId(true);
			commitments.setOutputMarkupPlaceholderTag(true);
			add(commitments);
			
			AmpComponentsFundingSubsectionFeature disbursements = new AmpComponentsFundingSubsectionFeature("disbursements", activityModel, 
					componentModel, componentsSetModel, "Components Disbursements", 
					Constants.DISBURSEMENT);
			disbursements.setOutputMarkupId(true);
			disbursements.setOutputMarkupPlaceholderTag(true);
			add(disbursements);

			AmpComponentsFundingSubsectionFeature expeditures = new AmpComponentsFundingSubsectionFeature("expeditures", activityModel, 
					componentModel, componentsSetModel, "Components Expenditures", 
					Constants.EXPENDITURE);
			expeditures.setOutputMarkupId(true);
			expeditures.setOutputMarkupPlaceholderTag(true);
			add(expeditures);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
