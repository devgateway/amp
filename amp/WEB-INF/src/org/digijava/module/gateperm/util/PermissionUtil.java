/**
 * PermissionUtil.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.util;


import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.permissionmanager.components.features.models.AmpPMFieldPermissionViewer;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.gateperm.core.ClusterIdentifiable;
import org.digijava.module.gateperm.core.CompositePermission;
import org.digijava.module.gateperm.core.Gate;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * PermissionUtil.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.util
 * @since 05.09.2007
 */
public final class PermissionUtil {
    private static Logger logger = Logger.getLogger(PermissionUtil.class);

    
    private static final String gateDefLocation="/classes/org/digijava/module/gateperm/gates";
    private static final String gateDefPackage="org.digijava.module.gateperm.gates";

    /**
     * gets the gate permissions scope. The scope is the place to put external objects needed by gates logical evaluation 
     * (like the current user) which are not the permissible istelf (so objects other than the current object on which the
     * permission query is invoked on)
     * @param session
     * @return
     */
    public static Map getScope(HttpSession session) {
        Map scope = (Map) session.getAttribute(GatePermConst.SCOPE);
        if(scope==null) return resetScope(session);
        return scope;
    }
    
    public static String removeTabs(String text) {
        return text.replace("\t", "");
    }
    
    public static String removeTabsNewlines(String text) {
        return removeTabs(text).replace("\r\n", " ").replace("\n", " ");
    }
    /**
     * flushes the gate permissions scope
     * @param sessiont
     * @return
     */
    public static Map resetScope(HttpSession session) {
        Map scope = (Map) session.getAttribute(GatePermConst.SCOPE);
        if (scope == null) {
            scope = new HashMap();
            session.setAttribute(GatePermConst.SCOPE, scope);
        } else {
            scope.clear();
        }
        return scope;
    }   
    /**
     * flushes the gate permissions scope
     * @param request
     * @return
     */
    public static Map resetScope(HttpServletRequest request) {
        Map scope = (Map) request.getAttribute(GatePermConst.SCOPE);
        if (scope == null) {
            scope = new HashMap();
            request.setAttribute(GatePermConst.SCOPE, scope);
        } else {
            scope.clear();
        }
        return scope;
    }
    
    
    /**
     * Deletes a permission and removes any references to objects associated with it
     * @param id the id of the permission to be removed
     * @throws HibernateException
     * @throws SQLException
     * @throws DgException
     */
    public static void deletePermission(Long id) throws HibernateException, SQLException, DgException{
        Session hs = PersistenceManager.getRequestDBSession();
        Permission p = (Permission) hs.get(Permission.class, id);
        
        Set<CompositePermission> compositeLinkedPermissions = p.getCompositeLinkedPermissions();
        Iterator<CompositePermission> i=compositeLinkedPermissions.iterator();
        while (i.hasNext()) {
            CompositePermission element = (CompositePermission) i.next();
            element.getPermissions().remove(p);
            i.remove();
            hs.saveOrUpdate(element);
         }
        
        
        Set<PermissionMap> permissibleObjects = p.getPermissibleObjects();
        Iterator<PermissionMap> ii=permissibleObjects.iterator();
        while (ii.hasNext()) {
            PermissionMap permissionMap = (PermissionMap) ii.next();
            p.getPermissibleObjects().remove(permissionMap);
            hs.saveOrUpdate(p);
        }
        hs.delete(p);
    }
    
    public static boolean arrayContains(Object[] a,Object o) {
        for (int i = 0; i < a.length; i++) {
            if(o.equals(a[i])) return true;
        }
        return false;
    }
    
    /**
     * Puts an objects in the permission scope. This will be later used by a gate to evaluate if an action is allowed or not
     * @param session the http session (where the scope lies)
     * @param key the scope key that can be used to get the object back
     * @param value the object
     */
    public static void putInScope(HttpSession session ,MetaInfo key, Object value) {
        Map scope=getScope(session);
        scope.put(key, value);
        if (value!=null){
            logger.debug("Object ["+key+"] with value ["+value.toString()+"] has been placed in the permission scope");
        }
    }
    
    
    public static void putInScope(HttpServletRequest request, MetaInfo key, Object value) {
        Map scope = getScope(request);
        scope.put(key, value);
        if (value != null) {
            logger.debug("Object [" + key + "] with value [" + value.toString()
                    + "] has been placed in the permission scope");
        }

    }
    
    /**
     * gets the gate permissions scope. The scope is the place to put external
     * objects needed by gates logical evaluation (like the current user) which
     * are not the permissible istelf (so objects other than the current object
     * on which the permission query is invoked on)
     * 
     * @param session
     * @return
     */
    public static Map getScope(HttpServletRequest request) {
        Map scope = (Map) request.getAttribute(GatePermConst.SCOPE);
        if (scope == null) {
            return resetScope(request);
        }
        return scope;
    }

