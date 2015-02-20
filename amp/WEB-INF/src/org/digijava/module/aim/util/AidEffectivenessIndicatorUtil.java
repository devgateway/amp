package org.digijava.module.aim.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * List of utility methods to operate with effectiveness indicators
 */
public class AidEffectivenessIndicatorUtil {

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

        query = session.createQuery(queryBuilder.toString());

        if (activeOnly) {
            query.setBoolean("active", true);
        }
        if (indicatorType >= 0) {
            query.setInteger("indicatorType", indicatorType);
        }

        return  (List<AmpAidEffectivenessIndicator>)query.list();
    }

    public static AmpAidEffectivenessIndicator loadById(Long indicatorId) {
        Session session = PersistenceManager.getSession();
        return (AmpAidEffectivenessIndicator) session.load(AmpAidEffectivenessIndicator.class, indicatorId);
    }

    public static void saveIndicator(AmpAidEffectivenessIndicator indicator) {
        Session session = PersistenceManager.getSession();
        session.saveOrUpdate(indicator);
    }

    /**
     * Do not delete via Id, but load first
     * Because we want to delete related options as well
     * @param indicatorId the indicator Id to delete
     */
    public static void deleteIndicator(long indicatorId) {
        Session session = PersistenceManager.getSession();
        AmpAidEffectivenessIndicator indicator = null;
        try {
            indicator = (AmpAidEffectivenessIndicator) session.load(AmpAidEffectivenessIndicator.class, indicatorId);
        }  catch (org.hibernate.ObjectNotFoundException nfe) {
            /*
             * Throwing Runtime here to prevent handling Hibernate exceptions in the action class
             * I do not want to create business level exception for this
             */
            throw new RuntimeException("Indicator with id " + indicatorId + " not found");
        }
        // TODO Handle if there are alreary exist activities with this indicator
        // The deletion is impossible and we should fire corresponding message to the user
        session.delete(indicator);
    }

    /**
     * Delete option and the relationship with indicator
     * @param optionId the option id to be deleted
     * @return returns the indicator for data be refreshed after deletion
     */
    public static AmpAidEffectivenessIndicator deleteOption(long optionId) {
        Session session = PersistenceManager.getSession();
        AmpAidEffectivenessIndicatorOption option = null;
        try {
            option = (AmpAidEffectivenessIndicatorOption)
                    session.load(AmpAidEffectivenessIndicatorOption.class, optionId);
        } catch (org.hibernate.ObjectNotFoundException nfe) {
            /*
             * Throwing Runtime here to prevent handling Hibernate exceptions in the action class
             * I do not want to create business level exception for this
             */
            throw new RuntimeException("Option with id " + optionId + " not found");
        }
        AmpAidEffectivenessIndicator indicator = option.getIndicator();
        indicator.getOptions().remove(option);
        session.delete(option);
        return indicator;

        /*
        Session session = PersistenceManager.getSession();
        String queryString = "delete from " + AmpAidEffectivenessIndicatorOption.class.getName() + " where ampIndicatorOptionId =:ampIndicatorOptionId";
        Query query = session.createQuery(queryString);
        query.setLong("ampIndicatorOptionId", optionId);
        query.executeUpdate();
        */
    }

    /**
     * Returns all active (visible) indicators for the activity form to display
     * @return
     */
    public static List<AmpAidEffectivenessIndicator> getAllActiveIndicators() {
        Session session = PersistenceManager.getSession();
        String queryStr = "select ind from " + AmpAidEffectivenessIndicator.class.getName() + " ind where active = true";
        Query query = session.createQuery(queryStr.toString());
        return  (List<AmpAidEffectivenessIndicator>)query.list();

    }



    /**
     * Add the configured indicators (actually, the default option of each indicator) to the activity
     * (if it has not been added before)
     *
     * @param activity the activity to add options to
     * @return all options for all indicators as a set
     */
    public static Map<Long, AmpAidEffectivenessIndicatorOption> populateSelectedOptions(AmpActivityVersion activity) {
        List<AmpAidEffectivenessIndicator> indicators = getAllActiveIndicators();

        Map<Long, AmpAidEffectivenessIndicatorOption> allOptions = new HashMap<Long, AmpAidEffectivenessIndicatorOption>();

        if (activity.getSelectedEffectivenessIndicatorOptions() == null) {
            activity.setSelectedEffectivenessIndicatorOptions(new HashSet<AmpAidEffectivenessIndicatorOption>());
        }
        Set<AmpAidEffectivenessIndicatorOption> selectedOptions = new HashSet<AmpAidEffectivenessIndicatorOption>();
        selectedOptions.addAll(activity.getSelectedEffectivenessIndicatorOptions());
        activity.getSelectedEffectivenessIndicatorOptions().clear();


        for (AmpAidEffectivenessIndicator indicator : indicators) {

            for (AmpAidEffectivenessIndicatorOption indicatorOption : indicator.getOptions()) {
                allOptions.put(indicatorOption.getAmpIndicatorOptionId(), indicatorOption);
            }

            AmpAidEffectivenessIndicatorOption selectedOption = isIndicatorPresentOnThisActivity(indicator, selectedOptions);
            // if this indicator has already been presented on the activity and an option was selected
            if (selectedOption != null) {
                /*AmpAidEffectivenessIndicatorOption selOpt = new AmpAidEffectivenessIndicatorOption();
                selOpt.setAmpIndicatorOptionId(selectedOption.getAmpIndicatorOptionId());*/
                activity.getSelectedEffectivenessIndicatorOptions().add(selectedOption);
            } else { // otherwise add the default option
                AmpAidEffectivenessIndicatorOption defaultOption = indicator.getDefaultOption();
                /*AmpAidEffectivenessIndicatorOption defOpt = new AmpAidEffectivenessIndicatorOption();
                defOpt.setAmpIndicatorOptionId(defaultOption.getAmpIndicatorOptionId());*/
                activity.getSelectedEffectivenessIndicatorOptions().add(defaultOption);
            }
        }

        /*
        // remove inactive options
        Set<AmpAidEffectivenessIndicatorOption> selectedOptions = activity.getSelectedEffectivenessIndicatorOptions();
        for (AmpAidEffectivenessIndicatorOption option : selectedOptions) {
            if (!option.getIndicator().getActive()) {
                selectedOptions.remove(option);
            }
        }
        */

        return allOptions;
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
        Session session = PersistenceManager.getSession();
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

