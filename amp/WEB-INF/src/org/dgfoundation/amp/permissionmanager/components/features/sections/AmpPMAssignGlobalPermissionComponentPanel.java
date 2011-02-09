/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.sections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMGateWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.permissionmanager.web.PMUtil;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.gates.OrgRoleGate;
import org.digijava.module.gateperm.gates.UserLevelGate;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * @author dan
 *
 */
public class AmpPMAssignGlobalPermissionComponentPanel extends  AmpComponentPanel {

	public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName, AmpFMTypes fmType) {
		super(id, fmName, fmType);
	}

	public AmpPMAssignGlobalPermissionComponentPanel(String id, String fmName) {
		super(id, fmName);
	}

	public AmpPMAssignGlobalPermissionComponentPanel(String id, IModel model,
			String fmName, AmpFMTypes fmBehavior) {
		super(id, model, fmName, fmBehavior);
	}

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

		PermissionMap pmAux = null;
		pmAux	=	PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissionMapForPermissibleClassModel.getObject());

		Set<AmpPMGateWrapper> gatesSet = new TreeSet();
		if(pmAux==null){
			pmAux = createPermissionMap(globalPermissionMapForPermissibleClassModel);
		}
		final IModel<PermissionMap> pmAuxModel = new Model(pmAux);
		Permission pNothing=null;
		if(!(pmAuxModel.getObject().getPermission() instanceof CompositePermission))
			{
				pNothing = pmAuxModel.getObject().getPermission();
				pmAuxModel.getObject().setPermission(createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
						"This permission was created using the PM UI by admin user",false));
				
			}
		
		generateGatesList((CompositePermission)pmAuxModel.getObject().getPermission(),gatesSet);
//		else {
//			generateGatesList(createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
//					"This permission was created using the PM UI by admin user",false),gatesSet);
//		}
		final IModel<Set<AmpPMGateWrapper>> gatesSetModel = new Model((Serializable) gatesSet);
		
		final AmpPMAddPermFormTableFeaturePanel permGatesFormTable = new AmpPMAddPermFormTableFeaturePanel("gatePermForm", gatesSetModel, "Permission Form Table", false);
		permGatesFormTable.setTableWidth(300);
		permGatesFormTable.setOutputMarkupId(true);
		form.add(permGatesFormTable);
		
		DropDownChoice dropDownPermCategories = new DropDownChoice("globalPermCategories", globalPermissionMapForPermissibleClassModel ,availablePermissibleCategories, new ChoiceRenderer("simpleName"));
		dropDownPermCategories.add(new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				pmAuxModel.setObject(PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissionMapForPermissibleClassModel.getObject()));
				if(pmAuxModel.getObject()==null)
				{
					pmAuxModel.setObject(createPermissionMap(globalPermissionMapForPermissibleClassModel));
				}
				TreeSet<AmpPMGateWrapper> aa = new TreeSet<AmpPMGateWrapper>();
//				if(pmAuxModel.getObject().getPermission() instanceof CompositePermission)
//					generateGatesList((CompositePermission)pmAuxModel.getObject().getPermission(),aa);
//				else {
//					generateGatesList(createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
//							"This permission was created using the PM UI by admin user",false),aa);
//				}
				if(!(pmAuxModel.getObject().getPermission() instanceof CompositePermission))
					{
						Permission pNothing1 = pmAuxModel.getObject().getPermission();	
						pmAuxModel.getObject().setPermission(createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
							"This permission was created using the PM UI by admin user",false));
						
					}
				generateGatesList((CompositePermission)pmAuxModel.getObject().getPermission(),aa);
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
					//PMUtil.setGlobalPermission(globalPermissionMapForPermissibleClassModel.getObject(),globalPermissionModel.getObject(), globalPermissionMapForPermissibleClassModel.getObject().getSimpleName());
					PMUtil.assignGlobalPermission(pmAuxModel.getObject(),gatesSetModel.getObject());
					System.out.println("PM global permission assigned");
			}
		};
		form.add(saveAndSubmit);
		add(form);
	}

	private PermissionMap createPermissionMap(final IModel<Class> globalPermissionMapForPermissibleClassModel) {
		PermissionMap pmAux;
		pmAux = new PermissionMap();
		pmAux.setPermissibleCategory(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName());
		pmAux.setObjectIdentifier(null);
		pmAux.setPermission(createCompositePermission(globalPermissionMapForPermissibleClassModel.getObject().getSimpleName() + " - Composite Permission",
				"This permission was created using the PM UI by admin user",false));
		return pmAux;
	}
	
	private CompositePermission createCompositePermission(String name, String description, boolean dedicated){
		CompositePermission cp=new CompositePermission(dedicated);
		cp.setDescription(description);
		cp.setName(name);
		return cp;
	}
	
	
	private void generateDefaultGatesList(Set<AmpPMGateWrapper> gatesSet){
		gatesSet.add(new AmpPMGateWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
		gatesSet.add(new AmpPMGateWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
	}
	
	public void generateGatesList(Object o, Set<AmpPMGateWrapper> gatesSet){
		if(!(o instanceof CompositePermission)) return;
		CompositePermission cp = (CompositePermission)o;
		if(cp==null || cp.getId() == null) 
			{
				generateDefaultGatesList(gatesSet);
				return;
			}
    	gatesSet.add(new AmpPMGateWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, hasView(cp.getPermissions(),"BA"),hasEdit(cp.getPermissions(),"BA")));
		gatesSet.add(new AmpPMGateWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, hasView(cp.getPermissions(),"CA"),hasEdit(cp.getPermissions(),"CA")));
    	gatesSet.add(new AmpPMGateWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, hasView(cp.getPermissions(),"EA"),hasEdit(cp.getPermissions(),"EA")));
		gatesSet.add(new AmpPMGateWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, hasView(cp.getPermissions(),"IA"),hasEdit(cp.getPermissions(),"IA")));
		gatesSet.add(new AmpPMGateWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, hasView(cp.getPermissions(),"DN"),hasEdit(cp.getPermissions(),"DN")));
		gatesSet.add(new AmpPMGateWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, hasView(cp.getPermissions(),"SG"),hasEdit(cp.getPermissions(),"SG")));		
		gatesSet.add(new AmpPMGateWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, hasView(cp.getPermissions(),"RG"),hasEdit(cp.getPermissions(),"RG")));
		gatesSet.add(new AmpPMGateWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, hasView(cp.getPermissions(),"RO"),hasEdit(cp.getPermissions(),"RO")));
		gatesSet.add(new AmpPMGateWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_EVERYONE),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_EVERYONE)));
		gatesSet.add(new AmpPMGateWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_GUEST),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_GUEST)));
		gatesSet.add(new AmpPMGateWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_OWNER),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_OWNER)));
	}
	
	private Boolean hasEdit(Set<Permission> permissions, String param) {
		for (Permission p : permissions) {
			{
				GatePermission ap = (GatePermission)p;
				if(ap.hasParameter(param)) return hasEdit(ap);
			}
		}
		return false;
	}

	private Boolean hasView(Set<Permission> permissions, String param) {
		for (Permission p : permissions) {
			{
				GatePermission ap = (GatePermission)p;
				if(ap.hasParameter(param)) return hasView(ap);
			}
		}
		return false;
	}

	private boolean hasEdit(GatePermission agencyPerm) {
		return agencyPerm.hasAction(GatePermConst.Actions.EDIT);
	}

	private boolean hasView(GatePermission agencyPerm) {
		return agencyPerm.hasAction(GatePermConst.Actions.VIEW);
	}

}
