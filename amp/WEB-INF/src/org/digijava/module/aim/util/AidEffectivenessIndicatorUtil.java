package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.type.StringType;

/**
 * List of utility methods to operate with effectiveness indicators
 */
public class AidEffectivenessIndicatorUtil {

    private static Logger logger = Logger.getLogger(AidEffectivenessIndicatorUtil.class);
    private static final String AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX = "/Activity Form/Aid Effectivenes/";

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
                .append(" where LOWER (ampIndicatorName) like :keyword");

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
        query.setParameter("keyword", "%" + keyword + "%");

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
        //String tooltip = InternationalizedModelDescription.getForProperty(AmpAidEffectivenessIndicator.class, "tooltipText").getSQLFunctionCall("ind.tooltip_text");
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
                "(select id from amp_modules_visibility where name = :name)";
        Query query = session.createNativeQuery(queryStr);
        query.setString("name", AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX + indicator.getFmName());
        query.executeUpdate();


        queryStr = "delete from amp_modules_visibility where name = :name";
        query = session.createNativeQuery(queryStr);
        query.setString("name", AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX + indicator.getFmName());
        query.executeUpdate();
    }

    /**
     * If indicator gets activated we need to "return it back" (create) to the Global FM tree
     * @param indicatorName
     */
    public static void createModulesVisibility(String indicatorName) {
        Session session = PersistenceManager.getSession();
        String queryStr = "select count(*) from amp_modules_visibility where lower (name) = :name";
        Query query = session.createNativeQuery(queryStr);
        query.setString("name", (AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX + indicatorName).toLowerCase());
        int count = Integer.parseInt(query.uniqueResult().toString());

        // if indicator with this name has not been found, we create one
        if (count == 0) {
            queryStr = "select parent from amp_modules_visibility values where name like '"
                    + AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX + "%'";
            query = session.createNativeQuery(queryStr);
            query.setMaxResults(1);
            List resultList = query.list();
            // if parent exists
            if (resultList != null && resultList.size() > 0) {
                queryStr = "insert into amp_modules_visibility values(nextval('amp_modules_visibility_seq'), :name, '', 't', :parent)";
                query = session.createNativeQuery(queryStr);
                query.setString("name", AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX + indicatorName);
                query.setLong("parent", Long.parseLong(resultList.get(0).toString()));
                query.executeUpdate();
            }
        }
    }

    /**
     * When indicator name is changed, the name in Global FM tree should be changed respectively
     * @param indicator
     * @param oldIndicatorName the name in FM tree to update
     */
    public static void updateModulesVisibility(AmpAidEffectivenessIndicator indicator, String oldIndicatorName) {
        Session session = PersistenceManager.getSession();

        String queryStr = "update amp_modules_visibility set name = :name where name = :oldName";
        Query query = session.createNativeQuery(queryStr);
        query.setParameter("name", AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX +  indicator.getFmName(), StringType.INSTANCE);
        query.setParameter("oldName", AID_EFFECTIVENESS_INDICATOR_VISIBILITY_PREFIX + oldIndicatorName, StringType.INSTANCE);
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

        Query query = session.createNativeQuery(queryStr);
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

        Query query = session.createNativeQuery(queryStr);
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

    /**
     * Sorts the HashSet of options assigned to the activity, replacing it with the sorted LinkedHashSet
     * The options are sorted by the ids of the related indicators
     * @param activity
     */
    public static void sortSelectedEffectivenessOptions(AmpActivityVersion activity) {
        List<AmpAidEffectivenessIndicatorOption> options = new ArrayList<>();
        if (activity.getSelectedEffectivenessIndicatorOptions() == null
                || activity.getSelectedEffectivenessIndicatorOptions().size() == 0) {
            return;
        }
        options.addAll(activity.getSelectedEffectivenessIndicatorOptions());
        Collections.sort(options, new Comparator<AmpAidEffectivenessIndicatorOption>() {
            @Override
            public int compare(AmpAidEffectivenessIndicatorOption o1, AmpAidEffectivenessIndicatorOption o2) {
                if (o1 == null || o2 == null || o1.getIndicator() == null || o2.getIndicator() == null) {
                    return 0;
                } else {
                    return (int)(o1.getIndicator().getAmpIndicatorId() - o2.getIndicator().getAmpIndicatorId());
                }
            }
        });
        Set<AmpAidEffectivenessIndicatorOption> optionsSet = new LinkedHashSet<>();
        optionsSet.addAll(options);
        activity.setSelectedEffectivenessIndicatorOptions(optionsSet);
    }

}
