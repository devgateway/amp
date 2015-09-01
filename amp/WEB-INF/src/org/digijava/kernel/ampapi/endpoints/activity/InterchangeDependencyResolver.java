package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.discriminators.TransactionTypeDiscriminator;
import org.digijava.kernel.ampapi.endpoints.activity.visibility.FMVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.FlushMode;
import org.hibernate.Session;

import clover.org.apache.commons.lang.StringUtils;

/**
 * 
 * @author acartaleanu
 *
 */
public class InterchangeDependencyResolver {
	
	/**
	 * This is a special adjusted Session with FlusMode = Commit so that Hiberante doesn't try to commit intermediate 
	 * changes while we still query some information
	 * TODO: AMP-20869: we'll need to give it a more thought during refactoring if either rewrite related queries as JDBC queries
	 * or investigate more for 
	 * 
	 * @return Session with no AutoFlush mode
	 */
	public static Session getSessionWithPendingChanges() {
		Session session = PersistenceManager.getSession();
		session.setFlushMode(FlushMode.COMMIT);
		return session;
	}
	/*
	 * Dependency codes section
	 */
	public final static String ON_BUDGET_KEY = "on_budget";
	public final static String IMPLEMENTATION_LEVEL_PRESENT_KEY = "implementation_level_present";
	public final static String IMPLEMENTATION_LOCATION_PRESENT_KEY = "implementation_location_present";
	public final static String IMPLEMENTATION_LEVEL_VALID_KEY = "implementation_level_valid";
	public final static String IMPLEMENTATION_LOCATION_VALID_KEY = "implementation_location_valid";
	public final static String COMMITMENTS_OR_DISBURSEMENTS_PRESENT_KEY = "funding_type_commitment_or_disbursements_present";
	public final static String DISBURSEMENTS_PRESENT_KEY = "funding_type_disbursements_present";
	public final static String COMMITMENTS_DISASTER_RESPONSE_REQUIRED = "commitments_disaster_response_required";
	public final static String DSIBURSEMENTS_DISASTER_RESPONSE_REQUIRED = "disbursements_disaster_response_required";
	
	private final static String BUDGET_PATH = "activity_budget";
	private final static String IMPLEMENTATION_LEVEL_PATH = "implementation_level";
	private final static String IMPLEMENTATION_LOCATION_PATH = "implementation_location";
	
	/*
	 * End of dependency codes section
	 */
	
	/**
	 * static constructor to init paths and values
	 */
	static{
		codeToPath = new HashMap<String, String>();
	}
	
	
	/**
	 * Dependency code to dependency path (like: sectors~sector_id)
	 */
	private static Map<String, String> codeToPath;
	
	/**
	 * Dependency code to dependency object value
	 */
	
