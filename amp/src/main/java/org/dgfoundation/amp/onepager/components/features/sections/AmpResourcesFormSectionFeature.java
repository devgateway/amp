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
import org.dgfoundation.amp.onepager.components.fields.AmpExistingDocumentFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpNewResourceFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
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
        this.fmType = AmpFMTypes.MODULE;
        
        if (am.getObject().getDocuments() == null)
            am.getObject().setDocuments(new HashSet());
        if (getSession().getMetaData(OnePagerConst.RESOURCES_NEW_ITEMS) == null)
            getSession().setMetaData(OnePagerConst.RESOURCES_NEW_ITEMS, new HashSet());
        if (getSession().getMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS) == null)
            getSession().setMetaData(OnePagerConst.RESOURCES_DELETED_ITEMS, new HashSet());
        

        final AmpResourcesFormTableFeature resourcesList = 
                new AmpResourcesFormTableFeature("resourcesList", "Resource List", am);
        resourcesList.setOutputMarkupId(true);
        add(resourcesList);

        final AmpNewResourceFieldPanel<AmpActivityVersion> newDoc = 
                new AmpNewResourceFieldPanel<AmpActivityVersion>("addNewDocument", am, "Add New Document", resourcesList, false);
        newDoc.setOutputMarkupId(true);
        add(newDoc);

        final AmpNewResourceFieldPanel<AmpActivityVersion> newLink = 
                new AmpNewResourceFieldPanel<AmpActivityVersion>("addNewWebLink", am, "Add New Web Link", resourcesList, true);
        newLink.setOutputMarkupId(true);
        add(newLink);

        AmpExistingDocumentFieldPanel searchDocs = 
                new AmpExistingDocumentFieldPanel("addExisting", am, "Search Resources", resourcesList);
        searchDocs.setOutputMarkupId(true);
        add(searchDocs);
    }

}
