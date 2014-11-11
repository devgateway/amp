/**
 * 
 */
package org.dgfoundation.amp.reports;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Detects which measures are visible in Activity Form and can be further used, 
 * e.g. in report generation & filtering 
 * @author Alexandru Cartaleanu
 */
public class MeasuresVisibility extends DataVisibility {
	protected static final Logger logger = Logger.getLogger(MeasuresVisibility.class);
	
	

	@SuppressWarnings("serial")
	protected static final Map<String, String> modulesToMeasuresMap = new HashMap<String, String>() {{
		put("/Activity Form/Funding/Funding Group/Funding Item/Commitments", "Commitments");
		put("/Activity Form/Funding/Funding Group/Funding Item/Disbursements", "Disbursements");
		put("/Activity Form/Funding/Funding Group/Funding Item/Expenditures", "Expenditures");
		
	}};
	
	@SuppressWarnings("serial")
	private static final Set<String> allPossibleValuesSet = new HashSet<String>() {{
		Collection<AmpCategoryValue> adjTypes = CategoryManagerUtil.getAmpCategoryValueCollectionByKey("adjustment_type");		
		for (AmpCategoryValue adj: adjTypes) {
			for (String trans: modulesToMeasuresMap.values()) {
				add(adj.getLabel() + " " + trans);
			}
		}
	}};
	
	 
	

	private static MeasuresVisibility currentMeasuresVisibility = null;

	
	@Override
	protected Set<String> detectVisibleData() {
		sanityCheck();
		Set<String> visiblePrecursors = new HashSet<String>(); 
		Set<String> invisiblePrecursors = new HashSet<String>(getAllPrecursors());

		
		Set<String> visibleData = new HashSet<String>(); 
		visibleData.addAll(getVisibleByDefault());
		Set<String> invisibleData = new HashSet<String>(getAllData());
		invisibleData.removeAll(getVisibleByDefault());

		
		
		
		AmpTemplatesVisibility currentTemplate = FeaturesUtil.getCurrentTemplate();

		//check modules
		List<AmpModulesVisibility> modules = FeaturesUtil.getAmpModulesVisibility(getDataMap(DataMapType.MODULES).keySet(), currentTemplate.getId());
		processVisbleObjects(modules, getDataMap(DataMapType.MODULES), visiblePrecursors, invisiblePrecursors);

		
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
	
	/**
	 * @return the current set of visible measures
	 */
	synchronized public static Set<String> getVisibleMeasures() {
		if (currentMeasuresVisibility == null)
			currentMeasuresVisibility = new MeasuresVisibility();
		return currentMeasuresVisibility.getCurrentVisibleMeasures();
	}
	
	private Set<String> visibleMeasures = null;
	
	private MeasuresVisibility() {

	}
	/** keeps track of visibility changes */
	private static AtomicBoolean atomicVisibilityChanged = new AtomicBoolean(false);
	
	protected static void setVisibilityChanged() {
		atomicVisibilityChanged.set(true);
	}
	private Set<String> getCurrentVisibleMeasures() {
		if (visibleMeasures == null)
			visibleMeasures = detectVisibleData();
		return visibleMeasures;
	}
	

	
	
	

	
	@SuppressWarnings("serial")
	public static final Map<String, Collection<String>> dependencyMap = new HashMap<String, Collection<String>>() {{
		Collection<AmpCategoryValue> adjTypes = CategoryManagerUtil.getAmpCategoryValueCollectionByKey("adjustment_type");		
		for (final AmpCategoryValue adj: adjTypes) {
			for (final Entry<String, String> val: modulesToMeasuresMap.entrySet()) {
				put(adj.getLabel() + " " + val.getValue(), new ArrayList<String>() {{  add(val.getValue());}});
			}
		}
	}};	
	protected static final Map<String, String> featuresToMeasuresMap = new HashMap<String, String>();
	
	protected static final Map<String, String> fieldsToMeasuresMap = new HashMap<String, String>();
	
	protected static final List<String> visibleByDefault = Arrays.asList(
	);

	@Override
	protected List<String> getVisibleByDefault() {
		return visibleByDefault;
	}

	
	protected Collection<String> getAllPrecursors() {
		return MeasuresVisibility.modulesToMeasuresMap.keySet();
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
		case FIELDS: return fieldsToMeasuresMap;
		case DEPENDENCY: return new HashMap<String, String>();
		default: return null; //shouldn't come here
		}
	}

	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAny() {
		return new HashMap<String,Collection<String>>();
	}

	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAll() {
		return dependencyMap;
	}
	
}
