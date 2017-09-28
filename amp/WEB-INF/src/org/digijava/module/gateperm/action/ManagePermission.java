/**
 * ManagePermission.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermission;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.form.PermissionForm;
import org.digijava.module.gateperm.util.ActionUtil;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.BeanWrapperImpl;

/**
 * ManagePermission.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.action
 * @since 05.09.2007
 */
public class ManagePermission extends MultiAction {

    /**
         * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
         *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
         *      javax.servlet.http.HttpServletResponse)
         */
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

    PermissionForm pf = (PermissionForm) form;

    List<Class> gates = new ArrayList<Class>();
    List<Class> name = Arrays.asList(PermissionUtil.getAvailableGates(this.getServlet().getServletContext()));
    gates.addAll(name);
    pf.set_availableGateTypes(gates);


    
    List<BeanWrapperImpl> actions = (List<BeanWrapperImpl>) Util.createBeanWrapperItemsCollection(ActionUtil.getAvailableActions());

    pf.set_availableActions(actions);

    List<Permission> permissions = PermissionUtil.getAllPermissions();
    pf.set_availablePermissions(permissions);

    return modeSelect(mapping, form, request, response);
    }

    /**
         * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping,
         *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
         *      javax.servlet.http.HttpServletResponse)
         */
    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    PermissionForm pf = (PermissionForm) form;
    String mode = pf.getMode();

    if (request.getParameter("delete") != null)
        return modeDelete(mapping, pf, request, response);
    
    if (request.getParameter("new") != null)
        return modeNew(mapping, pf, request, response);
    if ("save".equals(mode))
        return modeSave(mapping, pf, request, response);
    if ("type".equals(mode))
        return modeTypeSelect(mapping, pf, request, response);
    if ("gate".equals(mode))
        return modeGateSelect(mapping, pf, request, response);
    if ("permission".equals(mode))
        return modePermissionSelect(mapping, pf, request, response);
    if (request.getParameter("edit") != null)
        return modeEditExisting(mapping, pf, request, response);
    if (request.getParameter("list") != null)
        return modeListExisting(mapping, pf, request, response);

    return mapping.getInputForward();
    }

    private ActionForward modeDelete(ActionMapping mapping, PermissionForm pf, HttpServletRequest request, HttpServletResponse response) throws Exception {
    Long id = Long.parseLong(request.getParameter("permissionId"));
    PermissionUtil.deletePermission(id);
    return modeListExisting(mapping, pf, request, response);
    }

    private ActionForward modeListExisting(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

    request.setAttribute("allPermissions", PermissionUtil.getAllPermissions());

    return mapping.findForward("list");
    }

    private ActionForward modeNew(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

    pf.clear();

    return mapping.getInputForward();
    }

    private ActionForward modeEditExisting(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    Session hs = PersistenceManager.getSession();
    Long id = Long.parseLong(request.getParameter("permissionId"));
    Permission p = (Permission) hs.get(Permission.class, id);
    pf.clear();
    pf.setName(p.getName());
    pf.setDescription(p.getDescription());
    pf.setMode(null);
    pf.setId(p.getId());

    if (p instanceof GatePermission) {
        pf.setType("Gate");
        GatePermission gp = (GatePermission) p;
        pf.setGateTypeName(gp.getGateTypeName());
        Gate gate = Gate.instantiateGate( pf.getGateTypeName());
        for (int i = 0; gate.parameterInfo()!=null && i < gate.parameterInfo().length; i++) {
        MetaInfo mi = new MetaInfo(gate.parameterInfo()[i].getCategory(), new MetaInfo(gate.parameterInfo()[i].getValue().toString(),(Comparable) gp.getGateParameters().get(i)));
        pf.getGateParameters().add(mi);
        }

        pf.setActions(gp.getActions().toArray(new String[gp.getActions().size()]));
    }

    if (p instanceof CompositePermission) {
        pf.setType("Composite");
        CompositePermission cp = (CompositePermission) p;
        ArrayList<Long> ids = new ArrayList<Long>();
        Iterator i = cp.getPermissions().iterator();
        while (i.hasNext()) {
        Permission element = (Permission) i.next();
        ids.add(element.getId());
        }
        pf.setPermissions(ids.toArray(new Long[ids.size()]));
        pf.setIntersection(cp.getIntersection());
    }

    return mapping.getInputForward();
    }

    private ActionForward modePermissionSelect(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    pf.setPermissions(null);
    pf.getGateParameters().clear();

    pf.setMode(null);
    return mapping.getInputForward();
    }

    private ActionForward modeGateSelect(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    pf.setPermissions(null);
    pf.getGateParameters().clear();

    if (!pf.getGateTypeName().equals("unselected")) {

        Gate gate = Gate.instantiateGate( pf.getGateTypeName());
        for (int i = 0; gate.parameterInfo()!=null && i < gate.parameterInfo().length; i++) {
            MetaInfo mi = new MetaInfo(gate.parameterInfo()[i].getCategory(), new MetaInfo(gate.parameterInfo()[i].getValue().toString(),""));
        pf.getGateParameters().add(mi);
        }

    }

    pf.setMode(null);
    return mapping.getInputForward();
    }

    private ActionForward modeTypeSelect(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    pf.setPermissions(null);
    pf.getGateParameters().clear();

    return "Gate".equals(pf.getType()) ? modeGateSelect(mapping, pf, request, response) : modePermissionSelect(
        mapping, pf, request, response);
    }

    private ActionForward modeSave(ActionMapping mapping, PermissionForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    pf.setMode(null);
    Permission p = null;
    
    //check duplicates
    List<Permission> permissions = PermissionUtil.getAllPermissions();
    for(Permission perm: permissions) {
        if(pf.getId() == 0 && pf.getName().trim().equals(perm.getName().trim())) {
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.gateperm.duplicateName"));
            saveErrors(request,errors);
            pf.setMode(null);
            return mapping.getInputForward();
        }
    }
    
    Session hs = PersistenceManager.getSession();
    if (pf.getId() != 0)
        p = (Permission) hs.get(Permission.class, pf.getId());

    if (pf.getType().equals("Gate")) {
        if (p == null)
        p = new GatePermission();
        GatePermission gp = (GatePermission) p;
        gp.getActions().clear();
        if(pf.getActions()!=null && pf.getActions().length>0) gp.getActions().addAll(Arrays.asList(pf.getActions()));

        gp.setGateTypeName(pf.getGateTypeName());
        gp.getGateParameters().clear();
        Iterator i = pf.getGateParameters().iterator();
        while (i.hasNext()) {
        MetaInfo element = (MetaInfo) i.next();
        gp.getGateParameters().add((String) ((MetaInfo) element.getValue()).getValue());
        }
    } else {
        if (p == null)
        p = new CompositePermission();
        CompositePermission cp = (CompositePermission) p;
        cp.setIntersection(pf.getIntersection());
        cp.getPermissions().clear();
        for (int i = 0; i < pf.getPermissions().length; i++) {
        Permission r = (Permission) hs.get(Permission.class, pf.getPermissions()[i]);
        cp.getPermissions().add(r);
        }
    }

    p.setName(pf.getName());
    p.setDescription(pf.getDescription());

    hs.saveOrUpdate(p);

    hs.flush();

    return modeListExisting(mapping, pf, request, response);
    }

}
