package org.dgfoundation.amp.onepager.util;

import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.protocol.http.WebApplication;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpFMConfigurable;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.dgfoundation.amp.visibility.data.DataVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.Session;

import javax.servlet.ServletContext;
import java.util.*;

public final class FMUtil {
    private static Logger logger = Logger.getLogger(FMUtil.class);
    private static HashMap<String,Boolean> fmVisible=new HashMap<String,Boolean>();
    private static HashMap<String,Boolean> fmEnabled=new HashMap<String,Boolean>();
    private static boolean fmRootChecked = false;
    public static final String fmRootActivityForm="/Activity Form";
    public static final String fmRootPermissionManager="/Permission Manager";
    private static AmpTeamMember ampCurrentMember;

    public static final class PathException extends Exception{
        private static final long serialVersionUID = 1L;

        public PathException(String string) {
            super(string);
        }
    }
    
    
    /**
     * Singleton to check if FM root exists
     */
    public static synchronized final void checkFmRoot(String root){
        //if (!fmRootChecked){
            fmRootChecked = true;
            ServletContext context   = ((WebApplication)Application.get()).getServletContext();
            AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
            AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(context, session.getHttpSession());

            if(ampTreeVisibility!=null){
                if (!existInVisibilityTree(ampTreeVisibility, root, AmpFMTypes.MODULE)){
                    logger.info("Activity Form FM Root Node doesn't exist, attempting to create!");
                    try {
                        addModuleToFM(context, ampTreeVisibility, root, null);
                    } catch (Exception e) {
                        logger.error(">>>");
                        logger.error(">>> Unable to add Activity Form FM ROOT:", e);
                        logger.error(">>>");
                    }
                }
            }
        //}
    }

    public static final boolean isFmEnabled(Component c) {
        try {
            LinkedList<FMInfo> fmInfoPath = getFmPath(c);
            String fmPathString = getFmPathString(fmInfoPath);
            String fmParentPathString = fmPathString.substring(0, fmPathString.lastIndexOf('/'));
            
            AmpFMConfigurable fmc = (AmpFMConfigurable) c;
            
            Boolean cachedResult    = FMFormCache.getInstance().checkCache(GatePermConst.Actions.EDIT, fmPathString, fmc.getFMType() );
            if ( cachedResult != null  ) {
                return cachedResult;
            }
            
            ServletContext context   = ((WebApplication)Application.get()).getServletContext();
            AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
            AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(context, session.getHttpSession());
            boolean result;
            if(ampTreeVisibility!=null && fmParentPathString.length()>0){
                if (!existInVisibilityTree(ampTreeVisibility, fmParentPathString, AmpFMTypes.MODULE)){
                    logger.error("Parent of current component isn't in the FM Tree: " + fmPathString);
                    logger.error("Current feature '"+fmPathString+"' is disabled!");
                    result = false;
                }
                else {
                    AmpObjectVisibility visObj = getObjVisibilityTree(ampTreeVisibility, fmPathString, fmc.getFMType());
                    if (visObj==null){
                        try {/*
                            if (fmc.getFMType() == AmpFMTypes.FEATURE)
                                addFeatureFM(context, ampTreeVisibility, fmPathString, fmParentPathString);
                            else
                            */
                                if (fmc.getFMType() == AmpFMTypes.MODULE)
                                    addModuleToFM(context, ampTreeVisibility, fmPathString, fmParentPathString);
                            result = true;
                        } catch (Exception e) {
                            logger.error("Error while adding current to tree the feature: " + fmPathString, e);
                            result = false;
                        }
                    }
                    else {
                        result = checkIsEnabled(ampTreeVisibility, fmPathString, fmc.getFMType());
                    }
                }
            }
            else{
                if (ampTreeVisibility == null)
                    logger.error("Can't find ampTreeVisibility in context, all components enabled!");
                result = true;
            }
            
            FMFormCache.getInstance().insertInCache(result, GatePermConst.Actions.EDIT, fmPathString, fmc.getFMType() );
            
            return result;
        } catch (PathException e) {
            logger.error(">>>");
            logger.error(e.getMessage());
            logger.error(">>> Current feature is disabled !");
            logger.error(">>>");
        } catch (Exception e) {
            logger.error(">>>");
            logger.error("Error:", e);
            logger.error(">>> Current feature is disabled !");
            logger.error(">>>");
        }
        return false;
    }

