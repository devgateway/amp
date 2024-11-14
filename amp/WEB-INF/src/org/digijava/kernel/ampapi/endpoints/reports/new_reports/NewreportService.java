package org.digijava.kernel.ampapi.endpoints.reports.new_reports;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class NewreportService {
private static final Logger logger  = LoggerFactory.getLogger(NewreportService.class);
    private static final String BASE_QUERY =
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
                    "  AND oro.role = 1 ";
    public static List<Map<String, Object>> getData(Map<String, String> filters) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        int page = Integer.parseInt(filters.get("page"));
        int size = Integer.parseInt(filters.get("size"));
        // Dynamically build the WHERE clause based on filters
        if (filters.containsKey("core_type_name") && !filters.get("core_type_name").isEmpty()) {
            queryBuilder.append(" AND cv.category_value = ? ");
        }
        if (filters.containsKey("country_name") && !filters.get("country_name").isEmpty()) {
            queryBuilder.append(" AND cvl.location_name = ? ");
        }
        if (filters.containsKey("donor_name") && !filters.get("donor_name").isEmpty()) {
            queryBuilder.append(" AND org.name = ? ");
        }
        if (filters.containsKey("indicator_name") && !filters.get("indicator_name").isEmpty()) {
            queryBuilder.append(" AND i.name = ? ");
        }
        if (filters.containsKey("program_name") && !filters.get("program_name").isEmpty()) {
            queryBuilder.append(" AND t.name = ? ");
        }
        if (filters.containsKey("activity_name") && !filters.get("activity_name").isEmpty()) {
            queryBuilder.append(" AND aa.name = ? ");
        }

        // Add GROUP BY and ORDER BY clauses
        queryBuilder.append("GROUP BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, ")
                .append("i.program_id, t.name, aa.amp_activity_id, aa.name, i.indicator_id, i.name ")
                .append("ORDER BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, ")
                .append("i.program_id, t.name, aa.amp_activity_id, aa.name");
        int offset = (page - 1) * size;
        queryBuilder.append(" OFFSET ? LIMIT ?");
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = PersistenceManager.getJdbcConnection().prepareStatement(queryBuilder.toString())) {
            int index = 1;
            if (filters.containsKey("core_type_name") && !filters.get("core_type_name").isEmpty()) {
                stmt.setString(index++, filters.get("core_type_name"));
            }
            if (filters.containsKey("country_name") && !filters.get("country_name").isEmpty()) {
                stmt.setString(index++, filters.get("country_name"));
            }
            if (filters.containsKey("donor_name") && !filters.get("donor_name").isEmpty()) {
                stmt.setString(index++, filters.get("donor_name"));
            }
            if (filters.containsKey("indicator_name") && !filters.get("indicator_name").isEmpty()) {
                stmt.setString(index++, filters.get("indicator_name"));
            }
            if (filters.containsKey("program_name") && !filters.get("program_name").isEmpty()) {
                stmt.setString(index++, filters.get("program_name"));
            }
            if (filters.containsKey("activity_name") && !filters.get("activity_name").isEmpty()) {
                stmt.setString(index++, filters.get("activity_name"));
            }
            stmt.setInt(index++, offset);
            stmt.setInt(index++, size);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("core_type_id", rs.getInt("core_type_id"));
                    row.put("core_type_name", rs.getString("core_type_name"));
                    row.put("country_id", rs.getInt("country_id"));
                    row.put("country_name", rs.getString("country_name"));
                    row.put("donor_id", rs.getInt("donor_id"));
                    row.put("donor_name", rs.getString("donor_name"));
                    row.put("indicator_id", rs.getInt("indicator_id"));
                    row.put("indicator_name", rs.getString("indicator_name"));
                    row.put("program_id", rs.getInt("program_id"));
                    row.put ("program_name", rs.getString("program_name"));
                    row.put("activity_id", rs.getInt("activity_id"));
                    row.put("activity_name", rs.getString("activity_name"));
                    results.add(row);
                }
            }
        }
        return results;
    }
    public static List<String> getFilterOptions(String type) {
        if (!Arrays.asList("core_type_name", "country_name", "donor_name", "indicator_name", "program_name", "activity_name")
                .contains(type)) {
            throw new IllegalArgumentException("Invalid type provided: " + type);
        }

        // Modify the query to retrieve only the distinct values for the specified type column
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        queryBuilder.append("GROUP BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, ")
                .append("i.program_id, t.name, aa.amp_activity_id, aa.name, i.indicator_id, i.name ")
                .append("ORDER BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, ")
                .append("i.program_id, t.name, aa.amp_activity_id, aa.name");

        String filterQuery = "SELECT DISTINCT " + type + " FROM (" + queryBuilder + ") AS filtered_data";

        List<String> options;

        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createNativeQuery(filterQuery);
        options = query.getResultList();
        return options;
    }
}
