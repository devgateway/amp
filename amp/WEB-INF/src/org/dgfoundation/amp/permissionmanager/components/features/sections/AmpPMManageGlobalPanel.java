/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpButtonField;
import org.dgfoundation.amp.onepager.components.fields.AmpFieldPanel;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * 
 * @author dan 
 */

public class AmpPMManageGlobalPanel extends Panel {
	
	
	
	public AmpPMManageGlobalPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, final IModel<PermissionMap> globalPermissionMapForPermissibleClassModel, final IModel<Permission> globalPermission, String fmName, boolean hideLabel) {
		super(id);
		super.setOutputMarkupId(true);
		
		List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
		
		List<Permission> globalPermissionsList = new ArrayList<Permission>(globalPermissionsModel.getObject());
		
		final Form form = new Form("ampGlobalPMForm"){
			protected void onSubmit() {
				info("asafsa");
			}
		};
		form.setOutputMarkupId(true);
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
		
//		AmpButtonField saveAndSubmit = new AmpButtonField("saveGlobalPermissionButton","Save") {
//
//			@Override
//			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				// TODO Auto-generated method stub
//				info("asfasfa");
//			}
//		};
//		saveAndSubmit.getButton().add(new AttributeModifier("class", true, new Model("buttonx")));
		
		Button saveAndSubmit = new Button("saveGlobalPermissionButton") {
		
					public void onSubmit() {
						// TODO Auto-generated method stub
						info("asfasfa");
					}
				};
		saveAndSubmit.setDefaultFormProcessing(false);
		form.add(saveAndSubmit);
		
		add(form);
	}
}