	/**
	 * 
	 * @param code the code from the "Dependency codes section" of 
	 * <InterchangeDependencyMapper> 
	 * @return path as String (like: "sectors~sector_id")
	 */
	public static String getPath(String code) {
		return codeToPath.get(code);
	}

	
	/**
	 * Checks whether the "activity budget" category value is equal to "on budget" 
	 * @param incomingActivity The full JsonBean describing the activity to be imported
	 * @return true if it's valid, false if it isn't
	 */
	private static boolean checkOnBudget(JsonBean incomingActivity) {
		Object externalValue = InterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity, BUDGET_PATH);
		if (externalValue == null)
			return false;
		Object value = getOnBudgetValue();
		if (Number.class.isAssignableFrom(externalValue.getClass())) {
			if (externalValue != null)
				externalValue = ((Number)externalValue).longValue();
			value = ((Number)value).longValue();
		}
		return externalValue.equals(value);
	}
	
	/**
	 * checks whether the field described is present in the full activity
	 * @param incomingActivity
	 * @param path
	 * @return
	 */
	private static boolean checkFieldPresent(JsonBean incomingActivity, String path) {
		Object externalValue = InterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity, path);
		return externalValue != null;
	}
	
	/**
	 * Performs a check on ids corresponding to AmpLocation objects -- 
	 * whether those are included in the Implementation Level provided by the activity
	 * @param e
	 * @param incomingActivity
	 * @return
	 */
	private static boolean checkImplementationLevel(Object e, JsonBean incomingActivity) {
		//this object should be a Number (Long or Integer)
		Object externalValue = InterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity, IMPLEMENTATION_LEVEL_PATH);
		if (externalValue == null)
			return false;
		if (!Number.class.isAssignableFrom(e.getClass()))
			return false; //something's wrong with this item -- it's not an id
		String queryString = "select loc from " + AmpLocation.class.getName() + " loc where loc.ampLocationId in (" + ((Number)e).longValue() + ")";
		
		List<Object> objectList = getSessionWithPendingChanges().createQuery(queryString).list();
		if (objectList.size() != 1)
			return false; /*this location id isn't found in the list of possible values, 
			a different validator should have marked it already as invalid*/
		AmpLocation loc = (AmpLocation)objectList.get(0);
		Collection<AmpCategoryValue> usedVals = loc.getLocation().getParentCategoryValue().getUsedValues();
		for (AmpCategoryValue acv : usedVals) {
			
			Long acvId = acv.getId();
			Number extId = (Number) externalValue;
			if (acvId.equals(extId.longValue()) )
				return true;
		}
		return false;
	}
	
	/**
	 * checks whether the specified dependency code is fulfilled in regards to the activity
	 * @param value
	 * @param incomingActivity
	 * @param code
	 * @return
	 */
	public static boolean checkDependency(Object value, JsonBean incomingActivity, String code, 
			Map<String, Object> fieldParent) {
		switch (code) {
		case ON_BUDGET_KEY: return checkOnBudget(incomingActivity);
		case IMPLEMENTATION_LEVEL_PRESENT_KEY: return checkFieldPresent(incomingActivity, IMPLEMENTATION_LEVEL_PATH);
		case IMPLEMENTATION_LOCATION_PRESENT_KEY: return checkFieldPresent(incomingActivity, IMPLEMENTATION_LOCATION_PATH);
		case IMPLEMENTATION_LEVEL_VALID_KEY: return checkImplementationLevel(value, incomingActivity);
		case IMPLEMENTATION_LOCATION_VALID_KEY: return checkImplementationLocation(value, incomingActivity);
		case COMMITMENTS_OR_DISBURSEMENTS_PRESENT_KEY: return checkCommitmentDisbursement(value, incomingActivity, fieldParent);
		case COMMITMENTS_DISASTER_RESPONSE_REQUIRED:
			boolean isCommitment = checkTransactionType(value, incomingActivity, fieldParent, Constants.COMMITMENT);
			return !isCommitment || isCommitment && value != null;
		case DSIBURSEMENTS_DISASTER_RESPONSE_REQUIRED:
			boolean isDisbursement = checkTransactionType(value, incomingActivity, fieldParent, Constants.DISBURSEMENT);
			return !isDisbursement || isDisbursement && value != null;
		
		default: throw new RuntimeException("Interchange Dependency Mapper: no dependency found for code " + code);
		}
	}

	/**
	 * checks whether the implementation location (provided as Object e) 
	 * is corresponding to the value from implementation level category value  
	 * @param e the implementation location category value
	 * @param incomingActivity 
	 * @return
	 */
	private static boolean checkImplementationLocation(Object e, JsonBean incomingActivity) {
		//this object should be a Number (Long or Integer)
		Object externalValue = InterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity, IMPLEMENTATION_LEVEL_PATH);
		
		if (externalValue == null || !Number.class.isAssignableFrom(externalValue.getClass()))
			return false;
		if (!Number.class.isAssignableFrom(e.getClass()))
			return false; //something's wrong with this item -- it's not an id
		String queryString = "select loc from " + AmpCategoryValue.class.getName() + " loc where loc.id in (" + ((Number)e).longValue() + ")";
		
		List<Object> objectList = getSessionWithPendingChanges().createQuery(queryString).list();
		if (objectList.size() != 1)
			return false; /*this implementation location id isn't found in the list of possible values, 
			a different validator should have marked it already as invalid*/
		AmpCategoryValue loc = (AmpCategoryValue)objectList.get(0);
		Collection<AmpCategoryValue> usedVals = loc.getUsedValues();
		for (AmpCategoryValue acv : usedVals) {
			Long acvId = acv.getId();
			Number extId = (Number) externalValue;
			if (acvId.equals(extId.longValue()) )
				return true;
		}
		return false;
	}

	private static Object getOnBudgetValue() {
		return CategoryConstants.ACTIVITY_BUDGET_ON.getAmpCategoryValueFromDB().getIdentifier();
	}
	
	private static boolean checkCommitmentDisbursement(Object e, JsonBean incomingActivity, Map<String, Object> fieldParent) {
		Object trnType = getTransactionType(e, incomingActivity, fieldParent);
		return trnType != null && (trnType.equals(Constants.COMMITMENT) || trnType.equals(Constants.DISBURSEMENT));
	}
	
	private static boolean checkTransactionType(Object e, JsonBean incomingActivity, Map<String, Object> fieldParent, int trnCode) {
		Object trnType = getTransactionType(e, incomingActivity, fieldParent);
		return trnType != null && trnType.equals(trnCode);
	}
	
	private static Object getTransactionType(Object e, JsonBean incomingActivity, Map<String, Object> fieldParent) {
		return fieldParent.get(InterchangeUtils.underscorify(ActivityFieldsConstants.TRANSACTION_TYPE));
	}
	
	/**
	 * Does any other custom checks for dependency key to be displayed or not
	 * @param dependencyKey
	 * @return this or any other dependency key or null if no dependency key should be actually considered
	 */
	public static String getActualDependency(String dependencyKey) {
		// verify any custom keys processing
		switch (dependencyKey) {
		case COMMITMENTS_DISASTER_RESPONSE_REQUIRED:
			// do not mention commitments dependency key required setting if not enabled
			if (!FMVisibility.isVisible("/Activity Form/Funding/Funding Group/Funding Item/Commitments/Commitments Table/Required Validator for Disaster Response", null))
				return null;
			break;
		case DSIBURSEMENTS_DISASTER_RESPONSE_REQUIRED:
			// do not mention disbursements dependency key required setting if not enabled
			if (!FMVisibility.isVisible("/Activity Form/Funding/Funding Group/Funding Item/Disbursements/Disbursements Table/Required Validator for Disaster Response", null))
				return null;
			break;
		}
		// provide back the dependency key if no changes detected so far 
		return dependencyKey;
	}
	
	/**
	 * Verifies each configures dependency for any additional checks and builds up the final (actual) list of dependencies
	 * @param dependecies
	 * @return actual dependencies list or null if no dependency
	 */
	public static List<String> getActualDependencies(String[] dependecies) {
		if (dependecies == null || dependecies.length == 0)
			return null;
		List<String> actualDependecies = new ArrayList<String>();
		for(String dependency : dependecies) {
			dependency = getActualDependency(dependency);
			if (StringUtils.isNotBlank(dependency)) {
				actualDependecies.add(dependency);
			}
		}
		return actualDependecies.size() > 0 ? actualDependecies : null;
	}
}
