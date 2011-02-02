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
		// TODO Auto-generated constructor stub
		final IModel<Class> globalPermissionMapForPermissibleClassModel=new Model(null);
//		final IModel<Permission> globalPermissionModel = new Model(null);

		List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
		List<Permission> globalPermissionsList = new ArrayList<Permission>(globalPermissionsModel.getObject());
		
		final Form form = new Form("ampGlobalPMForm")
		{
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("ampGlobalPMForm submitted");
			}
		};
		form.setOutputMarkupId(true);

		
		final IModel<CompositePermission> cpModel = new Model(new CompositePermission(false));
		Set<AmpPMGateWrapper> a = new TreeSet();
		a.addAll(generateGatesList(cpModel.getObject(),a));
		final IModel<Set<AmpPMGateWrapper>> gatesSetModel = new Model((Serializable) a);
		
		final AmpPMAddPermFormTableFeaturePanel permGatesFormTable = new AmpPMAddPermFormTableFeaturePanel("gatePermForm", gatesSetModel, "Permission Form Table", false);
		permGatesFormTable.setTableWidth(300);
		permGatesFormTable.setOutputMarkupId(true);
		form.add(permGatesFormTable);
		
		final IModel<PermissionMap> pmModel = new Model(null);

		DropDownChoice dropDownPermCategories = new DropDownChoice("globalPermCategories", globalPermissionMapForPermissibleClassModel ,availablePermissibleCategories, new ChoiceRenderer("simpleName"));
		dropDownPermCategories.add(new OnChangeAjaxBehavior() {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: "+globalPermissionMapForPermissibleClassModel.getObject().getSimpleName());
				PermissionMap pmAux =	PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissionMapForPermissibleClassModel.getObject());
				if(pmAux!=null)
					{
						pmModel.setObject(pmAux);
						if(pmModel.getObject().getPermission() instanceof CompositePermission) 
							cpModel.setObject((CompositePermission)pmModel.getObject().getPermission());
						gatesSetModel.getObject().clear();
						generateGatesList(cpModel.getObject(),gatesSetModel.getObject());
						
						for (AmpPMGateWrapper agw : gatesSetModel.getObject()) {
							System.out.println("+++++++++++ "+agw.getName()+" : "+agw.getReadFlag()+": "+agw.getEditFlag());
						}
					}
//				target.addComponent(this.getComponent().getParent());
//				permGatesFormTable.setTableWidth(600);
				target.addComponent(AmpPMAssignGlobalPermissionComponentPanel.this);
//				permGatesFormTable.getList().removeAll();
//				target.addComponent(permGatesFormTable);
				//target.addComponent(permGatesFormTable.getList().getParent());

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

			//AjaxRequestTarget target, Form<?> form
			public void onSubmit() {
					// TODO Auto-generated method stub
					System.out.println("saveGlobalPermissionButton  submit pressed");
					//PMUtil.setGlobalPermission(globalPermissionMapForPermissibleClassModel.getObject(),globalPermissionModel.getObject(), globalPermissionMapForPermissibleClassModel.getObject().getSimpleName());
					
					System.out.println("PM global permission assigned");
			}
		};
		form.add(saveAndSubmit);
		add(form);
	}
	
	
	private Set<AmpPMGateWrapper> generateDefaultGatesList(){
		Set<AmpPMGateWrapper> gatesSet = new TreeSet<AmpPMGateWrapper>();
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
		return gatesSet;
	}
	
	public Set<AmpPMGateWrapper> generateGatesList(CompositePermission cp, Set<AmpPMGateWrapper> gatesSet){
		if(cp==null || cp.getId() == null) 
			return generateDefaultGatesList();
		//Set<AmpPMGateWrapper> gatesSet = new TreeSet<AmpPMGateWrapper>();
		cp.getPermissions();
		
		Iterator i=cp.getPermissions().iterator();
		while (i.hasNext()) {
		    GatePermission ap = (GatePermission) i.next();

		    if(ap.hasParameter("BA")) {
		    	gatesSet.add(new AmpPMGateWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, hasView(ap),hasEdit(ap)));
		    }
		    if(ap.hasParameter("CA")) {
				gatesSet.add(new AmpPMGateWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, hasView(ap),hasEdit(ap)));
		    }
		    if(ap.hasParameter("EA")) {		
		    	gatesSet.add(new AmpPMGateWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, hasView(ap),hasEdit(ap)));
		    }
		    if(ap.hasParameter("IA")) {
				gatesSet.add(new AmpPMGateWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, hasView(ap),hasEdit(ap)));
		    }
		    if(ap.hasParameter("DN")) {
				gatesSet.add(new AmpPMGateWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, hasView(ap),hasEdit(ap)));
		    }
		    if(ap.hasParameter("SG")) {
				gatesSet.add(new AmpPMGateWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, hasView(ap),hasEdit(ap)));		
			}
		    if(ap.hasParameter("RG")) {
				gatesSet.add(new AmpPMGateWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, hasView(ap),hasEdit(ap)));
			}  
		    if(ap.hasParameter("RO")) {
				gatesSet.add(new AmpPMGateWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, hasView(ap),hasEdit(ap)));
			}  
		    if(ap.hasParameter(UserLevelGate.PARAM_EVERYONE)) {
				gatesSet.add(new AmpPMGateWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, hasView(ap),hasEdit(ap)));
			}    
		    if(ap.hasParameter(UserLevelGate.PARAM_GUEST)) {
				gatesSet.add(new AmpPMGateWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, hasView(ap),hasEdit(ap)));
			}
		    if(ap.hasParameter(UserLevelGate.PARAM_OWNER)) {
				gatesSet.add(new AmpPMGateWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, hasView(ap),hasEdit(ap)));
			} 

		}
		return gatesSet;
		
	}

	private boolean hasEdit(GatePermission agencyPerm) {
		// TODO Auto-generated method stub
		return agencyPerm.hasAction(GatePermConst.Actions.EDIT);
	}

	private boolean hasView(GatePermission agencyPerm) {
		// TODO Auto-generated method stub
		return agencyPerm.hasAction(GatePermConst.Actions.VIEW);
	}

}
