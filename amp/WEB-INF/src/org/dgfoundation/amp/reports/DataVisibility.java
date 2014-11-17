/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
	
	/**
	 * Notifies that FM visibility changed
	 */
	public static void notifyVisibilityChanged() {
		ColumnsVisibility.setVisibilityChanged();
		MeasuresVisibility.setVisibilityChanged();
	}
	
	protected Set<String> detectVisibleData() {
		sanityCheck();
		Set<String> visibleData = new HashSet<String>(); 
		visibleData.addAll(getVisibleByDefault());
		Set<String> invisibleData = new HashSet<String>(getAllData());
		invisibleData.removeAll(getVisibleByDefault());
		
		AmpTemplatesVisibility currentTemplate = FeaturesUtil.getCurrentTemplate();
		
		//check fields
		List<AmpFieldsVisibility> fields = FeaturesUtil.getAmpFieldsVisibility(getDataMap(DataMapType.FIELDS).keySet(), currentTemplate.getId());
		processVisbleObjects(fields, getDataMap(DataMapType.FIELDS), visibleData, invisibleData);
		
		//check features
		List<AmpFeaturesVisibility> features = FeaturesUtil.getAmpFeaturesVisibility(getDataMap(DataMapType.FEATURES).keySet(), currentTemplate.getId());
		processVisbleObjects(features, getDataMap(DataMapType.FEATURES), visibleData, invisibleData);
		
		//check modules
		List<AmpModulesVisibility> modules = FeaturesUtil.getAmpModulesVisibility(getDataMap(DataMapType.MODULES).keySet(), currentTemplate.getId());
		processVisbleObjects(modules, getDataMap(DataMapType.MODULES), visibleData, invisibleData);
		
			/* old implementation of fields is not up, cannot rely on this approach as a general rule
		for(AmpFieldsVisibility field : fields) {
			String name = field.getName();
		//for(String name : COLUMNS.keySet()) {
			unmapped.remove(name);
			boolean visibleField = FeaturesUtil.isVisibleField(name); 
			boolean visibleFeature = FeaturesUtil.isVisibleFeature(field.getParent().getName());
			boolean visibleModule = FeaturesUtil.isVisibleModule(field.getParent().getParent().getName());
			logger.info(
					String.format("Field = [%b][%s], "
							+ "Feature = [%b][%s], "
							+ "Module = [%b][%s]", 
							visibleField, name, 
							visibleFeature, field.getParent().getName(), 
							visibleModule, field.getParent().getParent().getName()));
			if (visibleField && visibleFeature && visibleModule)
				visibleColumns.add(name);
			else
				notVisible.add(name);
		}
		*/
		
		dependencyCheck(visibleData, invisibleData);
		
		logger.info("Not visible: " + invisibleData);
		
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
			logger.warn("Unmapped columns for which by default visibility = false: " + unmapped);
		else
			logger.info("All columns are mapped and visibility can be detected.");
	}
	
	protected <T extends AmpObjectVisibility> void  processVisbleObjects(List<T> visibilityList, 
			Map<String, String> nameToColumnMap,
			Set<String> visibleColumns, Set<String> invisibleColumns) {
		Set<AmpObjectVisibility> visibleParents = new HashSet<AmpObjectVisibility>();
		Set<AmpObjectVisibility> invisibleParents = new HashSet<AmpObjectVisibility>();
		
		for (ListIterator<T> iter = visibilityList.listIterator(); iter.hasNext(); ) {
			AmpObjectVisibility o = iter.next();
			
			//check if all ancestors are visible
			AmpObjectVisibility parent = o.getParent();
			boolean visible = true;
			while (parent != null && visible) {
				if (invisibleParents.contains(parent))
					visible = false;
				else if (!visibleParents.contains(parent)) {
					visible = FeaturesUtil.isVisible(parent);
					if (visible)
						visibleParents.add(parent);
				}
				parent = parent.getParent();
			}
			
			if (visible) {
				String columnName = nameToColumnMap.get(o.getName());
				invisibleColumns.remove(columnName);
				visibleColumns.add(columnName);
			} else {
				//if current parent is invisible, then place all children to invisible
				while (o != null && (parent == null || !o.equals(parent))) {
					invisibleParents.add(o);
					o = o.getParent();
				}
			}
		}
	}
	
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
