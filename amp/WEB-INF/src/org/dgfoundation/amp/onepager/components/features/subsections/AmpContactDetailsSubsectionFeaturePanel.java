/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.subsections;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.items.AmpAddContactFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactDetailFeaturePanel;
import org.dgfoundation.amp.onepager.components.features.items.AmpContactOrganizationFeaturePanel;
import org.dgfoundation.amp.onepager.components.fields.AmpCategorySelectFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextAreaFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpContactProperty;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * @author dmihaila@dginternational.org
 * since Dec 6, 2010
 */
public class AmpContactDetailsSubsectionFeaturePanel extends AmpSubsectionFeaturePanel<AmpActivityContact>{

	

	protected ListView<AmpContactProperty> emailList, phoneList, faxList;
	
	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpContactDetailsSubsectionFeaturePanel(String id, String fmName, IModel<AmpActivityVersion> am, IModel<AmpActivityContact> ampActContact) throws Exception {
		//super(id, contactModel, fmName, true);
		super(id,fmName, ampActContact);
		add(new AmpAddContactFeaturePanel("contactDetail", fmName, am, ampActContact));


	}

}

