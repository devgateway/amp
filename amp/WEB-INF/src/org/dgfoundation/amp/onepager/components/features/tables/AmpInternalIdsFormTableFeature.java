/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.AmpSearchOrganizationComponent;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.util.DbUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 20, 2010
 */
public class AmpInternalIdsFormTableFeature extends AmpFormTableFeaturePanel {

    protected ListView<AmpActivityInternalId> idsList;
    
    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpInternalIdsFormTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am) throws Exception {
        super(id, am, fmName);
        final IModel<Set<AmpActivityInternalId>> setModel=new PropertyModel<Set<AmpActivityInternalId>>(am,"internalIds");
        if (setModel.getObject() == null)
            setModel.setObject(new HashSet<AmpActivityInternalId>());

        AbstractReadOnlyModel<List<AmpActivityInternalId>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(setModel);


        
        idsList = new ListView<AmpActivityInternalId>("listOrgs", listModel) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpActivityInternalId> item) {
                final MarkupContainer listParent=this.getParent();
                AmpTextFieldPanel<String> internalId=new AmpTextFieldPanel<String>("internalId", new PropertyModel<String>(item.getModel(), 
                        "internalId"),"internalId",true);
                internalId.setTextContainerDefaultMaxSize();
                item.add(internalId);
                
                item.add(new Label("orgNameLabel", item.getModelObject()
                        .getOrganisation().getAcronymAndName()));           
                
                AmpDeleteLinkField delOrgId = new AmpDeleteLinkField(
                        "delOrgId", "Delete Internal Id") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setModel.getObject().remove(item.getModelObject());
                        target.add(listParent);
                        idsList.removeAll();
                    }
                };
                item.add(delOrgId);
                
            }
        };
        idsList.setReuseItems(true);
        add(idsList);
        
        final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete", "Search Organizations", true,true, AmpOrganisationSearchModel.class,id) {          
            @Override
            protected void onSelect(AjaxRequestTarget target, AmpOrganisation choice) {
                AmpActivityInternalId activityInternalId = new AmpActivityInternalId();
                activityInternalId.setOrganisation(choice);
                activityInternalId.setAmpActivity(am.getObject());
                
                if (setModel.getObject() == null)
                    setModel.setObject(new HashSet<AmpActivityInternalId>());
                
                Set<AmpActivityInternalId> set = setModel.getObject();
                set.add(activityInternalId);
                idsList.removeAll();
                target.add(idsList.getParent());
            }
            
            @Override
            protected String getChoiceValue(AmpOrganisation choice) {
                return DbUtil.filter(choice.getName());
            }
            
            @Override
            protected boolean showAcronyms() {
                return true;
            }
            
            @Override
            protected String getAcronym(AmpOrganisation choice) {
                return choice.getAcronym();
            }
            
            @Override
            public Integer getChoiceLevel(AmpOrganisation choice) {
                // TODO Auto-generated method stub
                return null;
            }
        };

        AmpSearchOrganizationComponent searchOrganization = new AmpSearchOrganizationComponent("searchOrgs", new Model<String> (),
                "Search Organizations",  searchOrgs, null);
        add(searchOrganization);

    }

}
