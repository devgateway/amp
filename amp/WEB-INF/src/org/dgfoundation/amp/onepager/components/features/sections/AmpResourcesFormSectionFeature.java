/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.components.features.tables.AmpResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpNewResourceFieldPanel;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.AmpMEIndicatorSearchModel;
import org.dgfoundation.amp.onepager.models.AmpResourcesSearchModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;

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
		this.fmType = AmpFMTypes.MODULE;
		
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
		
		AmpAutocompleteFieldPanel<NodeWrapper> searchIndicators=new AmpAutocompleteFieldPanel<NodeWrapper>("addExisting","Search Resources",AmpResourcesSearchModel.class) {			
			
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(NodeWrapper choice) {
				return choice.getTitle();
			}

			@Override
			public void onSelect(AjaxRequestTarget target, NodeWrapper choice) {
				AmpActivityDocument ad = new AmpActivityDocument();
				ad.setAmpActivity(am.getObject());
				ad.setUuid(choice.getUuid());
				ad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
				am.getObject().getDocuments().add(ad);
				target.addComponent(resourcesList);
				target.appendJavascript(OnePagerConst.getToggleChildrenJS(AmpResourcesFormSectionFeature.this));
			}

			@Override
			public Integer getChoiceLevel(NodeWrapper choice) {
				return 0;
			}
		};

		add(searchIndicators);
	}
}
