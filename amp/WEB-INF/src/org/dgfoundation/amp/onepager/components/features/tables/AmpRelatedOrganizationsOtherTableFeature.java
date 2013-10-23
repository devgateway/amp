/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.features.sections.AmpDonorFundingFormSectionFeature;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.dgfoundation.amp.onepager.events.FundingOrgListUpdateEvent;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpOrgRole;

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
			final IModel<AmpActivityVersion> am, final String roleName,AmpDonorFundingFormSectionFeature donorFundingSection) throws Exception {
		super(id, fmName, am, roleName, donorFundingSection);
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
				AmpPercentageTextField percentageField = new AmpPercentageTextField("percentage", percModel, "percentage",percentageValidationField);
				item.add(percentageField);
				
				AmpDeleteLinkField delRelOrg = new AmpDeleteLinkField("delRelOrg", "Delete Related Organisation") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
                        send(getPage(), Broadcast.BREADTH, new FundingOrgListUpdateEvent(target));

                        Set<AmpFunding> fundings = am.getObject().getFunding();
                        AmpOrgRole ampOrgRole = item.getModelObject();
                        for (AmpFunding ampFunding: fundings) {
                            if (ampFunding.getAmpDonorOrgId().getAmpOrgId() == ampOrgRole.getOrganisation().getAmpOrgId()){
                                String translatedMessage = TranslatorUtil.getTranslation("This organization has a funding related.");
                                target.appendJavaScript("alert ('"+translatedMessage+"')");
                                return;
                            }
                        }
                        setModel.getObject().remove(ampOrgRole);
                        uniqueCollectionValidationField.reloadValidationField(target);
                        target.add(listParent);
                        roleRemoved(target, ampOrgRole);
                        list.getObject().removeAll();
					}
				};
				item.add(delRelOrg);
			}
		});
		add(list.getObject());
	}

}
