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

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;

/**
 * Detects which measures are visible in Activity Form and can be further used, 
 * e.g. in report generation & filtering 
 * @author Alexandru Cartaleanu
 */
public class MeasuresVisibility extends DataVisibility {
	protected static final Logger logger = Logger.getLogger(MeasuresVisibility.class);
	
	public static final List<String> TRANSACTION_TYPES = Arrays.asList(ArConstants.COMMITMENT, ArConstants.DISBURSEMENT, ArConstants.EXPENDITURE);
	

	@SuppressWarnings("serial")
	protected static final Map<String, String> modulesToMeasuresMap = new HashMap<String, String>() {{
		put("/Activity Form/Funding/Funding Group/Funding Item/Commitments", ArConstants.COMMITMENT);
		put("/Activity Form/Funding/Funding Group/Funding Item/Disbursements", ArConstants.DISBURSEMENT);
		put("/Activity Form/Funding/Funding Group/Funding Item/Expenditures", ArConstants.EXPENDITURE);
		put("/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Capital Spending Percentage", "Capital");
	}};
	
	@SuppressWarnings("serial")
	private static final Set<String> allPossibleValuesSet = new HashSet<String>() {{
		for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.ADJUSTMENT_TYPE_KEY)) {
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
		//visibleData.addAll(getVisibleByDefault());
		Set<String> invisibleData = new HashSet<String>(getAllData());
		//invisibleData.removeAll(getVisibleByDefault());
		AmpTemplatesVisibility currentTemplate = FeaturesUtil.getCurrentTemplate();

		//check modules
		List<AmpModulesVisibility> modules = FeaturesUtil.getAmpModulesVisibility(getDataMap(DataMapType.MODULES).keySet(), currentTemplate.getId());
		processVisbleObjects(modules, getDataMap(DataMapType.MODULES), visiblePrecursors, invisiblePrecursors);
		//processVisbleObjects(null, getDataMap(DataMapType.DEPENDENCY), visiblePrecursors, invisiblePrecursors);
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
	public static final Map<String, Collection<String>> dependencyMapTypeAll = new HashMap<String, Collection<String>>() {{
		//Collection<AmpCategoryValue> adjustmentTypes = new ArrayList<AmpCategoryValue>(); 
		for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.ADJUSTMENT_TYPE_KEY)) {
			for (final String trType: TRANSACTION_TYPES) {
				put(adj.getValue() + " " + trType, new ArrayList<String>() {{  add(trType);}});
			}
		}
		put(MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL, new ArrayList<String>() {{add("Disbursements"); add("Capital"); add(ADJUSTMENT_PREFIX + "Actual"); }});
		put(MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT, new ArrayList<String>() {{add("Disbursements"); add("Capital"); add(ADJUSTMENT_PREFIX + "Actual"); }});
		put(MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL, new ArrayList<String>() {{add("Disbursements"); add("Capital"); add(ADJUSTMENT_PREFIX + "Planned"); }});
		put(MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE, new ArrayList<String>() {{add("Disbursements"); add("Capital"); add(ADJUSTMENT_PREFIX + "Planned"); }});
		
	}};
	
	protected static final Map<String, String> featuresToMeasuresMap = new HashMap<String, String>();
	
	protected static final Map<String, String> fieldsToMeasuresMap = new HashMap<String, String>();
	protected final static String ADJUSTMENT_PREFIX = "adjustment_type: ";

	@Override
	protected List<String> getVisibleByDefault() {
		List<String> currentlyVisible = new ArrayList<>();
		
		for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.ADJUSTMENT_TYPE_KEY)) {
				currentlyVisible.add(ADJUSTMENT_PREFIX + adj.getValue());
		}
		return currentlyVisible;
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
		return dependencyMapTypeAll;
	}
	
}
