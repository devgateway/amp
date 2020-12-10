/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Provides common support for visibility detection, 
 * at the moment for report columns and measures.
 * 
 * @author Nadejda Mandrescu
 */

public abstract class DataVisibility {
    protected enum DataMapType {
        MODULES,
        FEATURES,
        FIELDS,
        DEPENDENCY
    };
    
    protected static final Logger logger = Logger.getLogger(DataVisibility.class);
    
    private static List<DataVisibility> registeredUsers = 
            Collections.synchronizedList(new ArrayList<DataVisibility>());
    
    /**
     * Notifies that FM visibility changed
     */
    synchronized
    public static void notifyVisibilityChanged() {
        for (DataVisibility visibilityUser : registeredUsers) {
            visibilityUser.atomicVisibilityChanged.set(true);
        }
    }
    
    /** keeps track of visibility changes */
    protected AtomicBoolean atomicVisibilityChanged = new AtomicBoolean(false);

    protected Set<String> visibleData = null;
    
    /**
     * Base constructor that also registers for visibility changes notifications
     */
    protected DataVisibility() {
        registeredUsers.add(this);
    }
    
    protected synchronized Set<String> getVisibleData(Long templateId) {
        if (atomicVisibilityChanged.compareAndSet(true, false) || visibleData == null)
            visibleData = detectVisibleData(templateId);
        return visibleData;
    }
    
    protected Set<String> detectVisibleData(Long templateId) {
        sanityCheck();
        Set<String> visibleData = new HashSet<String>(); 
        visibleData.addAll(getVisibleByDefault());
        Set<String> invisibleData = new HashSet<String>(getAllData());
        invisibleData.removeAll(getVisibleByDefault());
        
        //check fields
        List<AmpFieldsVisibility> fields = FeaturesUtil.getAmpFieldsVisibility(
                getDataMap(DataMapType.FIELDS).keySet(), templateId);
        splitObjectsByVisibility(fields, getDataMap(DataMapType.FIELDS), visibleData, invisibleData);
        
        //check features
        List<AmpFeaturesVisibility> features = FeaturesUtil.getAmpFeaturesVisibility(
                getDataMap(DataMapType.FEATURES).keySet(), templateId);
        splitObjectsByVisibility(features, getDataMap(DataMapType.FEATURES), visibleData, invisibleData);
        
        //check modules
        List<AmpModulesVisibility> modules = FeaturesUtil.getAmpModulesVisibility(
                getDataMap(DataMapType.MODULES).keySet(), templateId);
        splitObjectsByVisibility(modules, getDataMap(DataMapType.MODULES), visibleData, invisibleData);
        dependencyCheck(visibleData, invisibleData);
        
        //logger.info("Not visible: " + invisibleData);
        
        // avoid any tentative to change it  
        return Collections.unmodifiableSet(visibleData);
    }
    
    /**
     * Detects if dependent elements are visible based on 'dependent by' element visibility.
     * Note: if overridden, then please make sure to update the invisibleData set accordingly.
     * 
     * @param visibleData currently detected visible data
     * @param invisibleData currently detected invisible data
     */
    protected void dependencyCheck(Set<String> visibleData, Set<String> invisibleData) {
        // check 1-1 dependency
        Map<String, String> oneToOneDependecyMap = getDataMap(DataMapType.DEPENDENCY);
        if (oneToOneDependecyMap != null && oneToOneDependecyMap.size() > 0) {
            for(Entry<String, String> entry : oneToOneDependecyMap.entrySet()) {
                if (visibleData.contains(entry.getValue())) {
                    visibleData.add(entry.getKey());
                    invisibleData.remove(entry.getKey());
                }
            }
        }

        // check 1 - any dependency
        Map<String, Collection<String>> anyDependencyMap = getDependancyMapTypeAny();
        if (anyDependencyMap != null && anyDependencyMap.size() > 0) {
            for (Entry<String, Collection<String>> entry : anyDependencyMap.entrySet()) {
                for (String dependecy : entry.getValue())
                    if (visibleData.contains(dependecy)) {
                        visibleData.add(entry.getKey());
                        invisibleData.remove(entry.getKey());
                        break;
                    }
            }
        }
        // check 1 - all dependency
        Map<String, Collection<String>> allDependenciesMap = getDependancyMapTypeAll();
        if (allDependenciesMap != null && allDependenciesMap.size() > 0) {
            for (Entry<String, Collection<String>> entry : allDependenciesMap.entrySet()) {
                if (visibleData.containsAll(entry.getValue())) {
                    visibleData.add(entry.getKey());
                    invisibleData.remove(entry.getKey());
                }
            }
        }
        
        
        
    }
    
    /**
     * checks if all columns are mapped
     */
    protected void sanityCheck() {
        Set<String> unmapped = new HashSet<String>(getAllData());
        unmapped.removeAll(getDataMap(DataMapType.MODULES).values());
        unmapped.removeAll(getDataMap(DataMapType.FEATURES).values());
        unmapped.removeAll(getDataMap(DataMapType.FIELDS).values());
        unmapped.removeAll(getDataMap(DataMapType.DEPENDENCY).keySet());
        unmapped.removeAll(getVisibleByDefault());
        unmapped.removeAll(getDependancyMapTypeAll().keySet());
        unmapped.removeAll(getDependancyMapTypeAny().keySet());
        if (unmapped.size() > 0)
            logger.warn("Unmapped data for which by default visibility = false: " + unmapped);
        else
            logger.info("All data is mapped and visibility can be detected.");
    }
    
    /**
     * adds to visibleColumns the list of items which are visible, mapped
     * @param visibilityList
     * @param nameToColumnMap Map<FM_path, data>
     * @param visibleColumns - data
     * @param invisibleColumns - data
     */
    protected <T extends AmpObjectVisibility> void splitObjectsByVisibility(List<T> visibilityList,
            Map<String, String> nameToColumnMap, Set<String> visibleColumns, Set<String> invisibleColumns) {

        for (AmpObjectVisibility o : visibilityList) {
            String columnName = nameToColumnMap.get(o.getName());
            invisibleColumns.remove(columnName);
            visibleColumns.add(columnName);
        }
    }
    
    /**
     * do not clone DataVisibility. If you're trying to do it, you're doing it wrong
     */
    @Override public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    
    /* Default no data storage */
    protected static final List<String> noDataList = Collections.emptyList();
    protected static final Set<String> noDataSet = Collections.emptySet();
    protected static final Map<String, String> noDataMap = Collections.emptyMap();
    protected static final Map<String, Collection<String>> noDataCollectionMap = Collections.emptyMap(); 
    
    /* ******************************************************************************************
     * We need all children to make conscious decisions re which data to provide and which not.
     * When some documentation will be available we can configure default options here. 
     * ******************************************************************************************/
    
    /** provides data visible by default */ 
    abstract protected List<String> getVisibleByDefault();
    
    /** provides all data that is possible to be used */
    abstract protected Set<String> getAllData();
    
    /** provides the mapping for the specific type */
    abstract protected Map<String, String> getDataMap(DataMapType dataMapType);
    
    /** provides the dependencies that can be considered visible if ANY dependent by element is visible */ 
    abstract protected Map<String, Collection<String>> getDependancyMapTypeAny();
    
    /** provides the dependencies that can be considered visible if ANY dependent by element is visible */ 
    abstract protected Map<String, Collection<String>> getDependancyMapTypeAll();
}
