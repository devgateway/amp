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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.util.FMUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;

import java.util.List;

/**
 * @author aartimon@dginternational.org
 * @since Nov 9, 2012
 */
public class AmpRelatedOrganizationsOtherTableFeature extends AmpRelatedOrganizationsBaseTableFeature {
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     * @param fmName
     * @param am
     * @throws Exception
     */
    public AmpRelatedOrganizationsOtherTableFeature(String id, String fmName,
            final IModel<AmpActivityVersion> am, final String roleName,
            AmpDonorFundingFormSectionFeature donorFundingSection, 
            final List<AmpOrgGroup> availableOrgGroupChoices) throws Exception {
        super(id, fmName, am, roleName, donorFundingSection, availableOrgGroupChoices);

        this.configureTemplate(roleName, this::setTemplateFilter);
        
        list.setObject(new ListView<AmpOrgRole>("list", listModel) {
            private static final long serialVersionUID = 7218457979728871528L;
            @Override
            protected void populateItem(final ListItem<AmpOrgRole> item) {
                final MarkupContainer listParent=this.getParent();
                
                item.add(new AmpTextFieldPanel<String>(
                        "departmentDivision",
                        new PropertyModel<String>(item.getModel(), "additionalInfo"), "relOrgadditionalInfo", true, true));
                
                item.add(new Label("name", item.getModelObject().getOrganisation().getAcronymAndName()));   
                
                PropertyModel<Double> percModel = new PropertyModel<Double>(item.getModel(), "percentage"); 
                AmpPercentageTextField percentageField = new AmpPercentageTextField("percentage", percModel, "percentage", percentageValidationField) {
                    @Override
                    public void onConfigure() {
                        super.onConfigure();
                        if(orgAddedOrRemoved && !FMUtil.isFmEnabled(this)) {
                            //reset all percentages to null if percentages are hidden and organization list is altered
                            item.getModelObject().setPercentage(null);
                        }
                    }
                };
                item.add(percentageField);
                
                AmpDeleteLinkField delRelOrg = new AmpDeleteLinkField("delRelOrg", "Delete Related Organisation") {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        onDeleteOrg(target, item, am);
                    }
                };
                item.add(delRelOrg);
            }
        });
        add(list.getObject());
    }
}
