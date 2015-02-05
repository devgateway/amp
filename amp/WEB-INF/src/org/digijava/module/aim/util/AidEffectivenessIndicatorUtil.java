package org.digijava.module.aim.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicatorOption;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

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

}

