/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMViewGlobalPermsTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;

/**
 * 
 * @author dan 
 */

public class AmpPMManageGlobalPanel extends AmpComponentPanel {
	
	
	
	public AmpPMManageGlobalPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, String fmName, boolean hideLabel) throws Exception {
		super(id, fmName);
		super.setOutputMarkupId(true);
		
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

		form.add(new Link("resetGlobalPermissionButton"){
			@Override
			public void onClick() {
				globalPermissionMapForPermissibleClassModel.setObject(null);
				globalPermissionModel.setObject(null);
				form.clearInput();
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
		
		AmpPMViewGlobalPermsTableFeaturePanel globalPermsTable = new AmpPMViewGlobalPermsTableFeaturePanel("viewGlobalPerms", globalPermissionsModel, "View Existing Global Permissions", true);
		globalPermsTable.setOutputMarkupId(true);
		add(globalPermsTable);
		add(new PagingNavigator("globalPermsNavigator", (PageableListView)globalPermsTable.getList()));
		
	}

}