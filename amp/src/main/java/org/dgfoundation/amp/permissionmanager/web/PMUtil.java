/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web;

import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMGateReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMPermContentBean;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMReadEditWrapper;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpTreeVisibilityModelBean;
import org.dgfoundation.amp.permissionmanager.components.features.tables.AmpPMAddPermFormTableFeaturePanel;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.gateperm.core.*;
import org.digijava.module.gateperm.gates.*;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.StringType;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.*;

/**
 * @author dan
 *
 */
public final class PMUtil {

     private static Logger logger = Logger.getLogger(PMUtil.class);
    
    public static final String CUMMULATIVE = "Cummulative";
    public static final String WORKSPACE_PERMISSION = "Workspace based permission";
    public static final String ROLE_PERMISSION = "Role based permission";
    public static final String WORKSPACE_MEMBER_IMG_SRC = "/TEMPLATE/ampTemplate/img_2/ico_user.gif";
    public static final String WORKSPACE_MANAGER_IMG_SRC = "/TEMPLATE/ampTemplate/img_2/ico_user_admin.gif";
    
    public static final String PERM_ROLE = "Role Based Permission";
    public static final String PERM_WORKSPACE = "Workspace Based Permission";
    public static final String PERM_NO_ACCESS = "No Access";
    public static final String PERM_FULL_ACCESS = "Full Access";
    public static final String PERM_ROLE_WORKSPACE = "Role and Workspace Based Permission";
    public static final List<String> PERM_STRATEGIES = Arrays.asList(PERM_NO_ACCESS, PERM_ROLE, PERM_WORKSPACE, PERM_ROLE_WORKSPACE,PERM_FULL_ACCESS);
    
