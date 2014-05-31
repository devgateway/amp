/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
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
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author aartimon@dginternational.org
 * @since Nov 12, 2012
 */
public class AmpRelatedOrganizationsResponsibleTableFeature extends AmpRelatedOrganizationsBaseTableFeature {
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpRelatedOrganizationsResponsibleTableFeature(String id, String fmName,
			final IModel<AmpActivityVersion> am, final String roleName,AmpDonorFundingFormSectionFeature donorFundingSection) throws Exception {
		super(id, fmName, am, roleName, donorFundingSection, null);
		Long value = Long.valueOf(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_ORG_GROUP));
		if (value.longValue()!=-1) {
		setDefaultOrgGroup(DbUtil.getAmpOrgGroup(value));
		}
		
		
		setTitleHeaderColSpan(5);
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
				
				AmpOrgRole oRole = item.getModelObject();
				String orgCode = oRole.getOrganisation().getBudgetOrgCode();

                PropertyModel<String> bcModel = new PropertyModel<String>(item.getModel(), "budgetCode");
                if (bcModel.getObject() == null || "".equals(bcModel.getObject().trim()))
                    bcModel.setObject(orgCode); //if budget code not entered yet, add the first part from the org budget code

                AmpTextFieldPanel<String> budgetCode = new AmpTextFieldPanel<String>("budgetCodeEdit", bcModel, "Budget Code", true, true);
				budgetCode.getTextContainer().add(new AttributeModifier("style", "width: 80px;"));
				budgetCode.getTextContainer().add(new AttributeModifier("maxlength", "6"));

				/*if (disableBudgetCode){
					budgetCode.setIgnorePermissions(true);
					budgetCode.setEnabled(false);
				}*/
				item.add(budgetCode);
				
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
