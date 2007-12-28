/**
 * AssignFieldPermissions.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.aim.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.form.FieldPermissionsForm;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.gates.OrgRoleGate;
import org.digijava.module.gateperm.util.PermissionUtil;

/**
 * AssignFieldPermissions.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.aim.action
 * @since 01.11.2007
 */
public class AssignFieldPermissions extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
	
	FieldPermissionsForm fpf=(FieldPermissionsForm) form;
	Long fieldId=Long.parseLong(request.getParameter("fieldId"));
	
	
	Session session = PersistenceManager.getSession();
	Map permScope= PermissionUtil.resetScope(request.getSession());
	AmpFieldsVisibility afv=(AmpFieldsVisibility) session.get(AmpFieldsVisibility.class, fieldId);
	fpf.setFieldName(afv.getName());

	//saving the data
	if(request.getParameter("save")!=null) { 
		PermissionMap permissionMap = PermissionUtil.getOwnPermissionMapForPermissible(afv);
		if(permissionMap!=null) { 
		    Permission p=permissionMap.getPermission();
		    //we delete the old permissions, if they are dedicated
		    if (p!=null && p.isDedicated()) {
			CompositePermission cp = (CompositePermission)p;
			Iterator<Permission> i = cp.getPermissions().iterator();
			while (i.hasNext()) {
			    Permission element = (Permission) i.next();
			    Object object = session.load(Permission.class, element.getId());
			    session.delete(object);
			}
			    Object object = session.load(Permission.class, cp.getId());
			session.delete(object);
		    }
		    Object object = session.load(PermissionMap.class, permissionMap.getId());
		    session.delete(object);
		    session.flush();
		}
	
		//we create a fresh list of permissions and bind them with the field
		permissionMap=new PermissionMap(); 
		permissionMap.setPermissibleCategory(afv.getPermissibleCategory().getSimpleName());
		permissionMap.setObjectIdentifier(afv.getId());
		
		CompositePermission cp=new CompositePermission(true);
		
		cp.setName(afv.getName()+" - Composite Permission");
		
		GatePermission baGate=new GatePermission(true);
		baGate.setName(afv.getName()+ " - Beneficiary Agency Permission");
		baGate.getGateParameters().add("BA");
		baGate.setGateTypeName(OrgRoleGate.class.getName());
		
		GatePermission caGate=new GatePermission(true);
		caGate.setName(afv.getName()+ " - Contracting Agency Permission");
		caGate.getGateParameters().add("CA");
		caGate.setGateTypeName(OrgRoleGate.class.getName());
		
		GatePermission eaGate=new GatePermission(true);
		eaGate.setName(afv.getName()+ " - Executing Agency Permission");
		eaGate.getGateParameters().add("EA");
		eaGate.setGateTypeName(OrgRoleGate.class.getName());
		
		GatePermission iaGate=new GatePermission(true);
		iaGate.setName(afv.getName()+ " - Implementing Agency Permission");
		iaGate.getGateParameters().add("IA");
		iaGate.setGateTypeName(OrgRoleGate.class.getName());
		
		GatePermission faGate=new GatePermission(true);
		faGate.setName(afv.getName()+ " - Funding Agency Permission");
		faGate.getGateParameters().add("DN");
		faGate.setGateTypeName(OrgRoleGate.class.getName());
		
		GatePermission rgGate=new GatePermission(true);
		rgGate.setName(afv.getName()+ " - Regional Group Permission");
		rgGate.getGateParameters().add("RG");
		rgGate.setGateTypeName(OrgRoleGate.class.getName());
		
		GatePermission sgGate=new GatePermission(true);
		sgGate.setName(afv.getName()+ " - Sector Group Permission");
		sgGate.getGateParameters().add("SG");
		sgGate.setGateTypeName(OrgRoleGate.class.getName());
		
		
		HashSet baActions=new HashSet();
		HashSet caActions=new HashSet();
		HashSet eaActions=new HashSet();
		HashSet iaActions=new HashSet();
		HashSet faActions=new HashSet();
		HashSet rgActions=new HashSet();
		HashSet sgActions=new HashSet();
		
		if("on".equals(fpf.getBaEdit())) baActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getBaRead())) baActions.add(GatePermConst.Actions.VIEW);
		
		if("on".equals(fpf.getCaEdit())) caActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getCaRead())) caActions.add(GatePermConst.Actions.VIEW);
		
		if("on".equals(fpf.getEaEdit())) eaActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getEaRead())) eaActions.add(GatePermConst.Actions.VIEW);
	
		if("on".equals(fpf.getIaEdit())) iaActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getIaRead())) iaActions.add(GatePermConst.Actions.VIEW);
	
		if("on".equals(fpf.getFaEdit())) faActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getFaRead())) faActions.add(GatePermConst.Actions.VIEW);

		if("on".equals(fpf.getRgEdit())) rgActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getRgRead())) rgActions.add(GatePermConst.Actions.VIEW);

		if("on".equals(fpf.getSgEdit())) sgActions.add(GatePermConst.Actions.EDIT);
		if("on".equals(fpf.getSgRead())) sgActions.add(GatePermConst.Actions.VIEW);

		
		baGate.setActions(baActions);
		caGate.setActions(caActions);
		eaGate.setActions(eaActions);
		iaGate.setActions(iaActions);
		faGate.setActions(faActions);
		rgGate.setActions(rgActions);
		sgGate.setActions(sgActions);
		
		
		if(baGate.getActions().size()>0)  session.save(baGate);
		if(caGate.getActions().size()>0)  session.save(caGate);
		if(eaGate.getActions().size()>0)  session.save(eaGate);
		if(iaGate.getActions().size()>0)  session.save(iaGate);
		if(faGate.getActions().size()>0)  session.save(faGate);		
		if(rgGate.getActions().size()>0)  session.save(rgGate);
		if(sgGate.getActions().size()>0)  session.save(sgGate);		
		
		
		if(baGate.getActions().size()>0)  cp.getPermissions().add(baGate);
		if(caGate.getActions().size()>0)  cp.getPermissions().add(caGate);
		if(eaGate.getActions().size()>0)  cp.getPermissions().add(eaGate);
		if(iaGate.getActions().size()>0)  cp.getPermissions().add(iaGate);
		if(faGate.getActions().size()>0)  cp.getPermissions().add(faGate);		
		if(rgGate.getActions().size()>0)  cp.getPermissions().add(rgGate);
		if(sgGate.getActions().size()>0)  cp.getPermissions().add(sgGate);		
	
		
		session.save(cp);
		
		permissionMap.setPermission(cp);
		
		session.save(permissionMap);
		
	    request.setAttribute("close", "close");
	} else { 
	    //loading data ... 
	    Permission permission = afv.getPermission(true);		
	    if(permission!=null && permission.isDedicated() && permission instanceof CompositePermission) {
		CompositePermission cp=(CompositePermission) permission;
		Iterator i=cp.getPermissions().iterator();
		while (i.hasNext()) {
		    GatePermission agencyPerm = (GatePermission) i.next();
		    if(agencyPerm.hasParameter("BA")) {
			if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setBaEdit("on");
			if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setBaRead("on");			
		    }
		    if(agencyPerm.hasParameter("CA")) {
			if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setCaEdit("on");
			if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setCaRead("on");			
		    }
		    if(agencyPerm.hasParameter("EA")) {
			if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setEaEdit("on");
			if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setEaRead("on");			
		    }
		    if(agencyPerm.hasParameter("IA")) {
			if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setIaEdit("on");
			if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setIaRead("on");			
		    }
		    if(agencyPerm.hasParameter("DN")) {
			if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setFaEdit("on");
			if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setFaRead("on");			
		    }
		    if(agencyPerm.hasParameter("SG")) {
				if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setSgEdit("on");
				if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setSgRead("on");			
			}
		    if(agencyPerm.hasParameter("RG")) {
				if(agencyPerm.hasAction(GatePermConst.Actions.EDIT)) fpf.setRgEdit("on");
				if(agencyPerm.hasAction(GatePermConst.Actions.VIEW)) fpf.setRgRead("on");			
			}    
		}
	    } else { 
		//initialize everyone + guest
		fpf.setEvEdit(null);
		fpf.setGuEdit(null);
		fpf.setEvRead("on");
		fpf.setGuRead("on");
		
		
		//get the global for the fields:
		Permission globalPermissionForPermissibleClass = PermissionUtil.getGlobalPermissionForPermissibleClass(AmpFieldsVisibility.class);
		if(globalPermissionForPermissibleClass!=null) {Set<String> allowedActions = globalPermissionForPermissibleClass.getAllowedActions(permScope);
		if(allowedActions.contains(GatePermConst.Actions.EDIT)) {
		    fpf.setBaEdit("on");
		    fpf.setCaEdit("on");
		    fpf.setEaEdit("on");
		    fpf.setIaEdit("on");
		    fpf.setFaEdit("on");
		    fpf.setRgEdit("on");
		    fpf.setSgEdit("on");
		}
		if(allowedActions.contains(GatePermConst.Actions.VIEW)) {
		    fpf.setBaRead("on");
		    fpf.setCaRead("on");
		    fpf.setEaRead("on");
		    fpf.setIaRead("on");
		    fpf.setFaRead("on");
		    fpf.setRgRead("on");
		    fpf.setSgRead("on");
		}
		}
		
		
	    }
	}
	session.flush();

	PersistenceManager.releaseSession(session);
	
	return mapping.getInputForward();
    }
}
