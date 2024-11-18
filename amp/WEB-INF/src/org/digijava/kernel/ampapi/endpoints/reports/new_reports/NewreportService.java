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
                    "       ROUND(CAST(SUM(CASE WHEN iv.value_type = 1 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_actual, " +
                    "       COUNT(*) OVER() AS total_count " +
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

    private static final String GROUP_BY = "GROUP BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, " +
                            "i.program_id, t.name, aa.amp_activity_id, aa.name, i.indicator_id, i.name ";
    private  static final String ORDER_BY = "ORDER BY cv.id, cv.category_value, al.location_id, cvl.location_name, org.amp_org_id, org.name, " +
                            "i.program_id, t.name, aa.amp_activity_id, aa.name ";


    public static Map<String, Object> getData(Map<String, String> filters) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        Map<Integer, String> filterValues = new HashMap<>();

        // Build WHERE clause dynamically
        buildWhereClause(filters, queryBuilder, filterValues);
        queryBuilder.append(GROUP_BY).append(ORDER_BY);

        // Add pagination
        queryBuilder.append(" OFFSET ? LIMIT ?");

        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> finalResult = new HashMap<>();
        long totalElements = 0L;

        try (PreparedStatement stmt = PersistenceManager.getJdbcConnection().prepareStatement(queryBuilder.toString())) {
            // Set filter values and pagination parameters
            setFilterValues(stmt, filterValues);
            int page = Integer.parseInt(filters.getOrDefault("page", "1"));
            int size = Integer.parseInt(filters.getOrDefault("size", "10"));
            stmt.setInt(filterValues.size() + 1, (page - 1) * size); // Offset
            stmt.setInt(filterValues.size() + 2, size);             // Limit

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(extractRow(rs));
                    totalElements = rs.getLong("total_count");
                }
            }
        }

        finalResult.put("content", results);
        finalResult.put("totalElements", totalElements);
        return finalResult;
    }

    private static void buildWhereClause(Map<String, String> filters, StringBuilder queryBuilder, Map<Integer, String> filterValues) {
        int index = 1;
        Map<String, String> columnMapping = new HashMap<>();

                columnMapping.put("core_type_name", "cv.category_value");
                columnMapping.put("country_name", "cvl.location_name");
                columnMapping.put("donor_name", "org.name");
                columnMapping.put("indicator_name", "i.name");
                columnMapping.put("program_name", "t.name");
                columnMapping.put("activity_name", "aa.name");


        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (columnMapping.containsKey(filter.getKey()) && !filter.getValue().isEmpty()) {
                queryBuilder.append(" AND ").append(columnMapping.get(filter.getKey())).append(" = ? ");
                filterValues.put(index++, filter.getValue());
            }
        }
    }

    private static void setFilterValues(PreparedStatement stmt, Map<Integer, String> filterValues) throws SQLException {
        for (Map.Entry<Integer, String> entry : filterValues.entrySet()) {
            stmt.setString(entry.getKey(), entry.getValue());
        }
    }

    private static Map<String, Object> extractRow(ResultSet rs) throws SQLException {
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
        row.put("program_name", rs.getString("program_name"));
        row.put("activity_id", rs.getInt("activity_id"));
        row.put("activity_name", rs.getString("activity_name"));
        row.put("actual_value", rs.getString("value_type_actual"));
        row.put("target_value", rs.getString("value_type_target"));
        return row;
    }
    public static List<String> getFilterOptions(String type) {
        if (!Arrays.asList("core_type_name", "country_name", "donor_name", "indicator_name", "program_name", "activity_name")
                .contains(type)) {
            throw new IllegalArgumentException("Invalid type provided: " + type);
        }

        // Modify the query to retrieve only the distinct values for the specified type column


        String filterQuery = "SELECT DISTINCT " + type + " FROM (" + BASE_QUERY + GROUP_BY + ORDER_BY + ") AS filtered_data";

        List<String> options;

        Session session = PersistenceManager.getRequestDBSession();
        Query query = session.createNativeQuery(filterQuery);
        options = query.getResultList();
        return options;
    }
}