    public static final HashMap<String,String> permissionRoles = createPermissionRoles();
    
    
    public static void setGlobalPermission(Class globalPermissionMapForPermissibleClass, Permission permission,String simpleName) {
        // TODO Auto-generated method stub
        Session hs=null;
        try {
            hs = PermissionUtil.saveGlobalPermission(globalPermissionMapForPermissibleClass, permission.getId(), simpleName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(hs!=null){
            //pf.setPermissionId(new Long(0));
        }

    }
    
    
    
    
    public static void savePermission(IModel<CompositePermission> cpModel, Set<AmpPMGateReadEditWrapper> gatesSet) throws DgException {
        // TODO Auto-generated method stub
        Session session=null;
        try {
            session = PersistenceManager.getSession();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PermissionMap permissionMap=new PermissionMap(); 
        permissionMap.setPermissibleCategory(null);
        permissionMap.setObjectIdentifier(null);
        
//      CompositePermission cp=new CompositePermission(false);
//      cp.setDescription("This permission was created using the PM UI by admin user");
//      cp.setDedicated(false);
//      cp.setName(cpModel.getObject().getName());
        
        cpModel.getObject().setDescription("This permission was created using the PM UI by admin user");
        cpModel.getObject().setDedicated(false);
        
        
        for (AmpPMGateReadEditWrapper ampPMGateWrapper : gatesSet) {
            initializeAndSaveGatePermission(session,cpModel.getObject(),ampPMGateWrapper, true, null);
        }
        
        session.save(cpModel.getObject());
        
        permissionMap.setPermission(cpModel.getObject());
        
        session.save(permissionMap);
        
//session.flush();
        
    }
    
    public static void initializeAndSaveGatePermission(Session session, CompositePermission cp, AmpPMReadEditWrapper ampPMGateWrapper, 
            boolean global, String strategyType) throws HibernateException {
        AmpPMGateReadEditWrapper ampPMGateWrapperLocal = (AmpPMGateReadEditWrapper) ampPMGateWrapper;
        initializeAndSaveGatePermission(session, cp, cp.getName()+" - "+ampPMGateWrapperLocal.getName(), 
                ampPMGateWrapperLocal.getParameter(), ampPMGateWrapperLocal.getGate(),ampPMGateWrapperLocal.getReadFlag()?"on":"off",
                        ampPMGateWrapperLocal.getEditFlag()?"on":"off", "This permission has been generated by the Permission Manager UI", 
                                global, strategyType);
    }
    
    public static void initializeAndSaveGatePermission(Session session,CompositePermission cp,String permissionName,String parameter, 
            Class gate,String readFlag, String editFlag, String description, 
            boolean global, String strategyType ) throws HibernateException {
        //LogicalGate g= new LogicalGate("WorkspacePermSelectGate(ROLE)","OrgRoleGate(DN)","AND");
        
        GatePermission baGate=new GatePermission(true);
        baGate.setName(permissionName);
        baGate.setDescription(description);
        if(global)
        {
            baGate.getGateParameters().add(parameter);
            baGate.setGateTypeName(gate.getName());
        }
        else
        {
            baGate.getGateParameters().add(StrategyPermSelectGate.class.getName()+"("+strategyType+")");
            baGate.getGateParameters().add(gate.getName()+"("+parameter+")");
            baGate.getGateParameters().add(Integer.toString(LogicalGate.OPERATOR_AND));
            baGate.setGateTypeName(LogicalGate.class.getName());
        }
        HashSet baActions=new HashSet();
        if("on".equals(editFlag)) baActions.add(GatePermConst.Actions.EDIT);
        if("on".equals(readFlag)) baActions.add(GatePermConst.Actions.VIEW);
        baGate.setActions(baActions);
        if(baGate.getActions().size()>0) { 
            session.save(baGate);
            cp.getPermissions().add(baGate);
        }
    }

    
    private static void createGatePermissionGlobal(Session session, CompositePermission cp) {
        {
            GatePermission baGate=new GatePermission(true);
            baGate.setName(cp.getName()+" Full Access");
            baGate.setDescription(cp.getDescription());
            baGate.getGateParameters().add(StrategyPermSelectGate.class.getName()+"(Full Access)");
            baGate.getGateParameters().add(BooleanGate.class.getName()+"(true)");
            baGate.getGateParameters().add(Integer.toString(LogicalGate.OPERATOR_AND));
            baGate.setGateTypeName(LogicalGate.class.getName());
            HashSet baActions=new HashSet();
             baActions.add(GatePermConst.Actions.EDIT);
             baActions.add(GatePermConst.Actions.VIEW);
            baGate.setActions(baActions);
            session.save(baGate);
            cp.getPermissions().add(baGate);
        }
    }
    
    public static void deletePermissionMap(PermissionMap permissionMap, Session session){
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
    }




    public static void deleteCompositePermission(CompositePermission cp, Session session, boolean delCompositePermission) {
        // TODO Auto-generated method stub
//session.flush();  
        Iterator<Permission> i = cp.getPermissions().iterator();
        while (i.hasNext()) {
            Permission element = (Permission) i.next();
            Object object = session.load(Permission.class, element.getId());
            session.delete(object);
        }
        if(delCompositePermission){
            Object object = session.load(Permission.class, cp.getId());
            session.delete(object);
//session.flush();      
        }
    }
    public static void deleteGatePermission(GatePermission gp, Session session, boolean delGatePermission) {
        // TODO Auto-generated method stub
        if(delGatePermission){
            Object object = session.load(Permission.class, gp.getId());
            session.delete(object);
        }
    }

    public static void assignGlobalPermission(Set<AmpPMReadEditWrapper> gatesSet, Set<AmpPMReadEditWrapper> workspaceSet,   Class globalPermissibleClass) {
        // TODO Auto-generated method stub
        Session session = PersistenceManager.getSession();
        
        PermissionMap pm = PermissionUtil.getGlobalPermissionMapForPermissibleClass(globalPermissibleClass, session);
        
        if (pm != null && session != null) {
            Permission p = pm.getPermission();
            if (p != null) {
                if (p instanceof CompositePermission) {
                    CompositePermission cp = (CompositePermission) p;
                    PMUtil.deleteCompositePermission(cp, session,true);
                }
            }
        }
        
        //remove all duplicates and old perm map
        List<PermissionMap> permMaps = getGlobalPermissionMapListForPermissibleClass(globalPermissibleClass);
        if(permMaps!=null)
            for (Iterator it = permMaps.iterator(); it.hasNext();) {
                PermissionMap pmAux = (PermissionMap) it.next();
                PMUtil.deletePermissionMap(pmAux, session);                     
            }
        
        pm=new PermissionMap(); 
        pm.setObjectIdentifier(null);
        pm.setPermissibleCategory(globalPermissibleClass.getSimpleName());

        CompositePermission cp=new CompositePermission(true);
        cp.setDescription("This permission was created using the PM UI by admin user");
        cp.setName(getFieldSimpleName(globalPermissibleClass.getSimpleName()) + " - Composite Permission");
        
        for (AmpPMReadEditWrapper ampPMGateWrapper : gatesSet) {
            initializeAndSaveGatePermission(session,cp,ampPMGateWrapper, true, null);
        }
        
        for (AmpPMReadEditWrapper ampPMGateWrapper : workspaceSet) {
            initializeAndSaveGatePermission(session,cp,ampPMGateWrapper, true, null);
        }
        
        PMUtil.createGatePermissionGlobal(session, cp);
        
        session.save(cp);
        pm.setPermission(cp);   
        session.save(pm);
        
        
        
    }
    
    
/*
    public static void assignGlobalPermission(PermissionMap pm, Set<AmpPMReadEditWrapper> gatesSet, Class globalPermissionMapForPermissibleClass) {
        Session session = null;
            try {
                session = PersistenceManager.getRequestDBSession();
            } catch (DgException e) {
                e.printStackTrace();
            }
            
        
            
        String permCategory = pm.getPermissibleCategory();
        if(pm!=null && session!=null) {
            Permission p=pm.getPermission();
            //we delete the old permissions
//          if (p!=null) {
            String name = p.getName();
//          if(cp.getId()!=null  && p.isDedicated()) 
//              {
//                  CompositePermission cp = (CompositePermission)p;
//                  PMUtil.deleteCompositePermission(cp, session,true);
//                  cp=new CompositePermission(false);
//              }
            
           // if (p!=null && p.isDedicated()) {
            if (p!=null) {
                CompositePermission cp = (CompositePermission)p;
                PMUtil.deleteCompositePermission(cp, session,true);
                List<PermissionMap> permMaps = getGlobalPermissionMapListForPermissibleClass(globalPermissionMapForPermissibleClass);
                if(permMaps!=null)
                    for (Iterator it = permMaps.iterator(); it.hasNext();) {
                        PermissionMap pmAux = (PermissionMap) it.next();
                        PMUtil.deletePermissionMap(pmAux, session);                     
                    }
                //PMUtil.deletePermissionMap(ampObjectVisibility)
            }
            
            pm=new PermissionMap(); 
            pm.setObjectIdentifier(null);
            pm.setPermissibleCategory(permCategory);

            CompositePermission cp=new CompositePermission(true);
            cp.setDescription("This permission was created using the PM UI by admin user");
            cp.setName(name);
            
            for (AmpPMReadEditWrapper ampPMGateWrapper : gatesSet) {
                initializeAndSaveGatePermission(session,cp,ampPMGateWrapper);
            }
            session.save(cp);
//session.flush();
            pm.setPermission(cp);   
            session.save(pm);
//session.flush();
        }
    }
    */
    
    public static CompositePermission createCompositePermissionForFM(String name, Set<AmpPMReadEditWrapper> gatesSet, Set<AmpPMReadEditWrapper> workspacesSet){
        Session session = PersistenceManager.getSession();
        CompositePermission cp = new CompositePermission(true);
        cp.setDescription("This permission was created using the PM UI by admin user");
        cp.setName(getFieldSimpleName(name));
        if(gatesSet!=null && gatesSet.size()>0)
            for (AmpPMReadEditWrapper ampPMGateWrapper : gatesSet)
                initializeAndSaveGatePermission(session,cp,ampPMGateWrapper,false, PMUtil.PERM_ROLE);
        if(workspacesSet!=null && workspacesSet.size()>0)
            for (AmpPMReadEditWrapper ampPMGateWrapper : workspacesSet)
                initializeAndSaveGatePermission(session,cp,ampPMGateWrapper,false, PMUtil.PERM_WORKSPACE);
        
        AmpPMGateReadEditWrapper fullAccess = new AmpPMGateReadEditWrapper(new Long(100),"Full Access", "true", BooleanGate.class, Boolean.TRUE,Boolean.TRUE);
        initializeAndSaveGatePermission(session,cp, fullAccess ,false, PMUtil.PERM_FULL_ACCESS);
        
        AmpPMGateReadEditWrapper noAccess = new AmpPMGateReadEditWrapper(new Long(101),"No Access", "false", BooleanGate.class, Boolean.FALSE,Boolean.TRUE);
        initializeAndSaveGatePermission(session,cp, noAccess ,false, PMUtil.PERM_NO_ACCESS);

//
        return cp;
    }
    
    
    public static AmpTreeVisibilityModelBean getAmpTreeFMPermissions() {
        return generateAmpTreeFMPermissions(FeaturesUtil.getDefaultAmpTemplateVisibility());
    }
    
    public static AmpTreeVisibilityModelBean generateAmpTreeFMPermissions(AmpTemplatesVisibility currentTemplate) {
        AmpObjectVisibility ampObjRoot = currentTemplate;
        for (Iterator it = currentTemplate.getAllItems().iterator(); it.hasNext();) {
            AmpObjectVisibility o = (AmpObjectVisibility) it.next();
            if("/Activity Form".compareTo(o.getName()) ==0 )
                {
                    ampObjRoot = o; break;
                }
        } 
        AmpTreeVisibilityModelBean tree = new AmpTreeVisibilityModelBean(ampObjRoot.getName(), buildAmpSubTreeFMPermission(ampObjRoot), ampObjRoot);
//      if (ampObjRoot.getAllItems() != null && ampObjRoot.getAllItems().iterator() != null)
//              for (Iterator it = ampObjRoot.getSortedAlphaAllItems().iterator(); it.hasNext();) {
//                  AmpModulesVisibility module = (AmpModulesVisibility) it.next();
//                  if(module.getParent()==null) 
//                      {
//                          tree.getItems().add(new AmpTreeVisibilityModelBean(module.getName(),buildAmpSubTreeFMPermission(module), module));
//                      }
//              }
        //tree.getItems().add(new AmpTreeVisibilityModelBean(ampObjRoot.getName(),buildAmpSubTreeFMPermission(ampObjRoot), ampObjRoot));
        return tree;
    }
    
    public static AmpTreeVisibilityModelBean buildTreeObjectFMPermissions(AmpObjectVisibility currentAOV) {
        AmpTreeVisibilityModelBean tree = new AmpTreeVisibilityModelBean(currentAOV.getName(), new ArrayList<Object>(), currentAOV);
        Set itemsSet = null;
        if(currentAOV instanceof AmpModulesVisibility && ((AmpModulesVisibility) currentAOV).getSortedAlphaSubModules().size() > 0)
            itemsSet = ((AmpModulesVisibility) currentAOV).getSortedAlphaSubModules();
        else itemsSet = currentAOV.getSortedAlphaItems();
        if (itemsSet != null && itemsSet.iterator() != null)
                for (Iterator it = itemsSet.iterator(); it.hasNext();) {
                    AmpObjectVisibility item = (AmpObjectVisibility) it.next();
                    AmpTreeVisibilityModelBean iitem = new AmpTreeVisibilityModelBean(item.getName(),buildAmpSubTreeFMPermission(item),item);
                    tree.getItems().add(iitem);
                }
        return tree;
    }
    
    public static List<Object> buildAmpSubTreeFMPermission(AmpObjectVisibility aov){
        List<Object> list = new ArrayList<Object>();
        Set itemsSet=null;
        if(aov instanceof AmpModulesVisibility && ((AmpModulesVisibility) aov).getSortedAlphaSubModules().size()>0)
            itemsSet = ((AmpModulesVisibility) aov).getSortedAlphaSubModules();
        else itemsSet = aov.getSortedAlphaItems();
        if(itemsSet!=null)
            for (Iterator it = itemsSet.iterator(); it.hasNext();) {
                AmpObjectVisibility item = (AmpObjectVisibility) it.next();
                AmpTreeVisibilityModelBean iitem = new AmpTreeVisibilityModelBean(item.getName(),buildAmpSubTreeFMPermission(item), item);
                list.add(iitem);
            }
        return list;
    }

    
    public static TreeModel createTreeModel(IModel<AmpTreeVisibilityModelBean> treeModel)
    {
        AmpTreeVisibilityModelBean tree = treeModel.getObject();
        return convertToTreeModel(tree,tree.getItems());
    }

    public static TreeModel convertToTreeModel(AmpTreeVisibilityModelBean tree, List<Object> list)
    {
        TreeModel model = null;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(tree.getName(),list, tree.getAmpObjectVisibility()));
        add(rootNode, list);
        model = new DefaultTreeModel(rootNode);
        return model;
    }

    public static void add(DefaultMutableTreeNode parent, List<Object> sub)
    {
        for (Iterator<Object> i = sub.iterator(); i.hasNext();)
        {
            AmpTreeVisibilityModelBean o = (AmpTreeVisibilityModelBean)i.next();
            if(o.getItems().size()>0){
              DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.getName(),o.getItems(), o.getAmpObjectVisibility()));
              parent.add(child);
              add(child, (List<Object>)o.getItems());
            }
            else{
              DefaultMutableTreeNode child = new DefaultMutableTreeNode(new AmpTreeVisibilityModelBean(o.toString(), o.getAmpObjectVisibility()));
              parent.add(child);
            }
        }
    }
    
