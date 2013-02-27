/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpEditLinkField;
import org.dgfoundation.amp.onepager.components.fields.AmpPercentageTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpTextFieldPanel;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrgRole;

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
			final IModel<AmpActivityVersion> am, final String roleName) throws Exception {
		super(id, fmName, am, roleName);
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
				
				boolean disableBudgetCode = true;
				AmpOrgRole orole = item.getModelObject();
				String orgCode = orole.getOrganisation().getBudgetOrgCode();
				item.add(new Label("budgetCodeLabel", orgCode));
				
				AmpTextFieldPanel<Long> budgetCode = new AmpTextFieldPanel<Long>("budgetCodeEdit", new PropertyModel<Long>(item.getModel(), "budgetCode"), "Budget Code", true, true);
				budgetCode.getTextContainer().add(new AttributeModifier("style", "width: 40px;"));
				budgetCode.getTextContainer().add(new AttributeModifier("maxlength", "3"));
				budgetCode.getTextContainer().add(new RangeValidator<Long>(null, 1000L));
				if (disableBudgetCode){
					budgetCode.setIgnorePermissions(true);
					budgetCode.setEnabled(false);
				}
				item.add(budgetCode);
				
				AmpDeleteLinkField delRelOrg = new AmpDeleteLinkField("delRelOrg", "Delete Related Organisation") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						uniqueCollectionValidationField.reloadValidationField(target);
						list.getObject().removeAll();
						target.add(listParent);
					}
				};
				item.add(delRelOrg);
			}
		});
		list.getObject().setReuseItems(true);
		add(list.getObject());
	}

}
