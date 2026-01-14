/**
 * Permissible.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.core;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.gateperm.util.PermissionUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Permissible.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.core
 * @since 29.08.2007
 */
public abstract class Permissible implements Identifiable {
    public static final String GLOBAL_PERMISSION_MANAGER = "Global Permission Manager";
    private static Logger logger = Logger.getLogger(Permissible.class);

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PermissibleProperty {
        String[] type();

        public final static String PROPERTY_TYPE_ID = "ID";

        public final static String PROPERTY_TYPE_LABEL = "LABEL";
        
        public final static String PROPERTY_TYPE_CLUSTER_ID="CLUSTER_ID";
    }

    public static String getPermissiblePropertyName(Class permClass, String type) {
        Class c = permClass;
        while (!c.equals(Object.class)) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField
                        .isAnnotationPresent(PermissibleProperty.class)) {
                    PermissibleProperty annotation = declaredField
                            .getAnnotation(PermissibleProperty.class);
                    if (PermissionUtil.arrayContains(annotation.type(), type))
                        return declaredField.getName();
                }
            }
            c = permClass.getSuperclass();
        }
        return null;
    }

    /**
     * Returns a set of actions that this Permissible object implements. An
     * object may not need all possible actions that a permission may allow. It
     * may only need a subset of them
     * 
     * @return
     */
    public abstract String[] getImplementedActions();

    /**
     * Retruns the permission object associated with this permissible object.
     * @param own if true, it will only return the permission associated with this specific object or null if none existing. Otherwise
     * it returns the permission associated with this object, or, if null, the global permission associated with the object class
     * @return the permission object
     */
    public Permission getPermission(boolean own) {
        PermissionMap pm;
        if(own)
        pm=PermissionUtil.getOwnPermissionMapForPermissible(this);
        else
        pm=PermissionUtil.getPermissionMapForPermissible(this);
        if(pm==null) return null;
        return pm.getPermission();
    }
        
    /**
     * @return the object category to identify specific permission objects. This
     *         is usually a constant stored in Permissible and it usually
     *         represents its Class.
     */
    public abstract Class getPermissibleCategory();

    /**
     * Gets the list of linked permissions with this object. Queries the
     * permissions to produce a set of unique allowed actions for this object.
     * 
     * @param scope
     *            a map with the scope of the application (various variables
     *            like request, session or parts of request, session -
     *            currentMember, etc... that are relevant for the Gate logic All
     *            the actions that are not implemented by the Permissible object
     *            will be retained.
     * @see getImplementedActions()
     * @return the collection of unique allowed actions
     */
    public Collection<String> getAllowedActions(Map scope) {
        //put the self into scope:
        scope.put(GatePermConst.ScopeKeys.PERMISSIBLE,this);
        
        
        PermissionMap permissionMapForPermissible = getPermissionMap();
        Collection<String> actions = processPermissions(permissionMapForPermissible, scope);
                          
        java.util.List<String> implementedActions = buildImplementedActionsAsList();
        actions.retainAll(implementedActions);
            
          
        return actions;
    }
    
    protected java.util.List<String> implementedActionsAsList = null;
    
    /**
     * getImplementedActions() is, by default, a clone() call (for safety reasons) - which is very expensive. Because this function always returns the same value for a class instance (and even for a class, but let's be paranoid about it), we'll cache Arrays.asList(getImplementedActions()) per instance
     * @return
     */
    protected java.util.List<String> buildImplementedActionsAsList()
    {
        if (implementedActionsAsList == null)
            implementedActionsAsList = Collections.unmodifiableList(Arrays.asList(getImplementedActions()));
        return implementedActionsAsList;
    }
    
    protected PermissionMap cachedPermissionMap = null;
    protected boolean permissionMapCached = false;
    protected static long permMapCalls = 0;
    protected static long permMapCachedCalls = 0;
    
    protected synchronized PermissionMap getPermissionMap()
    {
        permMapCalls ++;
        if (!permissionMapCached)
        {
            cachedPermissionMap = PermissionUtil.getPermissionMapForPermissible(this);
            permissionMapCached = true;
        }
        else
        {
            permMapCachedCalls ++;
        }
        if (permMapCalls % 500 == 0)
            logger.info(String.format("getPermissionMap(): called %d times, out of which cached %d (%.2f percent)", permMapCalls, permMapCachedCalls, 100.0 * permMapCachedCalls / permMapCalls));
        return cachedPermissionMap;
    }
    
    public static Collection<String> processPermissions(PermissionMap permissionMapForPermissible,Map scope) {
        Set<String> actions = new TreeSet<String>();
        if(permissionMapForPermissible!=null && permissionMapForPermissible.getPermission()!=null) {
            Set<String> allowedActions = permissionMapForPermissible.getPermission().getAllowedActions(scope);
            if(allowedActions!=null) actions.addAll(allowedActions);
            if(permissionMapForPermissible.getObjectIdentifier()==null)
            logger.debug("Actions allowed by the Global Permission "+permissionMapForPermissible.getPermission().getName()+": "+actions); else
            logger.debug("Actions allowed for object "+ permissionMapForPermissible.getObjectLabel() + " (id="+permissionMapForPermissible.getObjectIdentifier()
                    + ") of type " + permissionMapForPermissible.getPermissibleCategory() + " are "
                    + actions); }
//          Object o = scope.get(GatePermConst.ScopeKeys.PERMISSIBLE);
//          if ( o instanceof AmpModulesVisibility )
//          {
//              //if( ((AmpModulesVisibility)o).getName().contains("Project Title") )
//                  logger.debug("---------------PERMISSIBLE " + ((AmpModulesVisibility)o).getName()+ " actions: "+actions);
//              
//          }
        return actions;
    }
    

    public static Collection<String> getAllowedActions(Object permissibleIdentifier, Class permissibleClass,Map scope) {
        PermissionMap permissionMapForPermissible = PermissionUtil.getPermissionMapForPermissible(permissibleIdentifier, permissibleClass);
         Collection<String> actions = processPermissions(permissionMapForPermissible, scope);
         
         //we cannot filter out the implemented actions yet, so we just return the whole list...
         return actions;
    }
        
    
    /**
     * Returns true if the current object is allowed to do the specified action
     * for the given scope
     * 
     * @param actionName
     *            the name of the action defined in GatePermConst class
     * @param scope
     *            the scope with the objects that represent the current state of
     *            amp - eg: request,session,etc...
     * @see GatePermConst
     * @return
     */
    public boolean canDo(String actionName, Map scope) {
        //logger.info("Testing canDo for Permissible: " + this);
        if (!FeaturesUtil.isVisibleModule(GLOBAL_PERMISSION_MANAGER)) {
            return true;
        } else {
            Collection<String> allowedActions = getAllowedActions(scope);
            return allowedActions.contains(actionName);
        }
    }

    /**
     * Static implementation of the same method
     * @param actionName
     * @param permissibleIdentifier
     * @param permissibleClass
     * @param scope
     * @return
     */
    public static boolean canDo(String actionName,Object permissibleIdentifier, Class permissibleClass, Map scope) {
        Collection<String> allowedActions = getAllowedActions(permissibleIdentifier, permissibleClass, scope);
        return allowedActions.contains(actionName);
    }

    
}
