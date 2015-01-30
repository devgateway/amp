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
     * @param indicatorType
     * @param keyword
     * @return
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
}

