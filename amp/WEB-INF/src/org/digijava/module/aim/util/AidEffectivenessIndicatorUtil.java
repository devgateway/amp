package org.digijava.module.aim.util;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpAidEffectivenessIndicator;
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
                .append(" where ampIndicatorName like '%"+ keyword +"%'")
                .append(" and active = :active");

        // the indicator type is selected
        if (indicatorType >= 0) {
            queryBuilder.append(" and indicatorType = :indicatorType");
        }

        query = session.createQuery(queryBuilder.toString());
        query.setBoolean("active", activeOnly);

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
     * @param indicatorId
     */
    public static void deleteIndicator(long indicatorId) {
        Session session = PersistenceManager.getSession();
        // todo handle if not found
        AmpAidEffectivenessIndicator indicator = (AmpAidEffectivenessIndicator) session.load(AmpAidEffectivenessIndicator.class, indicatorId);
        // TODO Handle if there are alreary exist activities with this indicator
        // The deletion is impossible and we should fire corresponding message to the user
        session.delete(indicator);
    }

}

