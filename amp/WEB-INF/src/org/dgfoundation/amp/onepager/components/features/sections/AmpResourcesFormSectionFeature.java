/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.tables.AmpResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpNewResourceFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Resources section
 * @author aartimon@dginternational.org
 * @since Apr 11, 2011 
 */
public class AmpResourcesFormSectionFeature extends AmpFormSectionFeaturePanel {
	
	public static Logger logger = Logger.getLogger(AmpResourcesFormSectionFeature.class);
	
	public AmpResourcesFormSectionFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName, am);
		
		if (am.getObject().getDocuments() == null)
			am.getObject().setDocuments(new HashSet());
		if (getSession().getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS) == null)
			getSession().setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, new HashSet());
		if (getSession().getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS) == null)
			getSession().setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, new HashSet());
		

		final AmpResourcesFormTableFeature resourcesList = new AmpResourcesFormTableFeature("resourcesList", "Resource List", am);
		resourcesList.setOutputMarkupId(true);
		add(resourcesList);
		
		AmpNewResourceFieldPanel newDoc = new AmpNewResourceFieldPanel("addNewDocument", am, "Add New Document", resourcesList, false);
		add(newDoc);
		
		AmpNewResourceFieldPanel newLink = new AmpNewResourceFieldPanel("addNewWebLink", am, "Add New Web Link", resourcesList, true);
		add(newLink);
		
	}
}
