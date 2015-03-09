package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * List of utility methods to operate with effectiveness indicators
 */
public class AidEffectivenessIndicatorUtil {

    private static Logger logger = Logger.getLogger(AidEffectivenessIndicatorUtil.class);

    /**
     * Returns list of indicators by the criteria
     * @param indicatorType - the indicator type 0 or 1
     * @param keyword - the keyword will be used to search in name
     * @return list of indicators
     */
    public static List<AmpAidEffectivenessIndicator> searchIndicators(
            int indicatorType, String keyword, boolean activeOnly) {

        Session session = null;
        StringBuilder queryBuilder = new StringBuilder();
        Query query = null;

        session = PersistenceManager.getSession();
        queryBuilder.append("select ind from ").append(AmpAidEffectivenessIndicator.class.getName()).append(" ind")
                .append(" where ampIndicatorName like '%"+ keyword +"%'");

        // show active only, otherwise return all
        if (activeOnly) {
            queryBuilder.append(" and active = :active");
        }

        // the indicator type is selected
        if (indicatorType >= 0) {
            queryBuilder.append(" and indicatorType = :indicatorType");
        }

        queryBuilder.append(" order by ampIndicatorId");

        query = session.createQuery(queryBuilder.toString());

        if (activeOnly) {
            query.setBoolean("active", true);
        }
        if (indicatorType >= 0) {
            query.setInteger("indicatorType", indicatorType);
        }

        return  (List<AmpAidEffectivenessIndicator>)query.list();
    }

    /**
     * Loads indicator by primary key from the database
     * @param indicatorId
     * @ throws org.hibernate.ObjectNotFoundException if the object was not found in the database,
     * usually because the id is incorrect
     * @return AmpAidEffectivenessIndicator object, null if object was not found
     */
    public static AmpAidEffectivenessIndicator loadIndicatorById(Long indicatorId) {
        Session session = PersistenceManager.getSession();
        try {
            return (AmpAidEffectivenessIndicator) session.load(AmpAidEffectivenessIndicator.class, indicatorId);
        } catch (ObjectNotFoundException nfe) {
            logger.error(nfe.getStackTrace());
            return null;
        }
    }

    /**
     * Loads indicator option by primary key from the database
     * @param optionId
     * @ throws org.hibernate.ObjectNotFoundException if the object was not found in the database,
     * usually because the id is incorrect
     * @return AmpAidEffectivenessIndicatorOption object, null if object was not found
     */
    public static AmpAidEffectivenessIndicatorOption loadOptionById(Long optionId) {
        Session session = PersistenceManager.getSession();
        try {
            return (AmpAidEffectivenessIndicatorOption) session.load(AmpAidEffectivenessIndicatorOption.class, optionId);
        } catch (ObjectNotFoundException nfe) {
            logger.error(nfe.getStackTrace());
            return null;
        }
    }

    public static void saveIndicator(AmpAidEffectivenessIndicator indicator) {
        Session session = PersistenceManager.getSession();
        session.saveOrUpdate(indicator);
    }

    /**
     * Do not delete via Id, but load first
     * Because we want to delete related options as well
     * @param indicatorId the indicator Id to delete
     * @return returns null if indicator with the id provided was not found in the database
     */
    public static AmpAidEffectivenessIndicator deleteIndicator(long indicatorId) {
        Session session = PersistenceManager.getSession();
        AmpAidEffectivenessIndicator indicator = null;

        indicator = loadIndicatorById(indicatorId);
        if (indicator != null) {
            // these two methods should be executed withing the transaction context
            session.delete(indicator);
            cleanUpModulesVisibility(indicator);
            return indicator;
        } else {
            return null;
        }
    }

    /**
     * Delete option and the relationship with indicator
     * @param optionId the option id to be deleted
     * @return returns the indicator for data be refreshed after deletion
     * returns null if indicator with the id provided was not found in the database
     */
    public static AmpAidEffectivenessIndicator deleteOption(long optionId) {
        Session session = PersistenceManager.getSession();
        AmpAidEffectivenessIndicatorOption option = null;
        try {
            option = (AmpAidEffectivenessIndicatorOption)
                    session.load(AmpAidEffectivenessIndicatorOption.class, optionId);
        } catch (org.hibernate.ObjectNotFoundException nfe) {
            return null;
        }
        AmpAidEffectivenessIndicator indicator = option.getIndicator();
        indicator.getOptions().remove(option);
        session.delete(option);
        return indicator;
    }

    /**
     * Returns all active (visible) indicators for the activity form to display
     * @return indicators with Active = true attribute
     */
    public static List<AmpAidEffectivenessIndicator> getAllActiveIndicators() {
        Session session = PersistenceManager.getSession();
        String queryStr = "select ind from " + AmpAidEffectivenessIndicator.class.getName()
                + " ind where active = true order by ampIndicatorId";
        Query query = session.createQuery(queryStr);
        List<AmpAidEffectivenessIndicator> indicators = (List<AmpAidEffectivenessIndicator>)query.list();
        return indicators;

    }

