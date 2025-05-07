/**
 * ManagePermissionMap.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.digijava.module.gateperm.form.PermissionMapForm;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * ManagePermissionMap.java
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.action
 * @since 05.09.2007
 */
public class ManagePermissionMap extends MultiAction {

    /**
         * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping,
         *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
         *      javax.servlet.http.HttpServletResponse)
         */
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

    PermissionMapForm pf = (PermissionMapForm) form;

    if (pf.getPermissionMaps() == null)
        pf.clear();

    List<Permission> allPermissions = PermissionUtil.getAllUnDedicatedPermissions();
    pf.set_availablePermissions(allPermissions);

    List<Class> availablePermissibleCategories = Arrays.asList(GatePermConst.availablePermissibles);
    Map<String, Class> permissibleCategoriesMap = new HashMap<String, Class>();
    Iterator i = availablePermissibleCategories.iterator();
    while (i.hasNext()) {
        Class element = (Class) i.next();
        permissibleCategoriesMap.put(element.getSimpleName(), element);
    }
    pf.set_permissibleCategoriesMap(permissibleCategoriesMap);
    pf.set_availablePermissibleCategories(availablePermissibleCategories);

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
    PermissionMapForm pf = (PermissionMapForm) form;
    String mode = pf.getMode();
    pf.setMode(null);

    if (request.getParameter("reset") != null)
        return modeReset(mapping, pf, request, response);

    if ("saveDetailed".equals(mode))
        return modeSaveDetailed(mapping, pf, request, response);
    if ("saveGlobal".equals(mode))
        return modeSaveGlobalPermission(mapping, pf, request, response);

    if ("permissiblePicked".equals(mode))
        return modePermissiblePicked(mapping, pf, request, response);
    if ("permissibleCategoryPicked".equals(mode))
        return modePermissibleCategoryPicked(mapping, pf, request, response);

    return mapping.getInputForward();
    }

    private ActionForward modeSaveGlobalPermission(ActionMapping mapping, PermissionMapForm pf,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
    Class permCatClass = pf.get_permissibleCategoriesMap().get(pf.getPermissibleCategory());
    if(permCatClass==null)  return mapping.getInputForward(); 
    PermissionUtil.cleanGlobalPermissionMapForPermissibleClass(permCatClass);
    Session hs= PermissionUtil.saveGlobalPermission(permCatClass,pf.getPermissionId(), pf.getPermissibleCategory());
    if(hs!=null){
        pf.setPermissionId(new Long(0));
    }
    return mapping.getInputForward();
    }

    private ActionForward modeSaveDetailed(ActionMapping mapping, PermissionMapForm pf, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    Session hs = PersistenceManager.getRequestDBSession();
    Iterator i = pf.getPermissionMaps().iterator();
    while (i.hasNext()) {
        PermissionMap element = (PermissionMap) i.next();
        if(element.getId()!=null) {
            Long elementId=element.getId();
            Long permissionId = element.getPermissionId();
            element=(PermissionMap) hs.get(PermissionMap.class, elementId);
            element.setPermissionId(permissionId);
        }
        
        // we ignore unpersisted unselected permission maps
        if (element.getId() == null && element.getPermissionId() == 0)
        continue;
//beginTransaction();
        // we delete previously persisted but unselected permissions
        if (element.getId() != null && element.getPermissionId() == 0) {
        hs.delete(element);
        //transaction.commit();
        continue;
        }
        // we save/update anything else
        Permission p = (Permission) hs.get(Permission.class, element.getPermissionId());
        element.setPermission(p);
        hs.saveOrUpdate(element);
        //transaction.commit();
    }
    hs.flush();
    
    return modePermissibleCategoryPicked(mapping, pf, request, response);   
    }

    private ActionForward modeReset(ActionMapping mapping, PermissionMapForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    form.clear();

    return mapping.getInputForward();
    }

    /**
         * a permissible category has been selected. we can now load all the permissible objects for that category and
         * display them in a dropdown
         * 
         * @param mapping
         * @param form
         * @param request
         * @param response
         * @return the form
         * @throws Exception
         */
    private ActionForward modePermissibleCategoryPicked(ActionMapping mapping, PermissionMapForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

    if (form.getPermissibleCategory().equals("select"))
        return modeReset(mapping, form, request, response);
    
    //load the global permission value:
    Class permCatClass = form.get_permissibleCategoriesMap().get(form.getPermissibleCategory());
    
    Permission globalPermissionForPermissibleClass = PermissionUtil
    .getGlobalPermissionForPermissibleClass(permCatClass);
    if(globalPermissionForPermissibleClass!=null) form.setPermissionId(globalPermissionForPermissibleClass.getId());
    

    Session hs = PersistenceManager.getRequestDBSession();
    Map<Long, String> objectLabels = PermissionUtil.getAllPermissibleObjectLabelsForPermissibleClass(permCatClass);

    Map<Long, PermissionMap> permissionMapsForPermissibleClass = PermissionUtil
        .getAllPermissionMapsForPermissibleClass(permCatClass);

    // iterate all permissibles and set the label for the mappings
    Iterator<Long> i = objectLabels.keySet().iterator();
    while (i.hasNext()) {
        Long elementId = (Long) i.next();
        PermissionMap pm = permissionMapsForPermissibleClass.get(elementId);
        if (pm != null) {
        pm.setObjectLabel(objectLabels.get(elementId));
        if (pm.getPermission() != null)
            pm.setPermissionId(pm.getPermission().getId());
        } else {
        // no previous mapping found, create a new permissionmap for this permission. if the user sets a
                // permission in the interface
        // this will be persisted, otherwise it will be discarded
        PermissionMap newpm = new PermissionMap();
        newpm.setPermissibleCategory(permCatClass.getSimpleName());
        newpm.setObjectIdentifier(elementId);
        newpm.setObjectLabel(objectLabels.get(elementId));
        permissionMapsForPermissibleClass.put(newpm.getObjectIdentifier(), newpm);
        }
    }

    // we put all the permission maps in the form list
    form.getPermissionMaps().clear();
    HashSet<PermissionMap> ts = new HashSet<PermissionMap>(permissionMapsForPermissibleClass.values());
    form.getPermissionMaps().addAll(ts);


    return mapping.getInputForward();
    }

    private ActionForward modePermissiblePicked(ActionMapping mapping, PermissionMapForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

    return mapping.getInputForward();
    }

}
