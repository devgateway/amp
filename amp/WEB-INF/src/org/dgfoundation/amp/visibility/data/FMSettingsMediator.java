/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Single point of reference for all FM settings groups 
 * @author Nadejda Mandrescu
 */
public class FMSettingsMediator {
    protected static final Logger logger = Logger.getLogger(FMSettingsMediator.class);
    
    public static final String FMGROUP_COLUMNS = "COLUMNS";
    public static final String FMGROUP_MEASURES = "MEASURES";
    public static final String FMGROUP_MODULES = "MODULES";
    public static final String FMGROUP_MENU = "MENU";

    /** stores all fm groups classes that are manageable via this proxy */
    private static Map<String, Class<? extends FMSettings>> registeredFMGroups = initFMGroups();
    
    /** stores all instances of fm settings per template */
    private static Map<Long, Map<String, FMSettings>> templateToFMGroupMap =
            Collections.synchronizedMap(new HashMap<Long, Map<String, FMSettings>>());
            
    private static Map<String, Class<? extends FMSettings>> initFMGroups() {
        Map<String, Class<? extends FMSettings>> groups = new HashMap<String, Class<? extends FMSettings>>();
        
        groups.put(FMGROUP_COLUMNS, ColumnsVisibility.class);
        groups.put(FMGROUP_MEASURES, MeasuresVisibility.class);
        groups.put(FMGROUP_MODULES, ModulesVisibility.class);
        groups.put(FMGROUP_MENU, MenuVisibility.class);
        
        return Collections.synchronizedMap(groups);
    }
    
    /**
     * Retrieves a set of enabled settings for the given FM group name
     * @param fmGroupName
     * @return
     */
    public static Set<String> getEnabledSettings(String fmGroupName, Long templateId) {
        if (templateId == null) {
            templateId = FeaturesUtil.getCurrentTemplateId();
        }
        
        FMSettings fmGroup = getFMSettings(fmGroupName, templateId);
        
        if (fmGroup != null) {
            return fmGroup.getEnabledSettings(templateId);
        }
        
        return Collections.emptySet();
    }

    /**
     * Retrieves a set of all settings for the given FM group name
     * @param fmGroupName
     * @return
     */
    public static Set<String> getSettings(String fmGroupName, Long templateId) {
        FMSettings fmGroup = getFMSettings(fmGroupName, templateId);
        return fmGroup.getSettings();
    }
    
    public static boolean supportsFMTree(String fmGroupName, Long templateId) {
        FMSettings fmGroup = getFMSettings(fmGroupName, templateId);
        return fmGroup == null ? false : fmGroup.supportsFMTree();
    }
    
    public static FMTree getEnabledSettingsAsTree(String fmGroupName, Long templateId) {
        FMSettings fmGroup = getFMSettings(fmGroupName, templateId);
        if (fmGroup != null) {
            return fmGroup.getEnabledSettingsAsFMTree();
        }
        return new FMTree(null, false);
    }
    
    /**
     * Identify the template group & create it if doesn't exist yet
     * @param id
     * @return
     */
    synchronized
    private static Map<String, FMSettings> getTemplate(Long id) {
        Map<String, FMSettings> templateGroup = templateToFMGroupMap.get(id);
        if (templateGroup == null) {
            templateGroup = Collections.synchronizedMap(new HashMap<String, FMSettings>());
            templateToFMGroupMap.put(id, templateGroup);
        }
        return templateGroup;
    }
    
    /**
     * Identifies fmGroup settings object for the given group name within a specific template group
     * @param templateGroup
     * @param fmGroupName
     * @return
     */
    private static synchronized FMSettings getFMSettings(String fmGroupName, Long templateId) {
        Map<String, FMSettings> templateGroup = getTemplate(templateId);
        FMSettings fmGroup = templateGroup.get(fmGroupName);
        if (fmGroup == null) {
            Class<? extends FMSettings> clazz = registeredFMGroups.get(fmGroupName);
            if (clazz != null) {
                try {
                    fmGroup = clazz.newInstance();
                    templateGroup.put(fmGroupName, fmGroup);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                // fallback to the generic settings
                ModulesVisibility modulesSettings = (ModulesVisibility) 
                        getFMSettings(FMSettingsMediator.FMGROUP_MODULES, templateId);
                String fmModule = modulesSettings.getOrigName(fmGroupName);
                fmGroup = new GenericVisibility(fmModule, templateId);
                templateGroup.put(fmGroupName, fmGroup);
            }
        }
        return fmGroup;
    }

}