    /**
     * Gets the object associated with the given key, if any, from the permissions scope
     * @param session
     * @param key the key used to put the object in the scope with putInScope
     * @return the object
     * @see PermissionUtil#putInScope(HttpSession, MetaInfo, Object)
     */
    public static Object getFromScope(HttpSession session, MetaInfo key) {
        Map scope = getScope(session);
        return scope.get(key);
    }
    
    /**
     * Removes the object associated with the given key, if any, from the permissions scope
     * @param session
     * @param key the key used to put the object in the scope with putInScope
     * @see PermissionUtil#putInScope(HttpSession, MetaInfo, Object)
     */
    public static void removeFromScope(HttpSession session,MetaInfo key) {
        Map scope=getScope(session);
        scope.remove(key);      
    }
    
    
    /**
     * list here all the available gates in the system. Since subclass search
     * through reflection is not supported by Java, we need a list with them
     * All Gates must extend the Gate class
     */
    public static synchronized Class[] getAvailableGates(ServletContext sc) {
        String realPath = sc.getRealPath("/WEB-INF/");
        if(GatePermConst.availableGatesSingleton!=null) return GatePermConst.availableGatesSingleton;
        
        File dir = new File(realPath+gateDefLocation);
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".class");
            }
        };

        ArrayList<Class> gateFiles=new ArrayList<Class>();
        if(!dir.isDirectory()) throw new RuntimeException("Gate definition path is invalid! Should be a directory:"+dir.getAbsolutePath());
        File[] files = dir.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            String className = files[i].getName().substring(0, files[i].getName().length()-6);
            try {
                Class c=Class.forName(gateDefPackage+"."+className);
                if(Gate.class.isAssignableFrom(c)) gateFiles.add(c);                
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("ClassNotFundingException occured "+e);
            }
        }
        GatePermConst.availableGatesSingleton=gateFiles.toArray(new Class[0]);
        GatePermConst.availableGatesBySimpleNames=new Hashtable<String, Class>();
        for (Class c : gateFiles) 
            GatePermConst.availableGatesBySimpleNames.put(c.getSimpleName(), c);
        
        return GatePermConst.availableGatesSingleton;
    }
    
    
    
    
 
    public static List<Permission> getAllPermissions() throws DgException {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(" from " + Permission.class.getName());
            return query.list();
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("HibernateException Exception encountered", e);
        }

    }

    public static Permission findPermissionByName(String name) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            Query query = session.createQuery(" from " + Permission.class.getName()+" p WHERE p.name=:permissionName");
            query.setParameter("permissionName", name);
            List list = query.list();

            if(list.size()>0) return (Permission) list.get(0);
            return null;
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("HibernateException Exception encountered", e);
        }
        }

    
    
    public static List<Permission> getAllUnDedicatedPermissions() {
    Session session = null;
    try {
        session = PersistenceManager.getSession();
        Query query = session.createQuery(" from " + Permission.class.getName() +" p WHERE p.dedicated=false");
        List list = query.list();
        return list;
    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("HibernateException Exception encountered", e);
    }
    }

    
    public static List<Permission> getAllDedicatedCompositePermissions() {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            Query query = session.createQuery(" from " + CompositePermission.class.getName() +" p WHERE p.dedicated=true");
            List list = query.list();

            return list;
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("HibernateException Exception encountered", e);
        }
        }
    
    
    public static Set<AmpPMFieldPermissionViewer> getAllAmpPMFieldPermissionViewers(Class permClass) {
    Session session = null;
    try {
        session = PersistenceManager.getSession();

        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is not null");
        query.setParameter("categoryName", permClass.getSimpleName());
        List col = query.list();
        Set<AmpPMFieldPermissionViewer> ret = new TreeSet<AmpPMFieldPermissionViewer>();
        Iterator i = col.iterator();
        while (i.hasNext()) {
        PermissionMap element = (PermissionMap) i.next();
            //ret.put(element.getObjectIdentifier(), element);
        AmpPMFieldPermissionViewer v = new AmpPMFieldPermissionViewer(element);
        ret.add(v);
        }
        return ret;
    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("HibernateException Exception encountered", e);
    }
    }
    
    public static Map<Long, PermissionMap> getAllPermissionMapsForPermissibleClass(Class permClass) {
    Session session = null;
    try {
        session = PersistenceManager.getSession();

        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is not null");
        query.setParameter("categoryName", permClass.getSimpleName());
        List col = query.list();
        Map<Long, PermissionMap> ret = new HashMap<Long, PermissionMap>();
        Iterator i = col.iterator();
        while (i.hasNext()) {
        PermissionMap element = (PermissionMap) i.next();
        ret.put(element.getObjectIdentifier(), element);
        }
        return ret;
    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("HibernateException Exception encountered", e);
    }
    }
    
    public static void cleanGlobalPermissionMapForPermissibleClass(Class permClass) {
        Session session = null;
      try {
        session = PersistenceManager.getSession();
        
      Query query = session.createQuery("delete from "+PermissionMap.class.getName()+
              " WHERE permissibleCategory=:categoryName AND objectIdentifier is null");
        query.setParameter("categoryName", permClass.getSimpleName());
        query.executeUpdate();
        
      } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException( "HibernateException Exception encountered", e);
    }
    }
        
     
    
    public static Long getGlobalPermissionMapIdForPermissibleClass(Class permClass) {
    Session session = null;
      try {
        session = PersistenceManager.getSession();
        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is null");
        query.setParameter("categoryName", permClass.getSimpleName());
        List col = query.list();
        if(col.size()==0) return null;
        PermissionMap pm= (PermissionMap) col.get(0);     
        return pm.getId();
    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException( "HibernateException Exception encountered", e);
    }
    }


    public static PermissionMap getGlobalPermissionMapForPermissibleClass(Class permClass, Session session) {
        Session sessionAux = null;
          try {
            if(session==null)
                sessionAux = PersistenceManager.getSession();
            else sessionAux = session;
            Query query = sessionAux.createQuery("SELECT p from " + PermissionMap.class.getName()
                + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is null");
            query.setParameter("categoryName", permClass.getSimpleName());
            List col = query.list();
            if(col.size()==0) return null;
            PermissionMap pm= (PermissionMap) col.get(0);     
            return pm;
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException( "HibernateException Exception encountered", e);
        }
        }
    
    
    public static Permission getGlobalPermissionForPermissibleClass(Class permClass) {
    Session session = null;
      try {
        session = PersistenceManager.getSession();
        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is null");
        query.setParameter("categoryName", permClass.getSimpleName());
        List col = query.list();
        if(col.size()==0) return null;
        PermissionMap pm= (PermissionMap) col.get(0);    
        return pm.getPermission();
    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException( "HibernateException Exception encountered", e);
    }     
    }

    
    /**
     * Gets the permission map assigned to the given permissible. Ignores any global permission, if any
     * @param obj
     * @return the permission map assigned
     */
    public static PermissionMap getOwnPermissionMapForPermissible(Permissible obj) {
    Session session = null;
    try {
        session = PersistenceManager.getSession();

        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier=:objectId ORDER BY p.objectIdentifier");
        query.setParameter("objectId", obj.getIdentifier());
        query.setParameter("categoryName", obj.getPermissibleCategory().getSimpleName());
        List col = query.list();

        
        if (col.size() == 0)
        return null;

        return (PermissionMap) col.get(0);
    
    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("HibernateException Exception encountered", e);
    }
    }


    /**
     * Gets the permission map assigned to the given permissible. If no permission map is assigned to it specifically, the global permission map
     * assigned to its class is returned (if any).
     * @param obj
     * @return the permission map assigned
     */
    public static PermissionMap getPermissionMapForPermissible(Permissible obj) {
    Session session = null;
    try {
        session = PersistenceManager.getSession();

        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND (p.objectIdentifier is null OR p.objectIdentifier=:objectId) ORDER BY p.objectIdentifier");
        query.setParameter("objectId", obj.getIdentifier());
        query.setParameter("categoryName", obj.getPermissibleCategory().getSimpleName());
        query.setCacheable(true);
        List col = query.list();
        

        if (col.size() == 0)
        return null;

        if (col.size() == 1)
        return (PermissionMap) col.get(0);
        Iterator i = col.iterator();
        while (i.hasNext()) {
        PermissionMap element = (PermissionMap) i.next();
        if (element.getObjectIdentifier() != null)
            return element;
        }

        return null;

    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("HibernateException Exception encountered", e);
    } 
    }

    
    
    

    public static PermissionMap getPermissionMapForPermissible(Object permissibleIdentifier,Class permissibleClass) {
    Session session = null;
    try {
        session = PersistenceManager.getSession();

        Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
            + " p WHERE p.permissibleCategory=:categoryName AND (p.objectIdentifier is null OR p.objectIdentifier=:objectId) ORDER BY p.objectIdentifier");
        query.setParameter("objectId", permissibleIdentifier);
        query.setParameter("categoryName",permissibleClass.getSimpleName());
        List col = query.list();

        if (col.size() == 0)
        return null;

        if (col.size() == 1)
        return (PermissionMap) col.get(0);
        Iterator i = col.iterator();
        while (i.hasNext()) {
        PermissionMap element = (PermissionMap) i.next();
        if (element.getObjectIdentifier() != null)
            return element;
        }

        return null;

    } catch (HibernateException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException("HibernateException Exception encountered", e);
    }
    }

    
    
    public static Identifiable getIdentifiableByClusterIdentifier(String clusterIdentifier,Class permissibleClass) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            //get the cluster identifier (if any) from the db. if the cluster id is not available in the db then instantiating all
            //objects is the only way to get it
            String clusterPropertyName = Permissible.getPermissiblePropertyName(permissibleClass, Permissible.PermissibleProperty.PROPERTY_TYPE_CLUSTER_ID);
    
            if(clusterPropertyName==null) {
                 Query query = session.createQuery("SELECT p from " + permissibleClass.getName());
                 List<ClusterIdentifiable> col = query.list();
                 for (ClusterIdentifiable clusterIdentifiable : col) {
                    if(clusterIdentifiable.getClusterIdentifier().equals(clusterIdentifier)) return clusterIdentifiable;
                }
                return null;
            }
            
            Query query = session.createQuery("SELECT p from " + permissibleClass.getName()
                + " p WHERE p."+clusterPropertyName+"=:objectId");
            query.setParameter("objectId", clusterIdentifier);
          
            List col = query.list();

            if (col.size() == 0)
            return null;

            if (col.size() > 1) {
                logger.error("Non Unique Cluster Identifier !");
                //throw new RuntimeException("Cluster Identifier "+clusterIdentifier+" identified more than one local object for class "+permissibleClass);
            }
            
            return  (Identifiable) col.get(0);

        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("HibernateException Exception encountered", e);
        }
        }

    

    
    public static List<PermissionMap> getAllPermissionMapsForPermission(Long permissionId) throws DgException, HibernateException { 
        Session session = PersistenceManager.getRequestDBSession();
        Query q = session.createQuery("from "+PermissionMap.class.getName()+" p WHERE p.permission.id = :permissionId");
        q.setParameter("permissionId", permissionId);
        return q.list();
    }
    
    
    public static Map<Long, String> getAllPermissibleObjectLabelsForPermissibleClass(Class permClass) {
    Map<Long, String> ret = new HashMap<Long, String>();
    Session session = null;
    try {
        session = PersistenceManager.getSession();
        String idProperty = Permissible.getPermissiblePropertyName(permClass,
            Permissible.PermissibleProperty.PROPERTY_TYPE_ID);
        String labelProperty = Permissible.getPermissiblePropertyName(permClass,
            Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL);
        Query q = session.createQuery("SELECT p." + idProperty + ",p." + labelProperty + " from "
            + permClass.getName() + " p");
        List list = q.list();
        Iterator i = list.iterator();
        while (i.hasNext()) {
        Object[] element = (Object[]) i.next();
        ret.put((Long) element[0], (String) element[1]);
        }
        return ret;

    } catch (HibernateException e) {

        throw new RuntimeException("HibernateException Exception encountered", e);
    }
}

    public static Session saveGlobalPermission(Class permCatClass,  Long permissionId, String permissibleCategory) throws DgException, HibernateException, SQLException {
        // TODO Auto-generated method stub
        Session hs = PersistenceManager.getRequestDBSession();
        Long globalPermissionMapIdForPermissibleClass = PermissionUtil.getGlobalPermissionMapIdForPermissibleClass(permCatClass);
        PermissionMap globalPermissionMapForPermissibleClass=null;
        if (globalPermissionMapIdForPermissibleClass == null)
            globalPermissionMapForPermissibleClass = new PermissionMap();
        else 
            globalPermissionMapForPermissibleClass = (PermissionMap) hs.get(PermissionMap.class,globalPermissionMapIdForPermissibleClass ); 
        
        if(permissionId.longValue()==0 && globalPermissionMapForPermissibleClass.getId()==null)  {
             return null;// mapping.getInputForward();
        } else if(permissionId.longValue()==0) {
            hs.delete(globalPermissionMapForPermissibleClass);
            hs.flush();
        //  pf.setPermissionId(new Long(0));
        //  PersistenceManager.releaseSession(hs);
            return hs;//mapping.getInputForward();      
        }
        
        globalPermissionMapForPermissibleClass.setObjectIdentifier(null);
        globalPermissionMapForPermissibleClass.setPermissibleCategory(permissibleCategory);
        Permission p = (Permission) hs.get(Permission.class, permissionId);
        globalPermissionMapForPermissibleClass.setPermission(p);    
        hs.saveOrUpdate(globalPermissionMapForPermissibleClass);
        
        hs.flush();

        return null;//mapping.getInputForward();
    }

    public static User getUser(Map scope, TeamMember teamMember) {
        User user = (User) scope.get(GatePermConst.ScopeKeys.CURRENT_USER);
        if (user == null || !user.getId().equals(teamMember.getUserId())) {
            user = TeamMemberUtil.getUserEntityByTMId(teamMember.getMemberId());
            scope.put(GatePermConst.ScopeKeys.CURRENT_USER, user);
        }
        return user;
    }

}
