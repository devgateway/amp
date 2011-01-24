/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * 
 * @author dan 
 */

public class AmpPMManageGlobalPanel extends AmpFieldPanel {
	
	
	
	public AmpPMManageGlobalPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, final IModel<PermissionMap> globalPermissionMapForPermissibleClassModel, final IModel<Permission> globalPermission, String fmName, boolean hideLabel) {
		super(id, globalPermissionsModel, fmName, hideLabel);
		super.setOutputMarkupId(true);
		
		List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
		
		List<Permission> globalPermissionsList = new ArrayList<Permission>(globalPermissionsModel.getObject());
		
		final Form form = new Form("ampGlobalPMForm"){
			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				System.out.println("form was submitted");
			}	
		};
		DropDownChoice dropDownPermCategories = new DropDownChoice("globalPermCategories", globalPermissionMapForPermissibleClassModel ,availablePermissibleCategories, new ChoiceRenderer("simpleName"));
		dropDownPermCategories.setNullValid(false);
		form.add(dropDownPermCategories);
		
		DropDownChoice dropDownPerms = new DropDownChoice("globalPerms", globalPermission ,globalPermissionsList, new ChoiceRenderer("name"));
		form.add(dropDownPerms);

		form.add(new Link("resetGlobalPermissionButton"){
			@Override
			public void onClick() {
				form.clearInput();
			}
		});
		
//		form.add(new Link("saveGlobalPermissionButton"){
//			@Override
//			public void onClick() {
//				form.clearInput();
//			}
//		});
		
		Button saveAndSubmit = new Button("saveGlobalPermissionButton") {
			public void onSubmit() {
				System.out.println("submitted button");
				info("!submitted button!");
				
			}
		};
//		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		form.add(saveAndSubmit);
		
		add(form);
	}

	public AmpPMManageGlobalPanel(String id, IModel model, String fmName) {
		super(id, model, fmName);
	}

	public AmpPMManageGlobalPanel(String id, String fmName, boolean hideLabel) {
		super(id, fmName, hideLabel);
	}

}
