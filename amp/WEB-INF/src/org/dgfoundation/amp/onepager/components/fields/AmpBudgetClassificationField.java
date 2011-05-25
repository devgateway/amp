/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.models.BudgetClassificationProxyModel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;


/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class AmpBudgetClassificationField extends AmpFieldPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DropDownChoice budgetProgram;
	private final DropDownChoice budgetDepartment;
	

	public AmpBudgetClassificationField(String id, final IModel model, String fmName) {
		super(id, model, fmName);
		this.fmType = AmpFMTypes.FEATURE;
		
		ArrayList<AmpBudgetSector> budgetSectors = BudgetDbUtil.getBudgetSectors();
		ArrayList<AmpTheme> budgetPrograms = BudgetDbUtil.getBudgetPrograms();
		final ArrayList<AmpOrganisation> budgetOrgs = new ArrayList<AmpOrganisation>();
		final ArrayList<AmpDepartments> budgetDepartments = new ArrayList<AmpDepartments>();
		
		final PropertyModel<Long> budgetSectorModel = new PropertyModel<Long>(model, "budgetsector");
		final PropertyModel<Long> budgetOrganizationModel = new PropertyModel<Long>(model, "budgetorganization");
		final PropertyModel<Long> budgetDepartmentModel = new PropertyModel<Long>(model, "budgetdepartment");
		final PropertyModel<Long> budgetProgramModel = new PropertyModel<Long>(model, "budgetprogram");
		
		if (budgetSectorModel.getObject() != null){
			budgetOrgs.clear();
			budgetOrgs.addAll(new ArrayList<AmpOrganisation>(BudgetDbUtil.getOrganizationsBySector(budgetSectorModel.getObject())));
		}
		
		if (budgetOrganizationModel.getObject() != null){
			budgetDepartments.clear();
			budgetDepartments.addAll(new ArrayList<AmpDepartments>(BudgetDbUtil.getDepartmentsbyOrg(budgetOrganizationModel.getObject())));
		}
		
		WebMarkupContainer activityBudgetHideable = new WebMarkupContainer("activityBudgetHideable");
		activityBudgetHideable.setOutputMarkupId(true);
		
		budgetProgram = new DropDownChoice("program", 
				new BudgetClassificationProxyModel(budgetProgramModel, "ampThemeId", budgetPrograms), 
				budgetPrograms, new ChoiceRenderer("name", "ampThemeId")); 
		budgetProgram.setOutputMarkupId(true);
		activityBudgetHideable.add(budgetProgram);
		
		budgetDepartment = new DropDownChoice("department", 
				new BudgetClassificationProxyModel(budgetDepartmentModel,"id", budgetDepartments), 
				budgetDepartments, new ChoiceRenderer("name", "id")); 
		budgetDepartment.setOutputMarkupId(true);
		activityBudgetHideable.add(budgetDepartment);
		
		add(activityBudgetHideable);
		
		final DropDownChoice budgetOrganization = new DropDownChoice("organization", 
				new BudgetClassificationProxyModel(budgetOrganizationModel, "ampOrgId", budgetOrgs), 
				budgetOrgs, new ChoiceRenderer("name", "ampOrgId")); 
		budgetOrganization.setOutputMarkupId(true);
		budgetOrganization.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Long orgId = budgetOrganizationModel.getObject();
				if (orgId != null){
					budgetDepartments.clear();
					budgetDepartments.addAll(new ArrayList<AmpDepartments>(BudgetDbUtil.getDepartmentsbyOrg(orgId)));
					
					target.addComponent(budgetDepartment);
				}
			}
		});
		add(budgetOrganization);
		
		
		DropDownChoice budgetSector = new DropDownChoice("sector", 
				new BudgetClassificationProxyModel(budgetSectorModel, "idsector", budgetSectors), 
				budgetSectors, new ChoiceRenderer("sectorname", "idsector")); 
		budgetSector.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Long sectorId = budgetSectorModel.getObject();
				if (sectorId != null){
					budgetOrgs.clear();
					budgetOrgs.addAll(new ArrayList<AmpOrganisation>(BudgetDbUtil.getOrganizationsBySector(sectorId)));
					
					target.addComponent(budgetOrganization);
				}
			}
		});
		
		add(budgetSector);
	}
	
	public void toggleActivityBudgetVisibility(boolean b){
		budgetDepartment.setVisible(b);
		budgetProgram.setVisible(b);
	}
	
	public void addToTargetActivityBudget(AjaxRequestTarget target){
		target.addComponent(budgetDepartment.getParent());
		//same parent, but just in case someone will change the code and split the components
		target.addComponent(budgetProgram.getParent());
	}
}
