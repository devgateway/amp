package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.validators.ComponentFundingOrgsValidator;
import org.digijava.kernel.ampapi.endpoints.activity.validators.FundingPledgesValidator;
import org.digijava.kernel.ampapi.endpoints.common.field.FieldMap;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceEPConstants;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.interchange.ActivityFieldsConstants;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.FlushMode;
import org.hibernate.Session;

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
    public final static String AGREEMENT_CODE_PRESENT_KEY = "agreement_code_required";
    public final static String AGREEMENT_TITLE_PRESENT_KEY = "agreement_title_required";
    public static final String TRANSACTION_PRESENT_KEY = "transaction_present";
    public static final String ORGANIZATION_PRESENT_KEY = "organization_present";
    public static final String FUNDING_ORGANIZATION_VALID_PRESENT_KEY = "funding_organization_group_valid";

    public static final String RESOURCE_TYPE_FILE_VALID_KEY = "resource_type_file_valid_key";
    public static final String RESOURCE_TYPE_LINK_VALID_KEY = "resource_type_link_valid_key";

    /*
     * End of dependency codes section
     */

    /*paths*/
    private final static String BUDGET_PATH = "activity_budget";
    private final static String IMPLEMENTATION_LEVEL_PATH = "implementation_level";
    private final static String IMPLEMENTATION_LOCATION_PATH = "implementation_location";
    private final static String AGREEMENT_CODE_PATH = "code";
    private final static String AGREEMENT_TITLE_PATH = "title";

    private FMService fmService;

    public InterchangeDependencyResolver(FMService fmService) {
        this.fmService = fmService;
    }

    /**
     * checks whether the field described is present in the full activity
     * @param incomingActivity
     * @param path
     * @return
     */
    private static DependencyCheckResult checkFieldPresent(Map<String, Object> incomingActivity, String path) {
        Object externalValue = ActivityInterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity, path);
        if (externalValue != null)
            return DependencyCheckResult.VALID;
        return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
    }

    /**
     * checks whether the field described is present in the map object
     * @param value
     * @param path
     * @return
     */
    private static DependencyCheckResult checkFieldValuePresent(Object value, String path) {
        if (value instanceof Map && value != null && ((Map<String, Object>)value).get(path) != null)
            return DependencyCheckResult.VALID;
        return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
    }

    /**
     * Performs a check on ids corresponding to AmpLocation objects --
     * whether those are included in the Implementation Level provided by the activity
     * @param e
     * @param incomingActivity
     * @return
     */
    private static DependencyCheckResult checkImplementationLevel(Object e, Map<String, Object> incomingActivity) {
        //this object should be a Number (Long or Integer)
        Object impLevelValue = ActivityInterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity,
                IMPLEMENTATION_LEVEL_PATH);
        if (impLevelValue == null)
            return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
        if (!Number.class.isAssignableFrom(e.getClass()))
            return DependencyCheckResult.INVALID_NOT_CONFIGURABLE; //something's wrong with this item -- it's not an id
        String queryString = "select loc from " + AmpLocation.class.getName() + " loc where loc.ampLocationId in (" + ((Number)e).longValue() + ")";

        List<Object> objectList = getSessionWithPendingChanges().createQuery(queryString).list();
        if (objectList.size() != 1)
            return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
        /*this location id isn't found in the list of possible values,
            a different validator should have marked it already as invalid*/
        AmpLocation loc = (AmpLocation)objectList.get(0);
        Collection<AmpCategoryValue> usedVals = loc.getLocation().getParentCategoryValue().getUsedValues();
        for (AmpCategoryValue acv : usedVals) {

            Long acvId = acv.getId();
            Number extId = (Number) impLevelValue;
            if (acvId.equals(extId.longValue()) )
                return DependencyCheckResult.VALID;
        }
        return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
    }

    /**
     * checks whether the specified dependency code is fulfilled in regards to the activity
     * @param value
     * @param importer
     * @param code
     * @return
     */
    public static DependencyCheckResult checkDependency(Object value, ObjectImporter importer, String code) {

        Map<String, Object> incomingActivity = importer.getNewJson();

        switch (code) {
        case IMPLEMENTATION_LEVEL_PRESENT_KEY: return checkFieldPresent(incomingActivity, IMPLEMENTATION_LEVEL_PATH);
        case IMPLEMENTATION_LOCATION_PRESENT_KEY: return checkFieldPresent(incomingActivity, IMPLEMENTATION_LOCATION_PATH);
        case AGREEMENT_CODE_PRESENT_KEY : return checkFieldValuePresent(value, AGREEMENT_CODE_PATH);
        case AGREEMENT_TITLE_PRESENT_KEY : return checkFieldValuePresent(value, AGREEMENT_TITLE_PATH);
        case IMPLEMENTATION_LEVEL_VALID_KEY: return checkImplementationLevel(value, incomingActivity);
        case IMPLEMENTATION_LOCATION_VALID_KEY: return checkImplementationLocation(value, incomingActivity);
        case ORGANIZATION_PRESENT_KEY:
            return checkComponentFundingOrg(value, incomingActivity);
        case FUNDING_ORGANIZATION_VALID_PRESENT_KEY:
            return checkFundingPledgesOrgGroup(importer, value);
        case ON_BUDGET_KEY:
        case TRANSACTION_PRESENT_KEY:
            return DependencyCheckResult.VALID;

        default: throw new RuntimeException("Interchange Dependency Mapper: no dependency found for code " + code);
        }
    }

    /**
     * Checks if required dependency is fullfilled
     *
     * @param value
     * @param importer
     * @param fieldDescription
     * @param fieldParent
     * @return
     */
    public static boolean checkRequiredDependencyFulfilled(Object value, ObjectImporter importer,
            APIField fieldDescription, Map<String, Object> fieldParent) {

        List<String> deps = fieldDescription.getDependencies();
        boolean result = true;
        if (deps != null) {
            for (String dep : deps) {
                switch (dep) {
                    case ON_BUDGET_KEY:
                        result = result && isOnBudget(value, importer, fieldDescription);
                        break;
                    case TRANSACTION_PRESENT_KEY:
                        result = result && hasTransactions(fieldParent);
                        break;
                    case RESOURCE_TYPE_FILE_VALID_KEY:
                        result = result && isResourceTypeValid(fieldParent, ResourceType.FILE);
                        break;
                    case RESOURCE_TYPE_LINK_VALID_KEY:
                        result = result && isResourceTypeValid(fieldParent, ResourceType.LINK);
                        break;
                    default: 
                        break;
                }
            }
        }

        return result;
    }

    /**
     * check if activity is on budget or not
     *
     * @return
     */
    private static boolean isOnBudget(Object checkedValue, ObjectImporter importer, APIField fieldDescription) {

        Map<String, Object> incomingActivity = importer.getNewJson();

        Object referenceOnBudgetValue = getOnBudgetValue();
        Object onOffBudgetValue = ActivityInterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity,
                BUDGET_PATH);
        if (onOffBudgetValue != null) {
            if (Number.class.isAssignableFrom(onOffBudgetValue.getClass())) {
                onOffBudgetValue = ((Number) onOffBudgetValue).longValue();
                referenceOnBudgetValue = ((Number) referenceOnBudgetValue).longValue();
            }
        }

        return referenceOnBudgetValue.equals(onOffBudgetValue);
    }

    private static boolean isResourceTypeValid(Map<String, Object> fieldParent, ResourceType resourceType) {

        Object resType = fieldParent.get(FieldMap.underscorify(ResourceEPConstants.RESOURCE_TYPE));
        return resType != null && resType.equals(resourceType.getId());
    }


    /**
     * check if fieldParent has funding details (transactions)
     *
     * @param fieldParent
     * @return
     */
    private static boolean hasTransactions(Map<String, Object> fieldParent) {

        int transactionsCount = ActivityEPConstants.TRANSACTION_FIELD_NAMES.stream()
                .mapToInt(transactionName -> getCollectionSize(fieldParent, transactionName))
                .sum();

        return transactionsCount > 0;
    }

    /**
     * checks whether the implementation location (provided as Object e)
     * is corresponding to the value from implementation level category value
     * @param e the implementation location category value
     * @param incomingActivity
     * @return
     */
    private static DependencyCheckResult checkImplementationLocation(Object e, Map<String, Object> incomingActivity) {
        //this object should be a Number (Long or Integer)
        Object externalValue = ActivityInterchangeUtils.getFieldValuesFromJsonActivity(incomingActivity,
                IMPLEMENTATION_LEVEL_PATH);
        if (e == null) {
            if (externalValue == null) {
                return DependencyCheckResult.VALID;
            }
            int locationsCount = getCollectionSize(incomingActivity, ActivityFieldsConstants.LOCATIONS);
            if (locationsCount == 0) {
                return DependencyCheckResult.VALID;
            }
        }

        if (externalValue == null || !Number.class.isAssignableFrom(externalValue.getClass()))
            return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
        if (!Number.class.isAssignableFrom(e.getClass()))
            return DependencyCheckResult.INVALID_NOT_CONFIGURABLE; //something's wrong with this item -- it's not an id
        String queryString = "select loc from " + AmpCategoryValue.class.getName() + " loc where loc.id in (" + ((Number)e).longValue() + ")";

        List<Object> objectList = getSessionWithPendingChanges().createQuery(queryString).list();
        if (objectList.size() != 1)
            return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
        /*this implementation location id isn't found in the list of possible values,
            a different validator should have marked it already as invalid*/
        AmpCategoryValue loc = (AmpCategoryValue)objectList.get(0);
        Collection<AmpCategoryValue> usedVals = loc.getUsedValues();
        for (AmpCategoryValue acv : usedVals) {
            Long acvId = acv.getId();
            Number extId = (Number) externalValue;
            if (acvId.equals(extId.longValue()) )
                return DependencyCheckResult.VALID;
        }
        return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
    }

    public static Object getOnBudgetValue() {
        return CategoryConstants.ACTIVITY_BUDGET_ON.getAmpCategoryValueFromDB().getIdentifier();
    }

    private static int getCollectionSize(Map<String, Object> fieldParent, String fieldName) {
        Object collection = fieldParent.get(FieldMap.underscorify(fieldName));
        if (collection != null && Collection.class.isAssignableFrom(collection.getClass())) {
            return ((Collection<?>) collection).size();
        }
        return 0;
    }

    /**
     * Performs a check on component funding org id corresponding to AmpOrganization objects --
     * whether those are included in the related organizations
     * @param e
     * @param incomingActivity
     * @return
     */

    private static DependencyCheckResult checkComponentFundingOrg(Object e, Map<String, Object> incomingActivity) {
        ComponentFundingOrgsValidator validator = new ComponentFundingOrgsValidator();
        if (validator.isValid(incomingActivity, e)) {
            return DependencyCheckResult.VALID;
        }

        return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
    }

    /**
     * Performs a check on funding pledges org gropu id corresponding to org group of donor organization object --
     * whether those is present in the parent funding
     *
     * @param importer
     * @param e
     * @return
     */
    private static DependencyCheckResult checkFundingPledgesOrgGroup(ObjectImporter importer, Object e) {

        FundingPledgesValidator validator = new FundingPledgesValidator();

        if (validator.isPledgeValid(importer, e)) {
            return DependencyCheckResult.VALID;
        }

        return DependencyCheckResult.INVALID_NOT_CONFIGURABLE;
    }

    /**
     * Verifies each configures dependency for any additional checks
     * and builds up the final (actual) list of dependencies
     * @param dependecies
     * @return actual dependencies list or null if no dependency
     */
    public List<String> getActualDependencies(String[] dependecies) {
        if (dependecies == null || dependecies.length == 0)
            return null;
        List<String> actualDependecies = new ArrayList<String>();
        for(String dependency : dependecies) {
            if (StringUtils.isNotBlank(dependency)) {
                actualDependecies.add(dependency);
            }
        }
        return actualDependecies.size() > 0 ? actualDependecies : null;
    }

}
