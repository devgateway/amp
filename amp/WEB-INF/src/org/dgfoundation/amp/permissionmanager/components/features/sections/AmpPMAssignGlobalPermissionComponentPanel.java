/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMGateReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * @author dan
 *
 */
public class AmpPMAssignGlobalPermissionComponentPanel extends  AmpComponentPanel {


	public AmpPMAssignGlobalPermissionComponentPanel(String id,  IModel<Set<Permission>> globalPermissionsModel, String fmName) {
		super(id, globalPermissionsModel, fmName);

		List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
		final IModel<Class> globalPermissionMapForPermissibleClassModel=new Model(availablePermissibleCategories.get(0));
		
		final Form form = new Form("ampGlobalPMForm")
		{
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("ampGlobalPMForm submitted");
			}
		};
		form.setOutputMarkupId(true);

		final IModel<PermissionMap> pmAuxModel = new Model(null);

		Set<AmpPMReadEditWrapper> gatesSet = null;
		try {
			gatesSet =	populateGatesSet(globalPermissionMapForPermissibleClassModel, pmAuxModel);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final IModel<Set<AmpPMReadEditWrapper>> gatesSetModel = new Model((Serializable) gatesSet);
		
		final AmpPMAddPermFormTableFeaturePanel permGatesFormTable = new AmpPMAddPermFormTableFeaturePanel("gatePermForm", gatesSetModel, "Permission Form Table", true);
		permGatesFormTable.setTableWidth(300);
		permGatesFormTable.setOutputMarkupId(true);
		form.add(permGatesFormTable);
		
		DropDownChoice dropDownPermCategories = new DropDownChoice("globalPermCategories", globalPermissionMapForPermissibleClassModel ,availablePermissibleCategories, new ChoiceRenderer("simpleName"));
		dropDownPermCategories.add(new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Set<AmpPMReadEditWrapper> aa = null;
				try {
					aa = populateGatesSet(globalPermissionMapForPermissibleClassModel, pmAuxModel);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gatesSetModel.setObject(aa);
				target.addComponent(AmpPMAssignGlobalPermissionComponentPanel.this);
			}
		});
		form.add(dropDownPermCategories);

//		DropDownChoice dropDownPerms = new DropDownChoice("globalPerms", globalPermissionModel, globalPermissionsList, new ChoiceRenderer("name"));
//		form.add(dropDownPerms);

		form.add(new AjaxFallbackLink("resetGlobalPermissionButton"){
			//@Override
			public void onClick(AjaxRequestTarget target) {
				form.clearInput();
				target.addComponent(AmpPMAssignGlobalPermissionComponentPanel.this);
			}
		});
		
		Button saveAndSubmit = new Button("saveGlobalPermissionButton") {
			public void onSubmit() {
					System.out.println("saveGlobalPermissionButton  submit pressed");
					PMUtil.assignGlobalPermission(pmAuxModel.getObject(),gatesSetModel.getObject(), globalPermissionMapForPermissibleClassModel.getObject());
					System.out.println("PM global permission assigned");
			}
		};
		form.add(saveAndSubmit);
		add(form);
	}

	private Set<AmpPMReadEditWrapper> populateGatesSet(final IModel<Class> globalPermissionMapForPermissibleClassModel,final IModel<PermissionMap> pmAuxModel) throws Exception{
		Set<AmpPMReadEditWrapper> gatesSet = new TreeSet<AmpPMReadEditWrapper>();
		System.out.println("on update: "+globalPermissionMapForPermissibleClassModel.getObject());
		PermissionMap pmAux = null;
		pmAux	=	PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissionMapForPermissibleClassModel.getObject());
		if(pmAux==null || pmAux.getPermission()==null){
			pmAux = PMUtil.createPermissionMap(globalPermissionMapForPermissibleClassModel.getObject(), true);
		}
		if(!(pmAux.getPermission() instanceof CompositePermission)){
			pmAux.setPermission(PMUtil.createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
					"This permission was created using the PM UI by admin user",false));
		}
		pmAuxModel.setObject(pmAux);
		PMUtil.generateGatesList((CompositePermission)pmAuxModel.getObject().getPermission(),gatesSet);
		return gatesSet;
	}

	public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName, AmpFMTypes fmType) {
		super(id, fmName, fmType);
	}

	public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName) {
		super(id, fmName);
	}

	public AmpPMAssignGlobalPermissionComponentPanel(String id, IModel model, String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
	}

	
	

	


}