    /**
     * When indicator is physically deleted we need to clean up the reference in the amp_modules_visibility table
     * To clean up trash in the Global FM tree
     * @param indicator the indicator to clean up for
     */
    public static void cleanUpModulesVisibility(AmpAidEffectivenessIndicator indicator) {
        Session session = PersistenceManager.getSession();

        String queryStr = "delete from amp_modules_templates where module in " +
                "(select id from amp_modules_visibility where name like '%Aid Effectivenes/"
              + indicator.getAmpIndicatorName() + "%') ";
        Query query = session.createSQLQuery(queryStr);
        query.executeUpdate();


        queryStr = "delete from amp_modules_visibility where name like '%Aid Effectivenes/"
                + indicator.getAmpIndicatorName() +"%'";
        query = session.createSQLQuery(queryStr);
        query.executeUpdate();
    }


    public static boolean hasIndicatorActivities(long indicatorId) {
        Session session = PersistenceManager.getSession();
        String queryStr = "select \n" +
                "    count(a.amp_indicator_option_id) \n" +
                "from \n" +
                "    AMP_ACTIVITY_EFFECTIVENESS_INDICATOR_OPTIONS a,\n" +
                "    AMP_AID_EFFECTIVENESS_INDICATOR_OPTION o\t\n" +
                "where \n" +
                "    a.amp_indicator_option_id = o.amp_indicator_option_id\n" +
                "    and o.amp_indicator_id=:indicatorId";

        Query query = session.createSQLQuery(queryStr);
        query.setParameter("indicatorId", indicatorId);

        long count = ((Number) query.uniqueResult()).longValue();
        return count > 0;
    }

    public static boolean hasOptionActivities(long optionId) {
        Session session = PersistenceManager.getSession();
        String queryStr = "select \n" +
                "    count(a.amp_indicator_option_id) \n" +
                "from \n" +
                "    AMP_ACTIVITY_EFFECTIVENESS_INDICATOR_OPTIONS a\n" +
                "where \n" +
                "    a.amp_indicator_option_id=:optionId";

        Query query = session.createSQLQuery(queryStr);
        query.setParameter("optionId", optionId);

        long count = ((Number) query.uniqueResult()).longValue();
        return count > 0;
    }


    /**
     * Returns all options from all Active indicators
     * @return
     */
    public static Map<Long, AmpAidEffectivenessIndicatorOption> getAllOptions() {
        Map<Long, AmpAidEffectivenessIndicatorOption> allOptions = new HashMap<Long, AmpAidEffectivenessIndicatorOption>();
        List<AmpAidEffectivenessIndicator> indicators = getAllActiveIndicators();

        for (AmpAidEffectivenessIndicator indicator : indicators) {

            for (AmpAidEffectivenessIndicatorOption indicatorOption : indicator.getOptions()) {
                allOptions.put(indicatorOption.getAmpIndicatorOptionId(), indicatorOption);
            }
        }

        return allOptions;
    }


    /**
     * Add the configured indicators (actually, the default option of each indicator) to the activity
     * (if it has not been added before)
     *
     * @param activity the activity to add options to
     * @return all options for all indicators as a set
     */
    public static Set<AmpAidEffectivenessIndicatorOption> populateSelectedOptions(AmpActivityVersion activity) {
        List<AmpAidEffectivenessIndicator> indicators = getAllActiveIndicators();
        Set<AmpAidEffectivenessIndicatorOption> optionList = new LinkedHashSet<>();

        if (activity.getSelectedEffectivenessIndicatorOptions() == null) {
            activity.setSelectedEffectivenessIndicatorOptions(new HashSet<AmpAidEffectivenessIndicatorOption>());
        }

        for (AmpAidEffectivenessIndicator indicator : indicators) {

            AmpAidEffectivenessIndicatorOption selectedOption = isIndicatorPresentOnThisActivity(indicator,
                    activity.getSelectedEffectivenessIndicatorOptions());
            // if this indicator has already been presented on the activity and an option was selected
            if (selectedOption != null) {
                optionList.add(selectedOption);
            } else {
                AmpAidEffectivenessIndicatorOption option = new AmpAidEffectivenessIndicatorOption();
                option.setIndicator(indicator);
                optionList.add(option);
            }
        }

        return optionList;
    }

    /**
     * Checks if particular indicator has already been selected/chosen in the activity
     * @param indicator - indicator from the total list of indicators
     * @param selectedOptions - the activity to check
     * @return the option that was selected on the 'Aid Effectiveness' sections
     *         returns null if indicator is not present in activity, or no option is selected
     */
    private static AmpAidEffectivenessIndicatorOption isIndicatorPresentOnThisActivity(AmpAidEffectivenessIndicator indicator,
                                                                                Set<AmpAidEffectivenessIndicatorOption> selectedOptions) {
        if (indicator == null || indicator.getOptions() == null
                || selectedOptions == null
                || selectedOptions.size() == 0) {
            return null;
        }

        // iterate in this way only!!!
        for (AmpAidEffectivenessIndicatorOption selectedOption : selectedOptions) {
            for (AmpAidEffectivenessIndicatorOption indicatorOption : indicator.getOptions()) {
                if (selectedOption.equals(indicatorOption)) {
                    return selectedOption;
                }
            }
        }

        return null;
    }

}