/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.components.features.subsections;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import org.dgfoundation.amp.onepager.components.fields.AmpUniqueCollectionValidatorField;
import org.dgfoundation.amp.onepager.models.AmpOrganisationSearchModel;
import org.dgfoundation.amp.onepager.yui.AmpAutocompleteFieldPanel;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.util.DbUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author aartimon@dginternational.org
 * @since Feb 8, 2011
 */
public class AmpContractOrganizationsSubsectionFeature extends
        AmpSubsectionFeaturePanel<IPAContract> {
    
    private static Logger logger = Logger.getLogger(AmpContractOrganizationsSubsectionFeature.class);

    /**
     * @param id
     * @param fmName
     * @param model
     * @throws Exception
     */
    public AmpContractOrganizationsSubsectionFeature(String id,
            IModel<IPAContract> model, String fmName){
        super(id, fmName, model, false, true);

        final IModel<Set<AmpOrganisation>> orgs = new PropertyModel<Set<AmpOrganisation>>(model, "organizations");
        
        if (orgs.getObject() == null)
            orgs.setObject(new HashSet<AmpOrganisation>());
        
        AbstractReadOnlyModel<List<AmpOrganisation>> listModel = OnePagerUtil
                .getReadOnlyListModelFromSetModel(orgs);

        final ListView<AmpOrganisation> list = new ListView<AmpOrganisation>("list", listModel) {
                @Override
                protected void populateItem(final ListItem<AmpOrganisation> item) {
                    AmpOrganisation org = item.getModelObject();
                    
                    Label orgName = new Label("name", org.getAcronymAndName());
                    item.add(orgName);
                    
                    AmpDeleteLinkField delete = new AmpDeleteLinkField(
                            "delete", "Delete Organisation") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            orgs.getObject().remove(item.getModelObject());
                            target.add(AmpContractOrganizationsSubsectionFeature.this);
                            target.appendJavaScript(OnePagerUtil.getToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
                            target.appendJavaScript(OnePagerUtil.getClickToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
                        }
                    };
                    item.add(delete);
                }
        };
        list.setReuseItems(true);
        add(list);

        
        WebMarkupContainer wmc = new WebMarkupContainer("ajaxIndicator");
        add(wmc);
        AjaxIndicatorAppender iValidator = new AjaxIndicatorAppender();
        wmc.add(iValidator);
        
        final AmpUniqueCollectionValidatorField<AmpOrganisation> uniqueCollectionValidationField = new AmpUniqueCollectionValidatorField<AmpOrganisation>(
                "uniqueOrgsValidator", listModel, "Unique Orgs Validator") {
            @Override
            public Object getIdentifier(AmpOrganisation t) {
                return t.getAcronymAndName();
            }
        };
        uniqueCollectionValidationField.setIndicatorAppender(iValidator);
        add(uniqueCollectionValidationField);
        
        
        
        
        final AmpAutocompleteFieldPanel<AmpOrganisation> searchOrgs=new AmpAutocompleteFieldPanel<AmpOrganisation>("searchAutocomplete","Search Funding Organizations",true,true,AmpOrganisationSearchModel.class,id) {         
            private static final long serialVersionUID = 1227775244079125152L;

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
            public void onSelect(AjaxRequestTarget target,
                    AmpOrganisation choice) {
                orgs.getObject().add(choice);
                list.removeAll();
                target.add(AmpContractOrganizationsSubsectionFeature.this);
                target.appendJavaScript(OnePagerUtil.getToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
                target.appendJavaScript(OnePagerUtil.getClickToggleJS(AmpContractOrganizationsSubsectionFeature.this.getSlider()));
                uniqueCollectionValidationField.reloadValidationField(target);
            }

            @Override
            public Integer getChoiceLevel(AmpOrganisation choice) {
                return null;
            }
        };
        AmpSearchOrganizationComponent searchOrganization = new AmpSearchOrganizationComponent("search", new Model<String> (),
                "Search Funding Organizations", searchOrgs, null);
        add(searchOrganization);
    }

}