    public static void generateGatesList(Object o, Set<AmpPMReadEditWrapper> gatesSet){
        if(!(o instanceof CompositePermission)) return;
        CompositePermission cp = (CompositePermission)o;
        if(cp==null || cp.getId() == null) 
            {
                generateDefaultGatesList(gatesSet);
                return;
            }
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, hasView(cp.getPermissions(),"BA"),hasEdit(cp.getPermissions(),"BA")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, hasView(cp.getPermissions(),"CA"),hasEdit(cp.getPermissions(),"CA")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, hasView(cp.getPermissions(),"EA"),hasEdit(cp.getPermissions(),"EA")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, hasView(cp.getPermissions(),"IA"),hasEdit(cp.getPermissions(),"IA")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, hasView(cp.getPermissions(),"DN"),hasEdit(cp.getPermissions(),"DN")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, hasView(cp.getPermissions(),"SG"),hasEdit(cp.getPermissions(),"SG")));      
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, hasView(cp.getPermissions(),"RG"),hasEdit(cp.getPermissions(),"RG")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, hasView(cp.getPermissions(),"RO"),hasEdit(cp.getPermissions(),"RO")));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_EVERYONE),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_EVERYONE)));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_GUEST),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_GUEST)));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, hasView(cp.getPermissions(),UserLevelGate.PARAM_OWNER),hasEdit(cp.getPermissions(),UserLevelGate.PARAM_OWNER)));
    }
    
    public static Boolean hasEdit(Set<Permission> permissions, String param) {
        for (Permission p : permissions) {
            {
                GatePermission ap = (GatePermission)p;
                if(ap.hasParameter(param)) return hasEdit(ap);
            }
        }
        return false;
    }

    public static Boolean hasView(Set<Permission> permissions, String param) {
        for (Permission p : permissions) {
            {
                GatePermission ap = (GatePermission)p;
                if(ap.hasParameter(param)) return hasView(ap);
            }
        }
        return false;
    }

    public static boolean hasEdit(GatePermission agencyPerm) {
        return agencyPerm.hasAction(GatePermConst.Actions.EDIT);
    }

    public static boolean hasView(GatePermission agencyPerm) {
        return agencyPerm.hasAction(GatePermConst.Actions.VIEW);
    }
    
    public static void generateDefaultGatesList(Set<AmpPMReadEditWrapper> gatesSet){
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(1),"Everyone", UserLevelGate.PARAM_EVERYONE, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(2),"Guest", UserLevelGate.PARAM_GUEST, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(3),"Owner", UserLevelGate.PARAM_OWNER, UserLevelGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(4),"Beneficiary Agency","BA",OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(5),"Contracting Agency", "CA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(6),"Executing Agency", "EA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(7),"Funding Agency", "DN", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(8),"Implementing Agency", "IA", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(9),"Responsible Agency", "RO", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(10),"Regional Group", "RG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
        gatesSet.add(new AmpPMGateReadEditWrapper(new Long(11),"Sector Group", "SG", OrgRoleGate.class, Boolean.FALSE,Boolean.FALSE));
    }
    
    public static HashMap<String, String> createPermissionRoles(){
        HashMap<String,String> result = new HashMap<String,String>();
        result.put(UserLevelGate.PARAM_EVERYONE, "Everyone");
        result.put(UserLevelGate.PARAM_GUEST, "Guest");
        result.put(UserLevelGate.PARAM_OWNER, "Owner");
        result.put("BA","Beneficiary Agency");
        result.put("CA","Contracting Agency");
        result.put("EA","Executing Agency");
        result.put("FA","Funding Agency");
        result.put("IA","Implementing Agency");
        result.put("RA","Responsible Agency");
        result.put("RG","Regional Group");
        result.put("SG","Sector Group");
        
        return result;
    }
    
    public static PermissionMap createPermissionMap(Class globalPermissionMapForPermissibleClass, boolean newCompPerm) {
        PermissionMap pmAux;
        pmAux = new PermissionMap();
        pmAux.setPermissibleCategory(globalPermissionMapForPermissibleClass.getSimpleName());
        pmAux.setObjectIdentifier(null);
        if(newCompPerm)
            pmAux.setPermission(createCompositePermission(globalPermissionMapForPermissibleClass.getSimpleName() + " - Composite Permission",
                "This permission was created using the PM UI by admin user",false));
        return pmAux;
    }
    
    public static String getFieldSimpleName(String name) {
        if(name.contains("/"))
            return name.substring(name.lastIndexOf("/")+1, name.length());
        return name;
    }
    
    
    public static CompositePermission createCompositePermission(String name, String description, boolean dedicated){
        CompositePermission cp=new CompositePermission(dedicated);
        cp.setDescription(description);
        cp.setName(getFieldSimpleName(name));
        return cp;
    }

    public static void generateWorkspacesList(IModel<Set<AmpTeam>> teamsModel, TreeSet<AmpPMReadEditWrapper> workspacesSet) {
        Set<AmpTeam> ampTeamSet = teamsModel.getObject();
        if(ampTeamSet!=null){
            int i=1;
            for (AmpTeam ampTeam : ampTeamSet) {
                workspacesSet.add(new AmpPMGateReadEditWrapper(new Long(i),ampTeam.getName(),ampTeam.getAmpTeamId().toString(),WorkspaceGate.class, Boolean.FALSE,Boolean.FALSE));
                i++;
            }
        }
    }


    public static List<String> getPermissionPriority() {
            List<String> permissionPriority = new ArrayList<String>();
            permissionPriority.add(PMUtil.ROLE_PERMISSION);
            permissionPriority.add(PMUtil.WORKSPACE_PERMISSION);
            permissionPriority.add(PMUtil.CUMMULATIVE);
            return permissionPriority;
    }

    public static void savePermissionMap( Session session, AmpTreeVisibilityModelBean ampTreeRootObject, CompositePermission cp) {
            PermissionMap permissionMap = new PermissionMap(); 
            permissionMap.setPermissibleCategory(ampTreeRootObject.getAmpObjectVisibility().getPermissibleCategory().getSimpleName());
            permissionMap.setObjectIdentifier(ampTreeRootObject.getAmpObjectVisibility().getId());
//          Calendar cal = Calendar.getInstance();
//          CompositePermission cp1 = PMUtil.createCompositePermissionForFM(ampTreeRootObject.getAmpObjectVisibility().getName()+" Composite Permission " + cal.getTimeInMillis(), gatesSet, workspacesSet);
            session.save(cp);
            permissionMap.setPermission(cp);
            
            //check if a permission map for this object and category already exists
            List <PermissionMap> auxList = getOwnPermissionMapListForPermissible(ampTreeRootObject.getAmpObjectVisibility());
            if (auxList !=null) {
              PermissionMap aux = auxList.get(0);
              session.delete(aux);
            }
            session.save(permissionMap);
        
    }
    
    
    public static List<PermissionMap> getOwnPermissionMapListForPermissible(Permissible obj) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
                + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier=:objectId ORDER BY p.objectIdentifier");
            query.setParameter("objectId", obj.getIdentifier());
            query.setParameter("categoryName", obj.getPermissibleCategory().getSimpleName());
            List<PermissionMap> col = query.list();
            
            if (col.size() == 0) return null;

            return col;
        
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("HibernateException Exception encountered", e);
        } finally {
            try {
            //PersistenceManager.releaseSession(session);
            } catch (HibernateException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException( "HibernateException Exception encountered", e);

            }
        }
    }
  
    public static void assignFieldsPermission(IModel<TreeModel> iTreeModel, IModel<Set<AmpPMReadEditWrapper>> gatesSetModel, IModel<Set<AmpPMReadEditWrapper>> workspacesSetModel) {
        
            Session session = PersistenceManager.getSession();
            
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)iTreeModel.getObject().getRoot();
            AmpTreeVisibilityModelBean ampTreeRootObject = (AmpTreeVisibilityModelBean)root.getUserObject();
