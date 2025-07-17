package org.digijava.kernel.services.analytics;

import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for fetching data from database views for import into another database
 */
public class DataImportService {
    private static final Logger logger = LoggerFactory.getLogger(DataImportService.class);

    /**
     * Fetches data from a list of views
     *
     * @param viewNames list of view names to fetch data from
     * @return map of view name to list of records
     */
    public static Map<String, Object> fetchDataFromViews(List<String> viewNames) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (viewNames == null || viewNames.isEmpty()) {
            result.put("error", "No views specified");
            return result;
        }

        for (String viewName : viewNames) {
            try {
                List<Map<String, Object>> viewData = fetchViewData(viewName);
                result.put(viewName, viewData);
            } catch (Exception e) {
                logger.error("Error fetching data from view: " + viewName, e);
                Map<String, Object> errorInfo = new HashMap<>();
                errorInfo.put("error", e.getMessage());
                result.put(viewName, errorInfo);
            }
        }

        return result;
    }

    /**
     * Fetches data from a single view
     *
     * @param viewName name of the view to fetch data from
     * @return list of records from the view
     */
    private static List<Map<String, Object>> fetchViewData(String viewName) {
        List<Map<String, Object>> records = new ArrayList<>();

        // Validate that the view exists
        if (!viewExists(viewName)) {
            throw new IllegalArgumentException("View does not exist: " + viewName);
        }

        // Fetch data from the view
        PersistenceManager.getSession().doWork(connection -> {
            List<String> columns = new ArrayList<>(SQLUtils.getTableColumns(viewName, true));

            DatabaseViewFetcher.fetchView(connection, TLSUtils.getEffectiveLangCode(), viewName, null,
                    columns, rs -> {
                try {
                    while (rs.next()) {
                        records.add(resultSetRowToMap(rs));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error processing result set for view: " + viewName, e);
                }
            });
        });

        return records;
    }

    /**
     * Converts a ResultSet row to a Map
     *
     * @param rs ResultSet to convert
     * @return Map representation of the row
     * @throws SQLException if there's an error accessing the ResultSet
     */
    private static Map<String, Object> resultSetRowToMap(ResultSet rs) throws SQLException {
        Map<String, Object> row = new LinkedHashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);
            row.put(columnName, value);
        }

        return row;
    }

    /**
     * Checks if a view exists in the database
     *
     * @param viewName name of the view to check
     * @return true if the view exists, false otherwise
     */
    private static boolean viewExists(String viewName) {
        try {
            return !SQLUtils.getTableColumns(viewName, true).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets all available views in the database
     *
     * @return list of view names
     */
    public static List<String> getAllViews() {
        List<String> views = new ArrayList<>();

        PersistenceManager.getSession().doWork(connection -> {
            try (Statement stmt = connection.createStatement()) {
                String query = "SELECT table_name FROM information_schema.tables " +
                        "WHERE table_schema = 'public' AND table_type = 'VIEW' " +
                        "ORDER BY table_name";

                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        views.add(rs.getString("table_name"));
                    }
                }
            } catch (SQLException e) {
                logger.error("Error fetching views", e);
                throw new RuntimeException("Error fetching views", e);
            }
        });

        return views;
    }
}
