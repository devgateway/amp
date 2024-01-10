package org.dgfoundation.amp.onepager.util;

import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FMFormCache {
    
    private ConcurrentHashMap<String, Boolean> editPermCache;
    private ConcurrentHashMap<String, Boolean> viewPermCache;

    
    private boolean disabled    = true;
    
    public static FMFormCache getInstance() {
        AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
        if ( session.getFormCache() == null ) {
            session.setFormCache(new FMFormCache() );
        }
        return session.getFormCache();
    }
    
    private FMFormCache () {
        this.editPermCache  = new ConcurrentHashMap<>();
        this.viewPermCache  = new ConcurrentHashMap<>();
        
    }
    
    


    
    public void insertInCache(boolean value, String action, String componentName, AmpFMTypes type) {
        
        if ( disabled ) return;
        
        AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
        Map scope=PermissionUtil.getScope(session.getHttpSession());
        
        insertInCache(value, action, scope, componentName, type);
        
    }
    
    public void insertInCache(boolean value, String action, Map scope, String componentName, AmpFMTypes type) {
        
        if ( disabled ) return;
        
        /*
         * In case scope contains exactly: team member, activity and permissabe object (AmpObjectVisibility)
         */
        if ( scope != null && scope.size() == 3 ) {
            Map<String,Boolean> cache   = null;
            if ( GatePermConst.Actions.EDIT.equals(action) ) {
                cache   = this.editPermCache;
            }
            else if ( GatePermConst.Actions.VIEW.equals(action) ) {
                cache   = this.viewPermCache;
            }
            if ( cache != null ) {
                AmpActivityVersion act  = null;
                AmpObjectVisibility permissable = null;
                for ( Object obj: scope.values() ) {
                    if ( obj instanceof AmpActivityVersion ) 
                        act = (AmpActivityVersion) obj;
                    else if ( obj instanceof AmpObjectVisibility ) 
                        permissable = (AmpObjectVisibility) obj;
                    
                    if ( act != null && permissable != null ) {
                        String key = generateKey(act, componentName, type);
                        cache.put(key, value);
                    }
                }
            }
        }
    }
    
    public Boolean checkCache(String action, String componentName, AmpFMTypes type) {
        
        if ( disabled ) return null;
        
        AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
        Map scope=PermissionUtil.getScope(session.getHttpSession());
        
        return checkCache(action, scope, componentName, type);
    }
    
    public Boolean checkCache(String action, Map scope, String componentName, AmpFMTypes type) {
        
        if ( disabled ) return null;
        
        /* 
         * In  case scope contains exactly: team member, activity and permissabe object (AmpObjectVisibility)
         */
        if ( scope != null && scope.size() == 3 ) {
            Map<String,Boolean> cache   = null;
            if ( GatePermConst.Actions.EDIT.equals(action) ) {
                cache   = this.editPermCache;
            }
            else if ( GatePermConst.Actions.VIEW.equals(action) ) {
                cache   = this.viewPermCache;
            }
            
            if ( cache != null ) {
                AmpActivityVersion act  = null;
                AmpObjectVisibility permissable = null;
                for ( Object obj: scope.values() ) {
                    if ( obj instanceof AmpActivityVersion ) 
                        act = (AmpActivityVersion) obj;
                    else if ( obj instanceof AmpObjectVisibility ) 
                        permissable = (AmpObjectVisibility) obj;
                    
                    if ( act != null && permissable != null ) {
                        String key = generateKey(act, componentName, type);
                        Boolean cachedResult    = cache.get(key);
                        ////System.out.println( "Found in cache !!!" );
                        return cachedResult;
                    }
                }
            }
        }
        return null;
    } 
    
    private static String generateKey ( AmpActivityVersion act, String componentName, AmpFMTypes type )  {
        String typeStr  = "none";
        if ( AmpFMTypes.MODULE.equals(type) ) 
            typeStr = "module";
        return typeStr + "-" + componentName 
                + "-" + act.getAmpActivityId();
    }
    
    public void clear () {
        this.editPermCache.clear();
        this.viewPermCache.clear();
    }

    public void disable (boolean withClear) {
        this.disabled   = true;
        if ( withClear )
            this.clear();
    }
    
    public void enable (boolean withClear) {
        this.disabled   = false;
        if ( withClear )
            this.clear();
    }
    
    public ConcurrentHashMap<String, Boolean> getEditPermCache() {
        return this.editPermCache;
    }

    public void setEditPermCache(ConcurrentHashMap<String, Boolean> editPermCache) {
        this.editPermCache = editPermCache;
    }
    
    public ConcurrentHashMap<String, Boolean> getViewPermCache() {
        return this.viewPermCache;
    }

    public void setViewPermCache(ConcurrentHashMap<String, Boolean> viewPermCache) {
        this.viewPermCache = viewPermCache;
    }
    
}
