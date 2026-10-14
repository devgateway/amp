package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AmpCoreIndicatorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpCoreIndicatorFundingJob.class);

    private static final int BATCH_SIZE = 50;

    static List<Long> parseCategoryIds(String settingValue) {
        LinkedHashSet<Long> categoryIds = new LinkedHashSet<>();
        if (settingValue != null) {
            for (String value : settingValue.split(";")) {
                if (!value.trim().isEmpty()) {
                    long categoryId = Long.parseLong(value.trim());
                    if (categoryId <= 0) {
                        throw new IllegalArgumentException("Indicator category IDs must be positive");
                    }
                    categoryIds.add(categoryId);
                }
            }
        }
        return new ArrayList<>(categoryIds);
    }

    static CoreIndicatorTypeDTO indicatorType(Long categoryId, String name, String unit, int order) {
        return new CoreIndicatorTypeDTO(unit, "indicator_category_" + categoryId, name, 1D, order);
    }

    static String buildQuery() {
        return buildQuery(null);
    }

    private static Long resolveDefaultCountryId() {
        AmpCategoryValueLocations country = DynLocationManagerUtil.getDefaultCountry();
        if (country == null || country.getId() == null || country.getId() <= 0
                || Boolean.TRUE.equals(country.getDeleted())) {
            throw new IllegalStateException("Default Country does not resolve to an active Administrative Level 0 location");
        }
        return country.getId();
    }

    private static String countryLocationsCte(Long countryId) {
        if (countryId == null || countryId <= 0) {
            throw new IllegalArgumentException("Default country location ID must be positive");
        }
        return "WITH RECURSIVE country_locations AS (SELECT location.id AS location_id, "
                + "location.id AS country_id, location.location_name AS country_name "
                + "FROM amp_category_value_location location WHERE location.id = " + countryId
                + " AND NOT coalesce(location.deleted, false) "
                + "UNION SELECT child.id, country.country_id, country.country_name "
                + "FROM amp_category_value_location child "
                + "JOIN country_locations country ON child.parent_location = country.location_id "
                + "WHERE NOT coalesce(child.deleted, false)) ";
    }

    static String buildQuery(List<Long> categoryIds) {
        return buildQuery(categoryIds, resolveDefaultCountryId());
    }

    static String buildQuery(List<Long> categoryIds, Long countryId) {
        StringBuilder query = new StringBuilder(countryLocationsCte(countryId));
        query.append("        SELECT cv.id                                                                                AS core_type_id,  ");
        query.append("        cv.category_value                                                                    AS core_type_name,  ");
        query.append("        coalesce(nullif(trim(i.unit), ''), indicator_unit.category_value)                     AS core_type_unit,  ");
        query.append("        country.country_id                                                                   AS country_id,  ");
        query.append("        country.country_name                                                                 AS country_name,  ");
        query.append("        org.amp_org_id                                                                       AS donor_id,  ");
        query.append("        org.name                                                                             AS donor_name,  ");
        query.append("        i.program_id                                                                         AS program_id,  ");
        query.append("        t.name                                                                               AS program_name,  ");
        query.append("        t.parent_theme_id                                                                    AS parent_theme_id,  ");
        query.append("        ROUND(CAST(SUM(CASE WHEN iv.value_type = 0 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_target,  ");
        query.append("        ROUND(CAST(SUM(CASE WHEN iv.value_type = 1 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_actual  ");
        query.append("        FROM amp_indicator i  ");
        query.append("        JOIN amp_indicator_connection ic ON ic.indicator_id = i.indicator_id  ");
        query.append("        JOIN amp_indicator_values iv ON iv.ind_connect_id = ic.id  ");
        query.append("        JOIN amp_category_value cv ON i.indicators_category = cv.id  ");
        query.append("        JOIN amp_category_class category_class ON category_class.id = cv.amp_category_class_id  ");
        query.append("        LEFT JOIN amp_category_value indicator_unit ON indicator_unit.id = i.unit_of_measure_id  ");
        query.append("        JOIN amp_activity_location al ON (ic.activity_location = al.amp_activity_location_id and  ");
        query.append("        iv.activity_location = al.amp_activity_location_id)  ");
        query.append("        JOIN country_locations country ON country.location_id = al.location_id  ");
        query.append("        JOIN (SELECT DISTINCT activity, organisation FROM amp_org_role WHERE role = 1) oro ON oro.activity = ic.activity_id  ");
        query.append("        JOIN amp_organisation org ON oro.organisation = org.amp_org_id  ");
        query.append("        JOIN amp_theme t ON t.amp_theme_id = i.program_id  ");
        query.append("        JOIN amp_activity aa ON aa.amp_activity_id = ic.activity_id AND aa.amp_activity_id = al.amp_activity_id  ");
        query.append("        WHERE ic.sub_clazz = 'a'  ");
        query.append("        AND iv.value_type IN (0, 1)  ");
        query.append("        AND category_class.keyname = 'indicator_category' AND NOT coalesce(cv.deleted, false)  ");
        if (categoryIds != null && categoryIds.isEmpty()) {
            query.append("        AND false  ");
        } else if (categoryIds != null) {
            query.append("        AND cv.id IN (").append(categoryIds.stream().map(String::valueOf)
                    .collect(Collectors.joining(","))).append(")  ");
        }
        String statusQuery = " AND aa.approval_status IN (:statuses) ";
        String placeholders = AmpARFilter.VALIDATED_ACTIVITY_STATUS.stream()
                .map(status -> "'" + status.getDbName() + "'")
                .collect(Collectors.joining(","));

        statusQuery = statusQuery.replace(":statuses", placeholders);
        query.append(statusQuery);

        query.append("        GROUP BY cv.id, cv.category_value, country.country_id, country.country_name, org.amp_org_id, org.name,  ");
        query.append("        i.program_id, t.name, coalesce(nullif(trim(i.unit), ''), indicator_unit.category_value), t.parent_theme_id  ");
        query.append("        ORDER BY cv.id, cv.category_value, country.country_id, country.country_name, org.amp_org_id, org.name,  ");
        query.append("        i.program_id, t.name  ");
        return query.toString();
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<Long> categoryIds;
        try {
            categoryIds = parseCategoryIds(FeaturesUtil.getGlobalSettingValue(
                    GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_CATEGORIES));
        } catch (IllegalArgumentException exception) {
            throw new JobExecutionException("Invalid core indicator category selection", exception);
        }
        if (categoryIds.isEmpty()) {
            logger.warn("Core indicator export skipped: no indicator categories selected in Global Settings");
            return;
        }
        List<?> activeCategoryIds = PersistenceManager.getSession().createNativeQuery(
            "SELECT cv.id FROM amp_category_value cv "
                + "JOIN amp_category_class category_class ON category_class.id = cv.amp_category_class_id "
                + "WHERE category_class.keyname = 'indicator_category' AND NOT coalesce(cv.deleted, false) "
                + "AND cv.id IN (:categoryIds)")
            .setParameterList("categoryIds", categoryIds).list();
        LinkedHashSet<Long> availableCategoryIds = activeCategoryIds.stream()
            .map(PersistenceManager::getLong).collect(Collectors.toCollection(LinkedHashSet::new));
        if (!availableCategoryIds.containsAll(categoryIds)) {
            throw new JobExecutionException("Selected core indicator categories are missing, deleted or invalid");
        }
        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_URL);
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            throw new JobExecutionException("AMP Dashboard Core Indicator URL is not configured");
        }
        Long countryId;
        try {
            countryId = resolveDefaultCountryId();
        } catch (RuntimeException exception) {
            throw new JobExecutionException("Cannot resolve the configured Default Country for indicator export", exception);
        }
        logger.info("Core indicator export default country location ID: " + countryId);
        List<CoreIndicatorProgressDTO> resp = new ArrayList<>();
        PersistenceManager.getSession().doWork(connection -> resp.addAll(readReports(connection, categoryIds, countryId)));
        try {
            sendReportsInBatches(resp, (serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl)
                    + "/update-core-indicator-progress");
        } catch (IOException exception) {
            throw new JobExecutionException("Core indicator batch export failed", exception);
        }
    }

    static List<CoreIndicatorProgressDTO> readReports(Connection connection) throws SQLException {
        return readReports(connection, null);
    }

    static List<CoreIndicatorProgressDTO> readReports(Connection connection, List<Long> categoryIds) throws SQLException {
        return readReports(connection, categoryIds, resolveDefaultCountryId());
    }

    static List<CoreIndicatorProgressDTO> readReports(Connection connection, List<Long> categoryIds,
            Long defaultCountryId) throws SQLException {
        List<CoreIndicatorProgressDTO> reports = new ArrayList<>();
        Map<Long, String> categoryUnits = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(buildQuery(categoryIds, defaultCountryId));
                ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Long coreTypeId = rs.getLong("core_type_id");
                    String coreTypeName = rs.getString("core_type_name");
                    String coreTypeUnit = rs.getString("core_type_unit");
                    if (coreTypeUnit == null || coreTypeUnit.trim().isEmpty()) {
                        throw new SQLException("Missing unit for indicator category " + coreTypeId);
                    }
                    coreTypeUnit = coreTypeUnit.trim();
                    String existingUnit = categoryUnits.putIfAbsent(coreTypeId, coreTypeUnit);
                    if (existingUnit != null && !existingUnit.equals(coreTypeUnit)) {
                        throw new SQLException("Mixed units for indicator category " + coreTypeId);
                    }
                    if (rs.getObject("country_id") == null || rs.getString("country_name") == null) {
                        throw new SQLException("Cannot resolve a country for indicator category " + coreTypeId
                                + "; check the configured Default Country location hierarchy");
                    }
                    Long countryId = rs.getLong("country_id");
                    String countryName = rs.getString("country_name");
                    Long donorId = rs.getLong("donor_id");
                    String donorName = rs.getString("donor_name");
                    String programName = rs.getString("program_name");
                    Long programId = rs.getLong("program_id");
                    Double actualValue = rs.getDouble("value_type_actual");
                    Double targetValue = rs.getDouble("value_type_target");
                    Long parentId = rs.getLong("parent_theme_id");

                    CoreIndicatorProgressDTO coreIndicatorProgressDTO = new CoreIndicatorProgressDTO();
                    coreIndicatorProgressDTO.setCountry(new CountryDTO(countryId, countryName));
                    coreIndicatorProgressDTO.setDonor(new DonorDTO(donorId, donorName));
                    coreIndicatorProgressDTO.setProgram(new ProgramDTO(programId, programName, parentId));
                    CoreIndicatorValueDTO coreIndicatorValueDTO = new CoreIndicatorValueDTO();
                    coreIndicatorValueDTO.setCoreIndicatorType(indicatorType(coreTypeId, coreTypeName,
                            coreTypeUnit, categoryIds == null
                                ? categoryUnits.size() : categoryIds.indexOf(coreTypeId) + 1));
                    coreIndicatorValueDTO.setActualValue(actualValue);
                    coreIndicatorValueDTO.setTargetValue(targetValue);
                    coreIndicatorProgressDTO.setValues(Collections.singletonList(coreIndicatorValueDTO));
                    reports.add(coreIndicatorProgressDTO);
                }
        }
        return reports;
    }

    static void sendReportsInBatches(List<CoreIndicatorProgressDTO> reports, String serverUrl) throws IOException {
        int batchCount = Math.max(1, (reports.size() + BATCH_SIZE - 1) / BATCH_SIZE);
        UUID uploadId = UUID.randomUUID();
        for (int batchIndex = 0; batchIndex < batchCount; batchIndex++) {
            int start = batchIndex * BATCH_SIZE;
            int end = Math.min(start + BATCH_SIZE, reports.size());
            logger.info("Sending core indicator batch " + (batchIndex + 1) + "/" + batchCount
                + "; reports=" + (end - start) + "; uploadId=" + uploadId);
            sendReportBatch(reports.subList(start, end), serverUrl, uploadId, batchIndex, batchCount);
        }
    }

    public static void sendReportsToServer(List<CoreIndicatorProgressDTO> ampCoreIndicatorCoreData, String serverUrl) {
        try {
            postReports(ampCoreIndicatorCoreData, serverUrl);
        } catch (Exception e) {
            logger.error("Exception raised when sending data to dashboard", e);
        }
    }

    static void sendReportBatch(List<CoreIndicatorProgressDTO> reports, String serverUrl,
            UUID uploadId, int batchIndex, int batchCount) throws IOException {
        URL endpoint = new URL(serverUrl);
        String query = endpoint.getQuery();
        String batchPath = endpoint.getPath() + "/uploads/" + uploadId + "/batches/" + batchIndex;
        URL batchUrl = new URL(endpoint, batchPath + "?"
                + (query == null || query.isEmpty() ? "" : query + "&") + "batchCount=" + batchCount);
        postReports(reports, batchUrl.toExternalForm());
    }

    private static void postReports(List<CoreIndicatorProgressDTO> ampCoreIndicatorCoreData, String serverUrl)
            throws IOException {
        HttpURLConnection connection = getHttpURLConnection(serverUrl);
        try {
            String jsonData = serializeReports(ampCoreIndicatorCoreData);
            byte[] requestBody = jsonData.getBytes(StandardCharsets.UTF_8);
            logger.info("Sending indicator progress export; method=" + connection.getRequestMethod()
                    + "; url=" + requestDestination(connection.getURL())
                    + "; reports=" + ampCoreIndicatorCoreData.size() + "; bytes=" + requestBody.length);
            logger.info("Indicator progress export request JSON: " + jsonData);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                String message = "Error sending data to amp dashboard. HTTP Response Code: " + responseCode
                        + "; url=" + requestDestination(connection.getURL())
                        + "; server=" + connection.getHeaderField("Server")
                        + "; reports: " + ampCoreIndicatorCoreData.size()
                        + "; bytes=" + requestBody.length
                        + "; response body: " + readErrorResponse(connection);
                logger.error(message);
                throw new IOException(message);
            }
            logger.debug("Data sent successfully to amp dashboard. HTTP Response Code: " + responseCode);
        } finally {
            connection.disconnect();
        }
    }

    static String requestDestination(URL url) {
        return url.getProtocol() + "://" + url.getHost()
                + (url.getPort() == -1 ? "" : ":" + url.getPort()) + url.getPath();
    }

    static String serializeReports(List<CoreIndicatorProgressDTO> reports) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CoreIndicatorValueDTO.class, new CoreIndicatorValueDTOSerializer())
                .create();
        return gson.toJson(reports);
    }

    static String readErrorResponse(HttpURLConnection connection) throws IOException {
        InputStream errorStream = connection.getErrorStream();
        if (errorStream == null) {
            return "(no response body)";
        }
        try (InputStream response = errorStream) {
            byte[] buffer = new byte[8193];
            int length = 0;
            while (length < buffer.length) {
                int bytesRead = response.read(buffer, length, buffer.length - length);
                if (bytesRead < 0) {
                    break;
                }
                length += bytesRead;
            }
            String body = new String(buffer, 0, Math.min(length, 8192), StandardCharsets.UTF_8)
                    .replace('\r', ' ').replace('\n', ' ');
            return body + (length > 8192 ? " [truncated]" : "");
        }
    }

    @NotNull
    private static HttpURLConnection getHttpURLConnection(String serverUrl) throws IOException {
        URL url = new URL(serverUrl);

        // Open a connection to the server
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(60000);
        connection.setInstanceFollowRedirects(false);

        // Set the HTTP request method to POST
        connection.setRequestMethod("POST");

        // Set the content type of the request
        connection.setRequestProperty("Content-Type", "application/json");

        // Enable input and output streams for the connection
        connection.setDoOutput(true);
        return connection;
    }
}