    public static final boolean isFmVisible(Component c) { 
        LinkedList<FMInfo> fmInfoPath;
        try {
            fmInfoPath = getFmPath(c);
            String fmPathString = getFmPathString(fmInfoPath);
            AmpFMConfigurable fmc = (AmpFMConfigurable) c;
            
            Boolean cachedResult    = 
                    FMFormCache.getInstance().checkCache(GatePermConst.Actions.VIEW, fmPathString, fmc.getFMType() );
            if ( cachedResult != null  ) {
                return cachedResult;
            }
            
            boolean result = isFmVisible(fmPathString, fmc.getFMType());
            FMFormCache.getInstance().insertInCache(result, GatePermConst.Actions.VIEW, fmPathString, fmc.getFMType() );
            return result;
        } catch (PathException handledByIsFmEnabled) {
        } 
        return true;
    }
    
    public static final boolean isFmVisible(String fmPathString, AmpFMTypes fmType) {
        try {
            ServletContext context   = ((WebApplication)Application.get()).getServletContext();

            AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
            //for admin user fields will be visible
            if( fmPathString.compareTo("Permission Manager") ==0 && "yes".compareTo(session.getIsAdmin()) ==0 ) {
                return true;
            }
            
            AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(context, session.getHttpSession());
            if(ampTreeVisibility!=null){
//              if (!existInVisibilityTree(ampTreeVisibility, fmPathString, fmc.getFMType())){
                    //Feature is disabled on purpose we should show it
                AmpObjectVisibility visObj = getObjVisibilityTree(ampTreeVisibility, fmPathString, fmType);
                if(visObj == null)
                {
                    logger.info("Not found in tree: " + fmPathString);
                    return true;
                }
                else{
                    return checkIsVisible(ampTreeVisibility, fmPathString, fmType); 
                    //return checkIsVisible(visObj); 
                }
            }
            else{
                //Can't find ampTreeVisibility in context, all components enabled!
                return true;
            }
        } catch (Exception handledByIsFmEnabled) {
        }
        //Error case: component disabled, but should be viewable
        return true;
    }
    
