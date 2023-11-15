/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.*;
import java.util.Map.Entry;

/**
 * Detects which main FM modules are enabled, that depends on root module being enabled 
 * @author Nadejda Mandrescu
 */
public class ModulesVisibility extends DataVisibility implements FMSettings {
    protected static final Logger logger = Logger.getLogger(ModulesVisibility.class);
    
    protected ModulesVisibility() {
    }
    
    @Override
    public Set<String> getEnabledSettings(Long templateId) {
        return getVisibleData(templateId);
    }

    @Override
    public Set<String> getSettings() {
        return getAllData();
    }

    @Override
    protected List<String> getVisibleByDefault() {
        return noDataList;
    }

    @Override
    protected Set<String> getAllData() {
        return mainModulesSet;
    }

    @Override
    protected Map<String, String> getDataMap(DataMapType dataMapType) {
        switch(dataMapType) {
        case MODULES:
            return mainModulesMap;
        default:
            return noDataMap;
        }
    }

    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAny() {
        return noDataCollectionMap;
    }

    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAll() {
        return noDataCollectionMap;
    }
    
    protected static final Map<String, String> mainModulesMap = getModules();
    
    protected static Map<String, String> getModules() {
        Map<String, String> parentModules = new HashMap<String, String>();
        Set<String> moduleNames = FeaturesUtil.getMainModulesNames();
        for (String name : moduleNames) {
            // remove any slashes from modules
            String displayedName = StringUtils.strip(name, "/");
            // skip some invalid root modules which have sub-elements in their name
            if (!displayedName.contains("/")) {
                parentModules.put(name, displayedName.toUpperCase());
            }
        }
        return parentModules;
    }
    
    protected static final Set<String> mainModulesSet = new HashSet<String>(mainModulesMap.values());
    
    /**
     * Provides the original FM name associated to the module
     * @param displayName the display name used as reference
     * @return the original FM name
     */
    public String getOrigName(String moduleDisplayName) {
        for (Entry<String, String> entry : mainModulesMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(moduleDisplayName))
                return entry.getKey();
        }
        return null;
    }
    
}
