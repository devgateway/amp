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
import java.util.Set;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Provides Dashboards FM enabled options
 * @author Nadejda Mandrescu
 */
public class DashboardsFMSettings extends DataVisibility implements FMSettings {
	//TODO: All values should come from the database, hardcoding them given the urgency of this feature.
	
	@SuppressWarnings("serial")
	public static final Map<String, Collection<String>> dependencyMapTypeAll = new HashMap<String, Collection<String>>() {{
		put("DASHBOARDS", new ArrayList<String>() 
				{{ 
				add("Aid Predictability"); 
				add("Funding Type"); 
				add("Top Donor Group");
				add("Top Regions");
				add("Top Donors");
				add("Responsible Organizations");
				add("Implementing Agencies");
				add("Beneficiary Agencies");
				add("Executing Agencies");
				add("Peace-building and State-building Goals");
				add("Sector Fragmentation");
			}});
	}};
	
	@SuppressWarnings("serial")
	private static final Set<String> allPossibleValuesSet = new HashSet<String>() {{
		add("Aid Predictability");
		add("Funding Type");
		add("Top Donors");
		add("Top Regions");
		add("Top Donor Group");
		add("Top Sectors");
		add("Responsible Organizations");
		add("Implementing Agencies");
		add("Beneficiary Agencies");
		add("Executing Agencies");
		add("Peace-building and State-building Goals");
		add("Sector Fragmentation");
	}};
	
	@SuppressWarnings("serial")
	protected static final Map<String, String> modulesToMeasuresMap = new HashMap<String, String>() {
		{
			put("/DASHBOARDS ", "DASHBOARDS");

		}
	};

	@SuppressWarnings("serial")
	protected static final Map<String, String> featuresToMeasuresMap = new HashMap<String, String>(){{
		put("Aid Predictability", "Aid Predictability");
		put("Funding Type", "Funding Type");
		put("Top Donors", "Top Donors");
		put("Top Regions", "Top Regions");
		put("Top Donor Group", "Top Donor Group");
		put("Responsible Organizations", "Responsible Organizations");
		put("Top Sectors", "Top Sectors");
		put("Implementing Agencies", "Implementing Agencies");
		put("Beneficiary Agencies", "Beneficiary Agencies");
		put("Executing Agencies", "Executing Agencies");
		put("Peace-building and State-building Goals", "Peace-building and State-building Goals");
		put("Sector Fragmentation", "Sector Fragmentation");
	}};
	
	protected Collection<String> getAllPrecursors() {
		return DashboardsFMSettings.modulesToMeasuresMap.keySet();
	}	
	
	@Override
	protected Set<String> detectVisibleData() {
		sanityCheck();
		Set<String> visiblePrecursors = new HashSet<String>(); 
		Set<String> invisiblePrecursors = new HashSet<String>(getAllPrecursors());
		Set<String> visibleData = new HashSet<String>(); 
		Set<String> invisibleData = new HashSet<String>(getAllData());
		AmpTemplatesVisibility currentTemplate = FeaturesUtil.getCurrentTemplate();

		//check modules
		List<AmpModulesVisibility> modules = FeaturesUtil.getAmpModulesVisibility(getDataMap(DataMapType.MODULES).keySet(), currentTemplate.getId());
		splitObjectsByVisibility(modules, getDataMap(DataMapType.MODULES), visiblePrecursors, invisiblePrecursors);
		
		//check features
		List<AmpFeaturesVisibility> features = FeaturesUtil.getAmpFeaturesVisibility(getDataMap(DataMapType.FEATURES).keySet(), currentTemplate.getId());
		splitObjectsByVisibility(features, getDataMap(DataMapType.FEATURES), visibleData, invisibleData);
		
		visiblePrecursors.addAll(getVisibleByDefault());
		
		Map<String, Collection<String>> allDependenciesMap = getDependancyMapTypeAll();
		if (allDependenciesMap != null && allDependenciesMap.size() > 0) {
			for (Entry<String, Collection<String>> entry : allDependenciesMap.entrySet()) {
				if (visiblePrecursors.containsAll(entry.getValue())) {
					visibleData.add(entry.getKey());
					invisibleData.remove(entry.getKey());
				}
			}
		}
		logger.info("Not visible: " + invisibleData);
		// avoid any tentative to change it  
		return Collections.unmodifiableSet(visibleData);
		
	}
	
	@Override
	public Set<String> getEnabledSettings() {
		return getCurrentVisibleData();
	}
	
	protected DashboardsFMSettings() {
	}

	@Override
	protected List<String> getVisibleByDefault() {
		List<String> currentlyVisible = new ArrayList<>();
		currentlyVisible.add("Aid Predictability");
		currentlyVisible.add("Funding Type");
		currentlyVisible.add("Top Donors");
		currentlyVisible.add("Top Regions");
		currentlyVisible.add("Top Donor Group");
		currentlyVisible.add("Responsible Organizations");
		currentlyVisible.add("Top Sectors");
		currentlyVisible.add("Beneficiary Agencies");
		currentlyVisible.add("Implementing Agencies");
		currentlyVisible.add("Executing Agencies");
		currentlyVisible.add("Peace-building and State-building Goals");
		currentlyVisible.add("Sector Fragmentation");
		
		return currentlyVisible;
	}

	@Override
	protected Set<String> getAllData() {
		return allPossibleValuesSet;
	}

	@Override
	protected Map<String, String> getDataMap(DataMapType dataMapType) {
		switch(dataMapType) {
			case MODULES: return modulesToMeasuresMap; 
			case FEATURES: return featuresToMeasuresMap;
			case DEPENDENCY: return new HashMap<String, String>();
			default: 
				return noDataMap; 
		}
	}

	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAny() {
		 return new HashMap<String,Collection<String>>();
	}

	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAll() {
		return dependencyMapTypeAll;
	}

}
