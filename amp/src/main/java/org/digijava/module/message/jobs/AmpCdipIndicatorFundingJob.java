package org.digijava.module.message.jobs;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.CoreIndicatorProgressDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.CoreIndicatorTypeDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.CoreIndicatorValueDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.CountryDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.DonorDTO;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.ProgramDTO;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class AmpCdipIndicatorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    private static final int BATCH_SIZE = 50;
    private static final Map<String, CoreIndicatorTypeDTO> indicatorMap = new LinkedHashMap<>();

    static {
        registerIndicatorGroup("Land restored or under sustainable management (ha)", "cdip_ndc_sn_land", "ha", 1);
        registerIndicatorGroup("People adopting or covered by adaptation measures (persons)",
                "cdip_ndc_sn_people", "persons", 2);
        registerIndicatorGroup("Climate finance committed or mobilized (XOF)", "cdip_ndc_sn_finance", "XOF", 3);
        registerIndicatorGroup("GHG emissions mitigated (Gg CO2e)", "cdip_ndc_sn_ghg", "Gg CO2e", 4);
        registerIndicatorGroup("Protective works and linear protection (km)", "cdip_ndc_sn_protection", "km", 5);
    }

    private static void registerIndicatorGroup(String name, String coreType, String unit, int order) {
        indicatorMap.put(name, new CoreIndicatorTypeDTO(unit, coreType, name, 1D, order));
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<CoreIndicatorProgressDTO> reports = new ArrayList<>();
        PersistenceManager.getSession().doWork(connection -> reports.addAll(readReports(connection)));
        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_URL);
        try {
            sendReportsInBatches(reports, serverUrl + "/update-core-indicator-progress");
        } catch (IOException exception) {
            throw new JobExecutionException("CDIP indicator batch export failed", exception);
        }
    }

    static void sendReportsInBatches(List<CoreIndicatorProgressDTO> reports, String serverUrl) throws IOException {
        int batchCount = Math.max(1, (reports.size() + BATCH_SIZE - 1) / BATCH_SIZE);
        for (int batchIndex = 0; batchIndex < batchCount; batchIndex++) {
            int start = batchIndex * BATCH_SIZE;
            int end = Math.min(start + BATCH_SIZE, reports.size());
            boolean clearExisting = batchIndex == 0;
            boolean finalBatch = batchIndex == batchCount - 1;
            AmpCoreIndicatorFundingJob.logger.info("Sending CDIP indicator batch " + (batchIndex + 1)
                    + "/" + batchCount + "; reports=" + (end - start)
                    + "; clearExisting=" + clearExisting + "; finalBatch=" + finalBatch);
            AmpCoreIndicatorFundingJob.sendReportBatch(reports.subList(start, end), serverUrl,
                    clearExisting, finalBatch);
        }
    }

    private static String senegalLocationsCte() {
        return "WITH RECURSIVE senegal_locations AS ("
                + "SELECT id AS location_id, id AS country_id, 'Senegal'::text AS country_name "
                + "FROM amp_category_value_location WHERE id = ("
                + "SELECT min(id) FROM amp_category_value_location "
                + "WHERE upper(trim(coalesce(iso, ''))) IN ('SN', 'SEN') "
                + "OR lower(trim(location_name)) = 'senegal') "
                + "UNION SELECT child.id, country.country_id, country.country_name "
                + "FROM amp_category_value_location child "
                + "JOIN senegal_locations country ON child.parent_location = country.location_id) ";
    }

    static String buildQuery() {
        String statuses = AmpARFilter.VALIDATED_ACTIVITY_STATUS.stream()
                .map(status -> "'" + status.getDbName() + "'")
                .collect(Collectors.joining(","));
        String programCodes = indicatorMap.values().stream()
                .map(type -> "'" + type.getCoreType().toUpperCase(Locale.ROOT) + "'")
                .collect(Collectors.joining(","));
        return senegalLocationsCte()
            + "SELECT cv.id AS core_type_id, cv.category_value AS core_type_name, "
            + "country.country_id AS country_id, country.country_name AS country_name, "
                + "org.amp_org_id AS donor_id, org.name AS donor_name, "
                + "i.program_id, t.name AS program_name, t.parent_theme_id, "
                + "ROUND(CAST(SUM(CASE WHEN iv.value_type = 0 THEN iv.value ELSE 0 END) AS NUMERIC), 2) "
                + "AS value_type_target, "
                + "ROUND(CAST(SUM(CASE WHEN iv.value_type = 1 THEN iv.value ELSE 0 END) AS NUMERIC), 2) "
                + "AS value_type_actual "
                + "FROM amp_indicator i "
                + "JOIN amp_indicator_connection ic ON ic.indicator_id = i.indicator_id "
                + "JOIN amp_indicator_values iv ON iv.ind_connect_id = ic.id "
                + "JOIN amp_category_value cv ON i.indicators_category = cv.id "
                + "JOIN amp_activity_location al ON ic.activity_location = al.amp_activity_location_id "
                + "AND iv.activity_location = al.amp_activity_location_id "
                + "JOIN senegal_locations country ON country.location_id = al.location_id "
                + "JOIN (SELECT DISTINCT activity, organisation FROM amp_org_role WHERE role = 1) oro "
                + "ON oro.activity = ic.activity_id "
                + "JOIN amp_organisation org ON oro.organisation = org.amp_org_id "
                + "JOIN amp_theme t ON t.amp_theme_id = i.program_id "
                + "JOIN amp_activity aa ON aa.amp_activity_id = ic.activity_id "
                + "WHERE ic.sub_clazz = 'a' AND iv.value_type IN (0, 1) "
                + "AND left(i.code, 12) = 'cdip_ndc_sn_' "
                + "AND t.theme_code IN (" + programCodes + ") "
                + "AND aa.approval_status IN (" + statuses + ") "
                + "GROUP BY cv.id, cv.category_value, country.country_id, country.country_name, "
                + "org.amp_org_id, org.name, i.program_id, t.name, t.parent_theme_id "
                + "ORDER BY cv.id, country.country_id, org.amp_org_id, i.program_id";
    }

    static List<CoreIndicatorProgressDTO> readReports(Connection connection) throws SQLException {
        List<CoreIndicatorProgressDTO> reports = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(buildQuery());
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                String categoryName = results.getString("core_type_name");
                CoreIndicatorTypeDTO type = indicatorMap.get(categoryName);
                if (type == null) {
                    throw new SQLException("Unknown CDIP category: " + categoryName);
                }
                CoreIndicatorProgressDTO report = new CoreIndicatorProgressDTO();
                report.setCountry(new CountryDTO(results.getLong("country_id"), results.getString("country_name")));
                report.setDonor(new DonorDTO(results.getLong("donor_id"), results.getString("donor_name")));
                report.setProgram(new ProgramDTO(results.getLong("program_id"), results.getString("program_name"),
                        results.getLong("parent_theme_id")));
                CoreIndicatorValueDTO value = new CoreIndicatorValueDTO();
                value.setCoreIndicatorType(type);
                value.setActualValue(results.getDouble("value_type_actual"));
                value.setTargetValue(results.getDouble("value_type_target"));
                report.setValues(Collections.singletonList(value));
                reports.add(report);
            }
        }
        return reports;
    }
}