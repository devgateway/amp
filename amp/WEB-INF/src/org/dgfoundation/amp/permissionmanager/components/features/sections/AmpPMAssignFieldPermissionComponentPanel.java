/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.tree.TreeModel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMObjectVisibilitySearchModel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * @author dan
 *
 */
public class AmpPMAssignFieldPermissionComponentPanel extends AmpComponentPanel {

	private Boolean showWorkspace=true;

	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param teamsModel 
	 * @param ampTreeVisibilityModel2 
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id,final IModel<AmpTreeVisibilityModelBean> ampTreeVisibilityBeanModel, String fmName, IModel<Set<AmpTeam>> teamsModel, final IModel<AmpTreeVisibility> ampTreeVisibilityModel) {
		super(id, ampTreeVisibilityBeanModel, fmName);
		// TODO Auto-generated constructor stub
		
		final Form form = new Form("ampFieldPMForm")
		{
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("ampFieldPMForm submitted");
			}
		};
		form.setOutputMarkupId(true);
		
		this.showWorkspace=true;
		final TransparentWebMarkupContainer workspaces = new TransparentWebMarkupContainer("workspaces");
		workspaces.setOutputMarkupId(true);
		form.add(workspaces);
		
		List<String> permissionPriority = PMUtil.getPermissionPriority();
		final IModel<String> permissionChoiceModel = new Model(permissionPriority.get(0));
		
		//permission priority radiobutton
		final RadioChoice permissionPriorityChoices = new RadioChoice("permissionPriorityChoices", permissionChoiceModel,	permissionPriority);

		permissionPriorityChoices.setSuffix("");
		form.add(permissionPriorityChoices);
		
		final Label permPriorityLabel = new Label("permPriorityLabel", "Permission Priority:");
		form.add(permPriorityLabel);
		
		//show workspace checkbox
		AjaxCheckBox showWorkspaceCheckBox =	new AjaxCheckBox("showWorkspace", new Model(this.showWorkspace)){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				setShowWorkspaces(!getShowWorkspaces());
				workspaces.setVisible(getShowWorkspaces());
				permPriorityLabel.setVisible(getShowWorkspaces());
				permissionPriorityChoices.setVisible(getShowWorkspaces());
				target.addComponent(AmpPMAssignFieldPermissionComponentPanel.this);
			}
			
		};
		showWorkspaceCheckBox.setOutputMarkupId(true);
		form.add(showWorkspaceCheckBox);

		//FM tree
		TreeModel treeModel = PMUtil.createTreeModel(ampTreeVisibilityBeanModel);
		final IModel<TreeModel> iTreeModel = new Model((Serializable) treeModel);
		final AmpPMTreeVisibilityFieldPermission tree = new AmpPMTreeVisibilityFieldPermission("fmFieldsPanel", iTreeModel, "FM Fields Panel");
		tree.setOutputMarkupId(true);
		form.add(tree);
		
		//search text box
		final AbstractAmpAutoCompleteTextField<AmpObjectVisibility> autoComplete = new AbstractAmpAutoCompleteTextField<AmpObjectVisibility>(AmpPMObjectVisibilitySearchModel.class) {
			@Override
			protected String getChoiceValue(AmpObjectVisibility choice) throws Throwable {
				return choice.getName();
			}
			@Override
			public void onSelect(AjaxRequestTarget target, AmpObjectVisibility choice) {
				ampTreeVisibilityBeanModel.setObject(PMUtil.buildTreeObjectFMPermissions(choice));
				iTreeModel.setObject(PMUtil.createTreeModel(ampTreeVisibilityBeanModel));
				tree.refreshTree(iTreeModel);
				target.addComponent(AmpPMAssignFieldPermissionComponentPanel.this);
			}
			@Override
			public Integer getChoiceLevel(AmpObjectVisibility choice) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		AttributeModifier sizeModifier = new AttributeModifier("size",new Model(43));
		autoComplete.add(sizeModifier);
		final AmpComboboxFieldPanel<AmpObjectVisibility> searchFields=new AmpComboboxFieldPanel<AmpObjectVisibility>("searchFields", "Search Fields", autoComplete,false,true);
		form.add(searchFields);
		
		
		PermissionMap permMap =	PMUtil.createPermissionMap(AmpFieldsVisibility.class, true);
		IModel<PermissionMap> permMapModel = new Model(permMap);
		TreeSet<AmpPMReadEditWrapper> gatesSet = new TreeSet<AmpPMReadEditWrapper>();
		PMUtil.generateGatesList((CompositePermission)permMapModel.getObject().getPermission(),gatesSet);
		final IModel<Set<AmpPMReadEditWrapper>> gatesSetModel = new Model((Serializable) gatesSet);
		final AmpPMAddPermFormTableFeaturePanel permGatesFieldsFormTable = new AmpPMAddPermFormTableFeaturePanel("permGatesFieldsForm", gatesSetModel, "Gate Form Table", true);
		permGatesFieldsFormTable.setTableWidth(470);
		permGatesFieldsFormTable.setOutputMarkupId(true);
		form.add(permGatesFieldsFormTable);
		
		TreeSet<AmpPMReadEditWrapper> workspacesSet = new TreeSet<AmpPMReadEditWrapper>();
		PMUtil.generateWorkspacesList(teamsModel, workspacesSet);
		final IModel<Set<AmpPMReadEditWrapper>> workspacesSetModel = new Model((Serializable) workspacesSet);
		final AmpPMAddPermFormTableFeaturePanel permWorkspacesFieldsFormTable = new AmpPMAddPermFormTableFeaturePanel("permWorkspacesFieldsForm", workspacesSetModel, "Workspaces Form Table", true);
		permWorkspacesFieldsFormTable.setTableWidth(470);
		permWorkspacesFieldsFormTable.setOutputMarkupId(true);
		form.add(permWorkspacesFieldsFormTable);
		
		PMUtil.setPermissionPriorityVisibility(permissionChoiceModel, permGatesFieldsFormTable, permWorkspacesFieldsFormTable);
		
		
		permissionPriorityChoices.add(new AjaxFormChoiceComponentUpdatingBehavior() {
	        @Override
	        protected void onUpdate(AjaxRequestTarget target) {
	          target.addComponent(AmpPMAssignFieldPermissionComponentPanel.this);
	          PMUtil.setPermissionPriorityVisibility(permissionChoiceModel, permGatesFieldsFormTable, permWorkspacesFieldsFormTable);
	        }

	      }); 
		
		
		form.add(new AjaxFallbackLink("resetFieldPermissionButton"){
			//@Override
			public void onClick(AjaxRequestTarget target) {
				form.clearInput();
				target.addComponent(AmpPMAssignFieldPermissionComponentPanel.this);
			}
		});
		
		Button saveAndSubmit = new Button("saveFieldPermissionButton") {
			public void onSubmit() {
					System.out.println("saveFieldPermissionButton  submit pressed");
					if(PMUtil.ROLE_PERMISSION.compareTo(permissionChoiceModel.getObject()) == 0)
						PMUtil.assignFieldsPermission(iTreeModel, gatesSetModel, null);
					if(PMUtil.CUMMULATIVE.compareTo(permissionChoiceModel.getObject()) == 0)
						PMUtil.assignFieldsPermission(iTreeModel, gatesSetModel, workspacesSetModel);
					if(PMUtil.WORKSPACE_PERMISSION.compareTo(permissionChoiceModel.getObject()) == 0)
						PMUtil.assignFieldsPermission(iTreeModel, null, workspacesSetModel);
					
					System.out.println("PM field permission assigned");
			}
		};
		form.add(saveAndSubmit);
		
		add(form);
	}

	public Boolean getShowWorkspaces(){
		return this.showWorkspace;
	}
	
	
	public void setShowWorkspaces(Boolean workspace){
		this.showWorkspace=workspace;
	}
	
	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, String fmName,AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}


}
