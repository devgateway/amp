/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.Set;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.subsections.AmpComponentsFundingSubsectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpComponentIdentificationFormTableFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpStructureIdentificationFormTableFeature;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpStructure;
import org.digijava.module.aim.helper.Constants;

/**
 * @author fferreyra@dginternational.org
 * since May 16, 2011
 */
public class AmpStructureField extends AmpFieldPanel<Boolean>{

	private static final long serialVersionUID = 0L;

	public AmpStructureField(String id,	IModel<AmpActivityVersion> activityModel, 
			IModel<AmpStructure> structureModel, String fmName){
		super(id,fmName, true);
		this.fmType = AmpFMTypes.MODULE;
		
		try {
			WebMarkupContainer structureInformation = new WebMarkupContainer("structureInformation");
			structureInformation.setVisible(false);
			structureInformation.setOutputMarkupId(true);
			structureInformation.setOutputMarkupPlaceholderTag(true);

			AmpStructureIdentificationFormTableFeature firstSection = 
				new AmpStructureIdentificationFormTableFeature("typeTitleCoordinate", activityModel, 
						structureModel, "Structure");
			add(firstSection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
