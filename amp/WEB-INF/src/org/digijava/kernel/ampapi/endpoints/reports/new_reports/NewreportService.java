package org.digijava.kernel.ampapi.endpoints.reports.new_reports;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class NewreportService {
private static final Logger logger  = LoggerFactory.getLogger(NewreportService.class);
    private static final String QUERY =
            "SELECT cv.id AS core_type_id, " +
                    "       cv.category_value AS core_type_name, " +
                    "       al.location_id AS country_id, " +
                    "       cvl.location_name AS country_name, " +
                    "       org.amp_org_id AS donor_id, " +
                    "       org.name AS donor_name, " +
                    "       i.indicator_id AS indicator_id, " +
                    "       i.name AS indicator_name, " +
                    "       i.program_id AS program_id, " +
                    "       t.name AS program_name, " +
                    "       aa.amp_activity_id AS activity_id, " +
                    "       aa.name AS activity_name, " +
                    "       ROUND(CAST(SUM(CASE WHEN iv.value_type = 0 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_target, " +
                    "       ROUND(CAST(SUM(CASE WHEN iv.value_type = 1 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_actual " +
                    "FROM amp_indicator i " +
                    "JOIN amp_indicator_connection ic ON ic.indicator_id = i.indicator_id " +
                    "JOIN amp_indicator_values iv ON iv.ind_connect_id = ic.id " +
                    "LEFT JOIN amp_category_value cv ON i.indicators_category = cv.id " +
                    "JOIN amp_activity_location al ON ic.activity_location = al.amp_activity_location_id " +
                    "JOIN amp_category_value_location cvl ON cvl.id = al.location_id " +
                    "JOIN amp_org_role oro ON oro.activity = ic.activity_id " +
                    "JOIN amp_organisation org ON oro.organisation = org.amp_org_id " +
                    "JOIN amp_theme t ON t.amp_theme_id = i.program_id " +
                    "JOIN amp_activity aa ON aa.amp_activity_id = oro.activity " +
                    "WHERE ic.sub_clazz = 'a' " +
                    "  AND iv.value_type IN (0, 1) " +
                    "  AND oro.role = 1 " +
                    "GROUP BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, " +
                    "         i.program_id, t.name, aa.amp_activity_id, aa.name, i.indicator_id, i.name " +
                    "ORDER BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, " +
                    "         i.program_id, t.name, aa.amp_activity_id, aa.name";
    public static List<String> getFilterOptions(String type) {
        if (!Arrays.asList("core_type_name", "country_name", "donor_name", "indicator_name", "program_name", "activity_name")
                .contains(type)) {
            throw new IllegalArgumentException("Invalid type provided: " + type);
        }

        // Modify the query to retrieve only the distinct values for the specified type column
        String filterQuery = "SELECT DISTINCT " + type + " FROM (" + QUERY + ") AS filtered_data";

        List<String> options;

        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createNativeQuery(filterQuery);
        options = query.getResultList();
        return options;
    }
}
