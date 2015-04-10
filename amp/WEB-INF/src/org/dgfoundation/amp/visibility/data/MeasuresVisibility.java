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

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.visibility.data.DataVisibility.DataMapType;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * Detects which measures are visible in Activity Form and can be further used, 
 * e.g. in report generation & filtering 
 * @author Alexandru Cartaleanu
 */
public class MeasuresVisibility extends DataVisibility implements FMSettings {
	protected static final Logger logger = Logger.getLogger(MeasuresVisibility.class);	

	@SuppressWarnings("serial")
	protected static final Map<String, String> modulesToMeasuresMap = new HashMap<String, String>() {{
		for(String transactionName:ArConstants.TRANSACTION_TYPE_NAME_TO_ID.keySet())
			put("/Activity Form/Funding/Funding Group/Funding Item/" + transactionName, transactionName);
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
		processVisbleObjects(modules, getDataMap(DataMapType.MODULES), visiblePrecursors, invisiblePrecursors);
		
		List<AmpFeaturesVisibility> features = FeaturesUtil.getAmpFeaturesVisibility(getDataMap(DataMapType.FEATURES).keySet(), currentTemplate.getId());
		processVisbleObjects(features, getDataMap(DataMapType.FEATURES), visibleData, invisibleData);
		
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
		return FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MEASURES);
	}

	protected MeasuresVisibility() {
	}
	
	@Override
	public Set<String> getEnabledSettings() {
		return getCurrentVisibleData();
	}
	
	@SuppressWarnings("serial")
	public static final Map<String, Collection<String>> dependencyMapTypeAll = new HashMap<String, Collection<String>>() {{
		//Collection<AmpCategoryValue> adjustmentTypes = new ArrayList<AmpCategoryValue>(); 
		for (AmpCategoryValue adj: CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.ADJUSTMENT_TYPE_KEY)) {
			for (final String trType: ArConstants.TRANSACTION_TYPE_NAME_TO_ID.keySet()) {
				put(adj.getValue() + " " + trType, new ArrayList<String>() {{  add(trType);}});
			}
		}
		put(MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL, new ArrayList<String>() {{     add(ArConstants.DISBURSEMENT); add("Capital"); add(ADJUSTMENT_PREFIX + "Actual"); }});
		put(MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT, new ArrayList<String>() {{   add(ArConstants.DISBURSEMENT); add("Capital"); add(ADJUSTMENT_PREFIX + "Actual"); }});
		put(MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL, new ArrayList<String>() {{    add(ArConstants.DISBURSEMENT); add("Capital"); add(ADJUSTMENT_PREFIX + "Planned"); }});
		put(MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE, new ArrayList<String>(){{ add(ArConstants.DISBURSEMENT); add("Capital"); add(ADJUSTMENT_PREFIX + "Planned"); }});
		
	}};
	
	@SuppressWarnings("serial")
	protected static final Map<String, String> featuresToMeasuresMap = new HashMap<String, String>() {{
		//Add measures listed under measures features in the feature manager Reporting Section
			put(MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT, MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT);
			put(MeasureConstants.BILATERAL_SSC_COMMITMENTS, MeasureConstants.BILATERAL_SSC_COMMITMENTS);
			put(MeasureConstants.CONSUMPTION_RATE, MeasureConstants.CONSUMPTION_RATE);
			put(MeasureConstants.CUMULATED_DISBURSEMENTS, MeasureConstants.CUMULATED_DISBURSEMENTS);
			put(MeasureConstants.DISBURSMENT_RATIO, MeasureConstants.DISBURSMENT_RATIO);
			put(MeasureConstants.EXECUTION_RATE, MeasureConstants.EXECUTION_RATE);
			put(MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS, MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS);
			put(MeasureConstants.PIPELINE_COMMITMENTS, MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS);
			put(MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL, MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL);
			put(MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE, MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE);
			put(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS, MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS);
			put(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS, MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS);
			put(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS,MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS);
			put(MeasureConstants.TOTAL_COMMITMENTS, MeasureConstants.TOTAL_COMMITMENTS);
			put(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS, MeasureConstants.TRIANGULAR_SSC_COMMITMENTS);
			put(MeasureConstants.UNCOMMITTED_BALANCE, MeasureConstants.UNCOMMITTED_BALANCE);
			put(MeasureConstants.UNDISBURSED_BALANCE, MeasureConstants.UNDISBURSED_BALANCE);
			put(MeasureConstants.REAL_DISBURSEMENTS, MeasureConstants.REAL_DISBURSEMENTS);
			put(MeasureConstants.REAL_COMMITMENTS, MeasureConstants.REAL_COMMITMENTS);
			put(MeasureConstants.REAL_MTFS, MeasureConstants.REAL_MTFS);
			
			put(MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS, MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS);
			put(MeasureConstants.PIPELINE_RELEASE_OF_FUNDS, MeasureConstants.PIPELINE_RELEASE_OF_FUNDS);
			put(MeasureConstants.PLEDGES_ACTUAL_COMMITMENTS, MeasureConstants.PLEDGES_ACTUAL_COMMITMENTS);
			put(MeasureConstants.PLEDGES_ACTUAL_DISBURSEMENTS, MeasureConstants.PLEDGES_ACTUAL_DISBURSEMENTS);
			put(MeasureConstants.PLEDGES_ACTUAL_PLEDGE, MeasureConstants.PLEDGES_ACTUAL_PLEDGE);
			put(MeasureConstants.PLEDGES_COMMITMENT_GAP, MeasureConstants.PLEDGES_COMMITMENT_GAP);
			put(MeasureConstants.PLEDGES_PERCENTAGE_OF_DISBURSEMENT,MeasureConstants.PLEDGES_PERCENTAGE_OF_DISBURSEMENT);
			put(MeasureConstants.PLEDGES_PLANNED_COMMITMENTS, MeasureConstants.PLEDGES_PLANNED_COMMITMENTS);
			put(MeasureConstants.PLEDGES_PLANNED_DISBURSEMENTS, MeasureConstants.PLEDGES_PLANNED_DISBURSEMENTS);
		
	}};
	
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
