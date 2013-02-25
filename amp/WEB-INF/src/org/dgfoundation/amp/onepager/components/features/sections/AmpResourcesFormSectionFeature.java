/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.features.sections;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.features.tables.AmpResourcesFormTableFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpNewResourceFieldPanel;
import org.dgfoundation.amp.onepager.helper.TemporaryDocument;
import org.dgfoundation.amp.onepager.models.AmpResourcesSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
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

        final AmpNewResourceFieldPanel newDoc = new AmpNewResourceFieldPanel("addNewDocument", am, "Add New Document", resourcesList, false);
        newDoc.setOutputMarkupId(true);
        newDoc.setVisibilityAllowed(false);
        add(newDoc);

        final AmpNewResourceFieldPanel newLink = new AmpNewResourceFieldPanel("addNewWebLink", am, "Add New Web Link", resourcesList, true);
        newLink.setOutputMarkupId(true);
        newLink.setVisibilityAllowed(false);
        add(newLink);

        AmpLabelLinkField alf = new AmpLabelLinkField("newDocLink", "Add New Document Link", "Add New Document"){
            @Override
            protected void onClick(AjaxRequestTarget target) {
                newDoc.setVisibilityAllowed(true);
                target.add(newDoc.getParent());
            }
        };
        add(alf);

        AmpLabelLinkField alf2 = new AmpLabelLinkField("newWebLink", "Add New WebLink Link", "Add New Web Link"){
            @Override
            protected void onClick(AjaxRequestTarget target) {
                newLink.setVisibilityAllowed(true);
                target.add(newLink.getParent());
            }
        };
        add(alf2);


        final AmpAutocompleteFieldPanel<NodeWrapper> searchDocs=new AmpAutocompleteFieldPanel<NodeWrapper>("addExisting","Search Resources",AmpResourcesSearchModel.class) {
			
			private static final long serialVersionUID = 1227775244079125152L;

			@Override
			protected String getChoiceValue(NodeWrapper choice) {
				return choice.getTitle()+" ("+choice.getUuid().hashCode()+")";
			}

			@Override
			public void onSelect(AjaxRequestTarget target, NodeWrapper choice) {
				Set<AmpActivityDocument> existingActDocs = am.getObject().getActivityDocuments();
				if (am.getObject().getActivityDocuments() == null)
					am.getObject().setActivityDocuments(new HashSet<AmpActivityDocument>());
				
				boolean docExists = false;
				
				if(choice.getUuid() != null){
					for (AmpActivityDocument ampActivityDocument : existingActDocs) {
						if(ampActivityDocument.getUuid().equals(choice.getUuid())){
							docExists = true;
							break;
						}
					}
				}
				
				if(!docExists){
					AmpActivityDocument ad = new AmpActivityDocument();
					ad.setAmpActivity(am.getObject());
					ad.setUuid(choice.getUuid());
					ad.setDocumentType(ActivityDocumentsConstants.RELATED_DOCUMENTS);
					am.getObject().getActivityDocuments().add(ad);
				}
				HashSet<AmpActivityDocument> delItems = getSession().getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS);
				if(delItems == null)
					delItems = new HashSet<AmpActivityDocument>();
				for(AmpActivityDocument delItem: delItems)
				{
					if(delItem.getUuid().equals(choice.getUuid()))
					{
						delItems.remove(delItem);
						break;
					}
				}
				
				resourcesList.setRefreshExistingDocs(true);
				target.add(resourcesList.getParent());
				target.appendJavaScript(OnePagerUtil.getToggleChildrenJS(AmpResourcesFormSectionFeature.this));
				
			}

			@Override
			public Integer getChoiceLevel(NodeWrapper choice) {
				return 0;
			}
		};

		add(searchDocs);
	}
}
