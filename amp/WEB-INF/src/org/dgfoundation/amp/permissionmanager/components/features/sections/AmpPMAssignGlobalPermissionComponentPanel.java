/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;

/**
 * @author dan
 *
 */
public class AmpPMAssignGlobalPermissionComponentPanel extends
		AmpComponentPanel {

	/**
	 * @param id
	 * @param fmName
	 * @param fmType
	 */
	public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName,
			AmpFMTypes fmType) {
		super(id, fmName, fmType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param fmName
	 */
	public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName) {
		super(id, fmName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param fmBehavior
	 */
	public AmpPMAssignGlobalPermissionComponentPanel(String id, IModel model,
			String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 */
	public AmpPMAssignGlobalPermissionComponentPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, String fmName) {
		super(id, globalPermissionsModel, fmName);
		// TODO Auto-generated constructor stub
		final IModel<Class> globalPermissionMapForPermissibleClassModel=new Model(null);
		final IModel<Permission> globalPermissionModel = new Model(null);

		List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
		List<Permission> globalPermissionsList = new ArrayList<Permission>(globalPermissionsModel.getObject());
		
		final Form form = new Form("ampGlobalPMForm")
		{
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("ampGlobalPMForm submitted");
			}
		};
		form.setOutputMarkupId(true);
		
		DropDownChoice dropDownPermCategories = new DropDownChoice("globalPermCategories", globalPermissionMapForPermissibleClassModel ,availablePermissibleCategories, new ChoiceRenderer("simpleName"));
		form.add(dropDownPermCategories);
		
		DropDownChoice dropDownPerms = new DropDownChoice("globalPerms", globalPermissionModel, globalPermissionsList, new ChoiceRenderer("name"));
		form.add(dropDownPerms);

		form.add(new AjaxFallbackLink("resetGlobalPermissionButton"){
			//@Override
			public void onClick(AjaxRequestTarget target) {
				//globalPermissionMapForPermissibleClassModel.setObject(null);
				//globalPermissionModel.setObject(null);
				form.clearInput();
				target.addComponent(AmpPMAssignGlobalPermissionComponentPanel.this);
			}
		});
		
		Button saveAndSubmit = new Button("saveGlobalPermissionButton") {

			//AjaxRequestTarget target, Form<?> form
			public void onSubmit() {
					// TODO Auto-generated method stub
					System.out.println("saveGlobalPermissionButton  submit pressed");
					PMUtil.setGlobalPermission(globalPermissionMapForPermissibleClassModel.getObject(),globalPermissionModel.getObject(), globalPermissionMapForPermissibleClassModel.getObject().getSimpleName());
					System.out.println("PM global permission assigned");
			}
		};
		form.add(saveAndSubmit);
		add(form);
	}

}