    public static boolean checkIsVisible(AmpTreeVisibility atv, String name, AmpFMTypes type){
        AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
        Map scope=PermissionUtil.getScope(session.getHttpSession());

        AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
        if(currentTemplate!=null){
            Set colection;
            if (type == AmpFMTypes.MODULE)
                colection = currentTemplate.getItems();
            else
                colection = currentTemplate.getFeatures();
            
            //for admin user fields will be visible
            if("yes".compareTo(session.getIsAdmin()) ==0 ) return true;
            
            if (type == AmpFMTypes.MODULE) {
                
                AmpObjectVisibility item =  atv.getItemsCache().get(name);
                if(item!=null) return item.canDo(GatePermConst.Actions.VIEW, scope);
                
            } else 
            
            if(colection != null){
                Iterator it = colection.iterator();
                while (it.hasNext()) {
                    AmpObjectVisibility object = (AmpObjectVisibility) it.next();
                    if (object.getName().compareTo(name) == 0){


                        return object.canDo(GatePermConst.Actions.VIEW, scope);
                        //return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    private static boolean checkIsEnabled(AmpTreeVisibility atv, String name, AmpFMTypes type){
        AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
        Map scope=PermissionUtil.getScope(session.getHttpSession());
        
        
        AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility) atv.getRoot();
        if(currentTemplate!=null){
            Set colection;
            if (type == AmpFMTypes.MODULE)
                colection = currentTemplate.getItems();
            else
                colection = currentTemplate.getFeatures();
            
            //for admin user fields will be visible
            if("yes".equals(session.getIsAdmin()))
                return true;
            if (type == AmpFMTypes.MODULE) {
                
                AmpObjectVisibility item = atv.getItemsCache().get(name);
                if(item!=null) return item.canDo(GatePermConst.Actions.EDIT, scope);
                
            } else 
            
            if(colection != null){
                Iterator it = colection.iterator();
                while (it.hasNext()) {
                    AmpObjectVisibility object = (AmpObjectVisibility) it.next();
                    if (object.getName().compareTo(name) == 0){
                        return object.canDo(GatePermConst.Actions.EDIT, scope);
                        //return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static boolean checkIsEnable(AmpObjectVisibility object){
        AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
        Map scope=PermissionUtil.getScope(session.getHttpSession());
        if(object == null) return false;
        boolean canDo = object.canDo(GatePermConst.Actions.EDIT, scope);
        return canDo;
    }
    
    public static synchronized void addFeatureFM(ServletContext context, AmpTreeVisibility ampTreeVisibility, String componentPath, String componentParentPath) throws Exception{
        if(FeaturesUtil.getFeatureVisibility(componentPath)==null){
            AmpModulesVisibility moduleByNameFromRoot = getModuleByNameFromRoot(ampTreeVisibility.getItems().values(), componentParentPath); //ampTreeVisibility.getModuleByNameFromRoot(componentParentPath);
            AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
            Long id=null;
            if(moduleByNameFromRoot!=null){
                id = moduleByNameFromRoot.getId();
                try {   
                    if(FeaturesUtil.getFeatureVisibility(componentPath)!=null){
                        FeaturesUtil.updateFeatureWithModuleVisibility(moduleByNameFromRoot.getId(), componentPath);
                    }
                    else{
                        FeaturesUtil.insertFeatureWithModuleVisibility(ampTreeVisibility.getRoot().getId(),id, componentPath, "no");
                    }
                    logger.info("Inserting feature in FM Tree: " + componentPath);
                    AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
                    ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
                    FeaturesUtil.setAmpTreeVisibility(context, session.getHttpSession(),ampTreeVisibility);
                }
                catch (Exception e) {
                    throw new Exception("Error while adding feature:", e);
                }
            }
            else {
                throw new Exception("Parent of current feature doesn't exist in the tree!");
            }
        }
    }
    
    public static AmpModulesVisibility getModuleByNameFromRoot(Collection<?> list, String moduleName) {
        if (list == null || list.size()<1)
            return null;
        //logger.error("Searching for:" + moduleName);
        for(Object obj:list) {
            AmpModulesVisibility module;
            if (obj instanceof AmpTreeVisibility)
                module = (AmpModulesVisibility) ((AmpTreeVisibility)obj).getRoot();
            else
                module = (AmpModulesVisibility) obj;
            if (module.getName().compareTo(moduleName) == 0)
                return module;
        }
        
        for(Object obj:list) {
            AmpModulesVisibility module;
            if (obj instanceof AmpTreeVisibility)
                module = (AmpModulesVisibility) ((AmpTreeVisibility)obj).getRoot();
            else
                module = (AmpModulesVisibility) obj;
        //  logger.error("Going into: " + module.getName());
            AmpModulesVisibility ret = getModuleByNameFromRoot(module.getSubmodules(), moduleName);
            if (ret != null)
                return ret;
        }
        return null;
    }
    
    public static AmpFeaturesVisibility getFeatureByNameFromRoot(Collection<AmpModulesVisibility> list, String featureName) {
        if (list == null)
            return null;
        
        Iterator<AmpModulesVisibility> it = list.iterator();
        while (it.hasNext()) {
            AmpModulesVisibility module;
            Object obj = it.next();
            if (obj instanceof AmpTreeVisibility)
                module = (AmpModulesVisibility) ((AmpTreeVisibility)obj).getRoot();
            else
                module = (AmpModulesVisibility) obj;

            Iterator it2 = module.getItems().iterator();
            while (it2.hasNext()) {
                AmpFeaturesVisibility feature = (AmpFeaturesVisibility) it2.next();
                if (feature.getName().compareTo(featureName) == 0)
                    return feature;
            }
        }
        
        it = list.iterator();
        while (it.hasNext()) {
            AmpModulesVisibility module;
            Object obj = it.next();
            if (obj instanceof AmpTreeVisibility)
                module = (AmpModulesVisibility) ((AmpTreeVisibility)obj).getRoot();
            else
                module = (AmpModulesVisibility) obj;
            AmpFeaturesVisibility ret = getFeatureByNameFromRoot(module.getSubmodules(), featureName);
            if (ret != null)
                return ret;
        }
        return null;
    }
    
    public static synchronized void addModuleToFM(ServletContext context, AmpTreeVisibility ampTreeVisibility, String component, String parentPath) throws Exception{
        if(FeaturesUtil.getModuleVisibility(component)==null){
            if (parentPath == null)
                FeaturesUtil.insertModuleVisibility(ampTreeVisibility.getRoot().getId(), component, "yes");
            else{
                AmpModulesVisibility moduleByNameFromRoot = getModuleByNameFromRoot(ampTreeVisibility.getItems().values(), parentPath);
                FeaturesUtil.insertModuleVisibility(ampTreeVisibility.getRoot().getId(), moduleByNameFromRoot.getId(), component, "yes");
            }
            logger.info("Inserting module in FM Tree: " + component);
            AmpTemplatesVisibility currentTemplate=(AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
            AmpAuthWebSession session = (AmpAuthWebSession) org.apache.wicket.Session.get();
            FeaturesUtil.setAmpTreeVisibility(context, session.getHttpSession(),ampTreeVisibility);
        }
    }

    public static void changeFmEnabled(Component c, boolean enabled) {
        setFmEnabled(c, enabled);
    }
    
    public static void switchFmEnabled(Component c) {
        Boolean current=isFmEnabled(c);
        setFmEnabled(c, !current);
    }
    
    public static void switchFmVisible(Component c) {
        Boolean isVisible = isFmVisible(c);
        
        changeFmVisible(c, !isVisible);
    }
    
    public static void changeFmVisible(Component c, boolean visible) {
        AmpFMConfigurable fmc = (AmpFMConfigurable) c;
        Boolean isVisible = isFmVisible(c);
        
        LinkedList<FMInfo> fmInfoPath = null;
        try {
            fmInfoPath = getFmPath(c);
        } catch (PathException e) {
            logger.error("Can't get fm path: ", e);
            return;
        }
        String fmPathString = getFmPathString(fmInfoPath);
        
        if (fmc.getFMType() != AmpFMTypes.MODULE && fmc.getFMType() != AmpFMTypes.MODULE)
            throw new RuntimeException("We shouldn't have components that are not MODULES or FEATURES!");
        
        ServletContext context   = ((WebApplication)Application.get()).getServletContext();
        AmpAuthWebSession sessionW = (AmpAuthWebSession) org.apache.wicket.Session.get();
        
        AmpTreeVisibility ampTreeVisibility=FeaturesUtil.getAmpTreeVisibility(context, sessionW.getHttpSession());

        AmpTemplatesVisibility ft;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            ft = (AmpTemplatesVisibility) session.load(AmpTemplatesVisibility.class, ampTreeVisibility.getRoot().getId());
            
            Set set;
            if (fmc.getFMType() == AmpFMTypes.MODULE)
                set = ft.getItems();
            else
                set = ft.getFeatures();
            Iterator it = set.iterator();

            boolean found = false;
            Object obj = null;
            while (it.hasNext()) {
                AmpObjectVisibility o = (AmpObjectVisibility) it.next();
                if (o.getName().compareTo(fmPathString) == 0){
                    obj = o;
                    found = true;
                    break;
                }
            }
            if (found != isVisible){
                throw new Exception("Current component [" + fmPathString + "] has it's visibility status diferent from it's presence in the tree!");
            }
            
            if (!visible){
                set.remove(obj);
            }
            else{
                AmpObjectVisibility newObj = getFromVisibilityTree(ampTreeVisibility, fmPathString, fmc.getFMType());
                set.add(newObj);
            }
            
            session.update(ft);
//session.flush();

            AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility)FeaturesUtil.getTemplateById(ampTreeVisibility.getRoot().getId());
            ampTreeVisibility.buildAmpTreeVisibility(currentTemplate);
            
            FeaturesUtil.setAmpTreeVisibility(context, sessionW.getHttpSession(), ampTreeVisibility);
            logger.info("Changed FM visible status of "+fmc.getFMName()+ " to "+visible);
            
            // notify about visibility change
            DataVisibility.notifyVisibilityChanged();
        }
        catch (Exception ex) {
            logger.error("Exception : " + ex.getMessage()+" while changing FM visible status for "+fmc.getFMName());
        }
    }
    
    public static final void setFmEnabled(Component c, Boolean value) {
        AmpFMConfigurable fmc = (AmpFMConfigurable) c;
        fmEnabled.put(fmc.getFMName(), value);
    }
    
    public static final void setFmVisible(Component c, Boolean value) {
        AmpFMConfigurable fmc = (AmpFMConfigurable) c;
        fmVisible.put(fmc.getFMName(), value);
    }

    public static boolean existInVisibilityTree(AmpTreeVisibility atv, String name, AmpFMTypes type)
    {
        AmpObjectVisibility obj = getFromVisibilityTree(atv, name, type);
        if (obj == null) 
            return false;
        return true;
    }
    
    public static AmpObjectVisibility getObjVisibilityTree(AmpTreeVisibility atv, String name, AmpFMTypes type)
    {
        return getFromVisibilityTree(atv, name, type);
    }

    
    /**
     * Retreive visibility object from tree.
     * WARNING: fmPath is retreived with {@link #getFmPathString(LinkedList)} and 
     * is not the module/feature name! 
     * 
     * @param atv
     * @param fmPath
     * @param type
     * @return
     */
    public static AmpObjectVisibility getFromVisibilityTree(AmpTreeVisibility atv, String fmPath, AmpFMTypes type)
    {
        AmpObjectVisibility obj = null;
        if (type == AmpFMTypes.MODULE){
            //obj = getModuleByNameFromRoot(atv.getItems().values(), fmPath);
            obj = atv.getModuleByNameFromRoot(fmPath);
        }
        /*
        else
            if (type == AmpFMTypes.FEATURE){
                obj = getFeatureByNameFromRoot(atv.getItems().values(), fmPath);
            }
            */
        return obj;
    }
    
    /**
     * Returns the tooltip for this field, if any, stored in the FM database.
     * @param fmc
     * @return the tooltip string, or null if not available
     */
    public static final String getTooltip(AmpFMConfigurable fmc) {
        return "This tooltip for "+fmc.getFMName()+" is supposed to be fetched from the FM database";
    }
    
    public static Component getFirstFmConfigurableParent(Component c){
        while (c != null){
            c = c.getParent();
            if (c instanceof AmpFMConfigurable){
                return c;
            }
        }
        return null;
    }
    
    
    
    public static LinkedList<FMInfo> getFmPath(Component c) throws PathException {
        Component visitor = c;
        String ret = "";
        LinkedList<FMInfo> fmInfoPath = new LinkedList<FMInfo>();
        LinkedList<AmpFMTypes> mmm = new LinkedList<AmpFMTypes>();
        while (visitor != null) {
            if (visitor instanceof AmpFMConfigurable){
                AmpFMConfigurable fmc = (AmpFMConfigurable) visitor;
                String typeName;
                switch (fmc.getFMType()) {
                /*
                case FEATURE:
                    typeName = "feature";
                    break;
                case FIELD:
                    typeName = "field";
                    break;
                    */
                case MODULE:
                    typeName = "module";
                    break;
                default:
                    typeName = "n/a";
                    break;
                }
                ret = "[" + typeName + ": " + fmc.getFMName() + "] " + ret;
                mmm.addFirst(fmc.getFMType());
                FMInfo tmp = new FMInfo(fmc.getFMType(), fmc.getFMName());
                fmInfoPath.addFirst(tmp);
            }
            visitor = visitor.getParent();
        }
//      ret = "[module: Activity Form] "+ ret;
//      mmm.addFirst(AmpFMTypes.MODULE);
//      FMInfo tmp = new FMInfo(AmpFMTypes.MODULE, "Activity Form");
//      fmInfoPath.addFirst(tmp);
        
        //Check that path is compatible with FM structure:
        //      (module)*{(feature)}?
        boolean pathOk = true;
        while (!mmm.isEmpty() && mmm.getFirst() == AmpFMTypes.MODULE)
            mmm.removeFirst();

        if (mmm.size() > 1)
            pathOk = false;
        else{
            if (!mmm.isEmpty() && mmm.getFirst() != AmpFMTypes.MODULE)
                pathOk = false;
        }

        /*
         * Old FM Structure
        //      (module)*{(feature) | [(feature)(field)]}?
        if (mmm.size() > 2)
            pathOk = false;
        else{
            if (!mmm.isEmpty() && mmm.getFirst() != AmpFMTypes.MODULE)
                pathOk = false;
            else
                if (mmm.size() == 2 && mmm.getLast() != AmpFMTypes.FIELD)
                    pathOk = false;
        }
         */

        //logger.info(ret);
        if (!pathOk){
            throw new PathException("Current FM path is wrong: " + ret);
        }
        return fmInfoPath;
    }
    
    public static String getFmPathString(LinkedList<FMInfo> path){
        String ret = "";
        Iterator<FMInfo> it = path.iterator();
        while (it.hasNext()) {
            FMInfo fmInfo = (FMInfo) it.next();
            String tmp = fmInfo.getName().replaceAll("/", " ");
            ret = ret + "/" + tmp;
        }
        return ret;
    }

    public static boolean isVisibleChildWithFmName(Component c, String fmName) {
        try {
            LinkedList<FMInfo> path = FMUtil.getFmPath(c);
            path.add(new FMInfo(AmpFMTypes.MODULE, fmName));
            String pathString = FMUtil.getFmPathString(path);
            return FMUtil.isFmVisible(pathString, AmpFMTypes.MODULE);
        } catch (PathException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }
}
