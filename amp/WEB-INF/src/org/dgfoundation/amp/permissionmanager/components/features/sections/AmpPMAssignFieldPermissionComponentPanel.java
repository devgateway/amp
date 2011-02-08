/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

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
	 */
	public AmpPMAssignFieldPermissionComponentPanel(String id, IModel<AmpTreeVisibility> ampTreeVisibilityModel, String fmName) {
		super(id, ampTreeVisibilityModel, fmName);
		// TODO Auto-generated constructor stub
		this.showWorkspace=true;

		final TransparentWebMarkupContainer a = new TransparentWebMarkupContainer("workspaces");
		
		AjaxCheckBox showWorkspaceCheckBox =	new AjaxCheckBox("showWorkspace", new Model(this.showWorkspace)){
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// TODO Auto-generated method stub
				setShowWorkspaces(!getShowWorkspaces());
				a.setVisible(getShowWorkspaces());
				target.addComponent(AmpPMAssignFieldPermissionComponentPanel.this);
			}
			
		};
		showWorkspaceCheckBox.setOutputMarkupId(true);
		add(showWorkspaceCheckBox);
		
		a.setOutputMarkupId(true);
		add(a);
		
		
		List<String> permissionPriority = new ArrayList<String>();
		permissionPriority.add(PMUtil.CUMMULATIVE);
		permissionPriority.add(PMUtil.ROLE_PERMISSION);
		permissionPriority.add(PMUtil.WORKSPACE_PERMISSION);
		IModel<String> permissionChoiceModel = new Model(permissionPriority.get(0));
		
		RadioChoice permissionPriorityChoices = new RadioChoice("permissionPriorityChoices", permissionChoiceModel,	permissionPriority);
		permissionPriorityChoices.setSuffix("");
		add(permissionPriorityChoices);
		
		AmpPMTreeVisibilityFieldPermission tree = new AmpPMTreeVisibilityFieldPermission("fmFieldsPanel", permissionChoiceModel, "FM Fields Panel");
		add(tree);
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