//          List<PermissionMap> pmList = PMUtil.getOwnPermissionMapListForPermissible(ampTreeRootObject.getAmpObjectVisibility());
            
            Calendar cal = Calendar.getInstance();
            CompositePermission cp = PMUtil.createCompositePermissionForFM(ampTreeRootObject.getAmpObjectVisibility().getName()+" Composite Permission " + cal.getTimeInMillis(), gatesSetModel!=null?gatesSetModel.getObject():null, workspacesSetModel!=null?workspacesSetModel.getObject():null);
            session.save(cp);
            PMUtil.saveFieldsPermission(session, root, cp);
            
    }


    private static void saveFieldsPermission(Session session, DefaultMutableTreeNode root, CompositePermission cp) {
        AmpTreeVisibilityModelBean ampTreeRootObject = (AmpTreeVisibilityModelBean)root.getUserObject();
        if(ampTreeRootObject.getChecked())
            {
                PMUtil.deletePermissionMap(ampTreeRootObject.getAmpObjectVisibility());
                PMUtil.savePermissionMap(session, ampTreeRootObject,cp);
            }
        Enumeration children = root.children();
        while ( children.hasMoreElements() ) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
            AmpTreeVisibilityModelBean userObject = (AmpTreeVisibilityModelBean)child.getUserObject();
            saveFieldsPermission(session, child, cp);
        }
        return ;
    }




    private static void deletePermissionMap(AmpObjectVisibility ampObjectVisibility) {
        // TODO Auto-generated method stub
        Session session = PersistenceManager.getSession();
        List<PermissionMap> pmList = PMUtil.getOwnPermissionMapListForPermissible(ampObjectVisibility);
        if(pmList!=null)
        for (PermissionMap permissionMap : pmList) {
            if(permissionMap!=null) {
                Permission p=permissionMap.getPermission();
                //we delete the old permissions, if they are dedicated
                List<PermissionMap> pMapList=null;
                try {
                    pMapList = PermissionUtil.getAllPermissionMapsForPermission(p.getId());
                } catch (HibernateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (DgException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (p!=null && p.isDedicated() && pMapList!=null && pMapList.size() == 1) {
                CompositePermission cp = (CompositePermission)p;
                PMUtil.deleteCompositePermission(cp, session,true);
                }
                else if (p!=null && p.isDedicated() && pMapList!=null){
                    //PMUtil.deletePermissionMap(permissionMap, session);
                    p.getPermissibleObjects().remove(permissionMap);
                    session.saveOrUpdate(p);
                    permissionMap = (PermissionMap) session.load(PermissionMap.class, permissionMap.getId());
                    session.delete(permissionMap);
                }
            }
        }
        
    }
    
    public static List<PermissionMap> getGlobalPermissionMapListForPermissibleClass(Class permClass) {
        Session session = null;
          try {
            session = PersistenceManager.getSession();
            Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
                + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is null");
            query.setParameter("categoryName", permClass.getSimpleName());
            List col = query.list();
//          if(col.size()==0) return null;
//          PermissionMap pm= (PermissionMap) col.get(0);     
            return col;
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException( "HibernateException Exception encountered", e);
        }
        }

    
    
    public static void addOrganizationsToUser(User user, Set<AmpOrganisation> orgsSet) {
        TreeSet<AmpOrganisation> orgs = new TreeSet<AmpOrganisation>();
        orgs.addAll(orgsSet);
        orgs.addAll(user.getAssignedOrgs());
        user.setAssignedOrgs(orgs);
    }

    public static boolean addOrganizationToUser(User user, AmpOrganisation org) {
        TreeSet<AmpOrganisation> orgs = new TreeSet<AmpOrganisation>();
        orgs.addAll(user.getAssignedOrgs());
        boolean existOrg = orgs.add(org);
        user.setAssignedOrgs(orgs);
        return existOrg;
    }

    
    public static void setPermissionPriorityVisibility(final IModel<String> permissionChoiceModel,final AmpPMAddPermFormTableFeaturePanel permGatesFieldsFormTable,final AmpPMAddPermFormTableFeaturePanel permWorkspacesFieldsFormTable) {
        if(PMUtil.ROLE_PERMISSION.compareTo(permissionChoiceModel.getObject()) == 0){
              permWorkspacesFieldsFormTable.setEnabled(false);
              permGatesFieldsFormTable.setEnabled(true);
          }
          if(PMUtil.WORKSPACE_PERMISSION.compareTo(permissionChoiceModel.getObject()) == 0){
              permWorkspacesFieldsFormTable.setEnabled(true);
              permGatesFieldsFormTable.setEnabled(false);
          }
          if(PMUtil.CUMMULATIVE.compareTo(permissionChoiceModel.getObject()) == 0){
              permWorkspacesFieldsFormTable.setEnabled(true);
              permGatesFieldsFormTable.setEnabled(true);
          }
    }


    public static String generatePermInfo(AmpObjectVisibility ampObjectVisibility) {
        // TODO Auto-generated method stub
        String  result  = "";
        List<PermissionMap> pmList = PMUtil.getOwnPermissionMapListForPermissible(ampObjectVisibility);
        if(pmList!=null)
        {
            for (PermissionMap permissionMap : pmList) {
                if(permissionMap!=null) {
                    Permission p=permissionMap.getPermission();
                    result+="Assigned FIELD Permission : "+p.getName()+";\n";
                    result = getContentInfoFieldPermission(result, p);
                }
            }
        }
        else {
            Permission p = PermissionUtil.getGlobalPermissionForPermissibleClass(AmpModulesVisibility.class);
            if(p!=null){
                String name = p.getName()!=null?p.getName():"";
                result+="GLOBAL Permission : "+name+";\n";
                result = getContentInfoGlobalPermission(result, p);
            }
            else result+="No GLOBAL Permission";
        }
        return result; 
    }




    private static String getContentInfoGlobalPermission(String result, Permission p) {
        if (p!=null && p.isDedicated()) {
            CompositePermission cp = (CompositePermission)p;
            //result+=" - cp:"+cp.getName();
            String userInfo      = "";
            String workspaceInfo = "";
            String orgRoleInfo   = "";
            //PMUtil.PERM_ROLE , PMUtil.PERM_WORKSPACE
            for (Iterator<Permission> it = cp.getPermissions().iterator(); it.hasNext();) {
                GatePermission pGate = (GatePermission) it.next();
                if(UserLevelGate.class.getName().compareTo(pGate.getGateTypeName()) == 0)
                    userInfo        +=  buildInfoGate(pGate,1);
                if(OrgRoleGate.class.getName().compareTo(pGate.getGateTypeName()) == 0)
                    orgRoleInfo     +=  buildInfoGate(pGate,2);
                if(WorkspaceGate.class.getName().compareTo(pGate.getGateTypeName()) == 0)
                    workspaceInfo   +=  buildInfoGate(pGate,3);

            }
            result +=userInfo +orgRoleInfo+ workspaceInfo;
        }
        return result;
    }

    public static String getContentInfoFieldPermission(String result, Permission p) {
        if (p!=null && p.isDedicated()) {
            CompositePermission cp = (CompositePermission)p;
            //result+=" - cp:"+cp.getName();
            String userInfo      = "";
            String workspaceInfo = "";
            for (Iterator<Permission> it = cp.getPermissions().iterator(); it.hasNext();) {
                
                Permission pp = (Permission) it.next();
                if(!(pp instanceof GatePermission)) continue;
                
                GatePermission pGate = (GatePermission) pp;
                
                if(pGate.hasParameter(StrategyPermSelectGate.class.getName()+"("+PMUtil.PERM_ROLE+")"))
                    userInfo+=buildInfoLogicalGate(pGate);
                if(pGate.hasParameter(StrategyPermSelectGate.class.getName()+"("+PMUtil.PERM_WORKSPACE+")"))
                    workspaceInfo+=buildInfoLogicalGate(pGate);
                
            }
            if(result == null) result = "";
            result +=userInfo + workspaceInfo;
        }
        return result;
    }
    
    
    public static Set<AmpPMPermContentBean> getContentInfoFieldPermission(Permission p, StringBuilder strategy) {
        Set<AmpPMPermContentBean> result    = new TreeSet<AmpPMPermContentBean>();
        if (p!=null && p.isDedicated()) {
            CompositePermission cp = (CompositePermission)p;
            //result+=" - cp:"+cp.getName();
//          Set<AmpPMPermContentBean> rolePerms = new TreeSet<AmpPMPermContentBean>();
//          Set<AmpPMPermContentBean> wrkPerms  = new TreeSet<AmpPMPermContentBean>();
            boolean permRole    = false;
            boolean wrkRole     = false;
            for (Iterator<Permission> it = cp.getPermissions().iterator(); it.hasNext();) {
                
                Permission pp = (Permission) it.next();
                if(!(pp instanceof GatePermission)) continue;
                
                GatePermission pGate=(GatePermission) pp;
                
                if(pGate.hasParameter(StrategyPermSelectGate.class.getName()+"("+PMUtil.PERM_ROLE+")"))
                    {
                        result.add(buildPermContentBean(pGate));
                        permRole = true;
                    }
                if(pGate.hasParameter(StrategyPermSelectGate.class.getName()+"("+PMUtil.PERM_WORKSPACE+")"))
                    {
                        result.add(buildPermContentBean(pGate));
                        wrkRole = true;
                    }
                
            }
            
            if(permRole && wrkRole)
                strategy.append(PMUtil.PERM_ROLE_WORKSPACE);
            else if(permRole && !wrkRole)
                    strategy.append(PMUtil.PERM_ROLE);
                else strategy.append(PMUtil.PERM_WORKSPACE);
//          result.addAll(rolePerms);
//          result.addAll(wrkPerms);
        }
        return result;
    }


    
    private static AmpPMPermContentBean buildPermContentBean(GatePermission pGate) {
        // TODO Auto-generated method stub
        
        String label = pGate.getName().substring(pGate.getName().lastIndexOf('-')+2, pGate.getName().length());
        Boolean view = false;
        Boolean edit = false;
        
        if(pGate.getActions().contains(GatePermConst.Actions.EDIT))
            edit = true;
        
        if(pGate.getActions().contains(GatePermConst.Actions.VIEW))
            view = true;
        
        AmpPMPermContentBean cb = new AmpPMPermContentBean(label,view,edit);
        
        return cb;
    }




    private static String buildInfoLogicalGate(GatePermission pGate){
        String info = "";
        
        info+=pGate.getName().substring(pGate.getName().lastIndexOf('-')+2, pGate.getName().length());
        
        info+=": "+listToString(pGate.getActions(),",")+";\n";
        
        return info;
    }
    


    private static String buildInfoGate(GatePermission pGate, int type) {
        String info = "";
        //type = 1 - UserLevelGate
        //type = 2 - OrgRoleGate
        //type = 3 - WorkspaceGate
        for (Iterator<String> iterator = pGate.getGateParameters().iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            if(type == 3)
                {
                    AmpTeam team = TeamUtil.getAmpTeam(Long.parseLong(s));
                    s = team.getName();
                }
            else if (type == 2)
                    s = permissionRoles.get(s);
            info+=s+": "+listToString(pGate.getActions(),",")+";\n";
        }
        return info; 
    }
    
    private static String listToString(Set<String> s, String sep){
        String result = "";
        for (Iterator iterator = s.iterator(); iterator.hasNext();) {
            String action = (String) iterator.next();
            if(!iterator.hasNext())
                result+=action;
            else
                result+=action+sep;
        }
        return result;
    }




    public static void updateAmpObj(Object object) {
        logger.debug("In add " + object.getClass().getName());
        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.saveOrUpdate(object);
            ////tx.commit();
        } catch (Exception e) {
            logger.error("Unable to update");
            e.printStackTrace(System.out);
        }
        return ;
        
    }
    
    public static List<GatePermission> getPermissionsByTeam (String teamId) {
        Session dbSession           = null;
        List<GatePermission> returnCollection   = null;
        try {
            dbSession= PersistenceManager.getRequestDBSession();
            String queryString = "select v from "
                + GatePermission.class.getName()
                + " v join v.gateParameters as param where param=:teamId";
            Query qry           = dbSession.createQuery(queryString);
            qry.setParameter("teamId", teamId, StringType.INSTANCE);
            returnCollection    = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to getPermissionByTeam: " + ex.getMessage());
        } 
        return returnCollection;
    }
    
}
