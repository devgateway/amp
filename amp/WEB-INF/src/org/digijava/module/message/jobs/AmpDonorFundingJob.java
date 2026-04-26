package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.IntCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.ReportsDashboard;
import org.dgfoundation.amp.newreports.TextCell;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class AmpDonorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpDonorFundingJob.class);
    private List<String> currencies = new ArrayList<>(java.util.Arrays.asList("USD", "EUR"));
    /*private String reportName = "preview report";
    private Integer reportType = ArConstants.DONOR_TYPE;*/

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //TODO make currency configurable
        AmpJobsUtil.populateRequest();
        Long ampTeamId = FeaturesUtil
                .getGlobalSettingValueLong(GlobalSettingsConstants.WORKSPACE_TO_RUN_REPORT_FROM_JOB);
        AmpJobsUtil.setTeamForNonRequestReport(ampTeamId);
        List<ReportsDashboard> ampDashboardFundingCombined = new ArrayList<>();
        currencies = Objects.requireNonNull(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DASHBOARD_CURRENCIES)).isEmpty() ?
                currencies :
                Arrays.asList(Objects.requireNonNull(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DASHBOARD_CURRENCIES)).split("\\|"));
        currencies.forEach(currency -> ampDashboardFundingCombined.addAll(getFundingByCurrency(currency)));
        //List<ReportsDashboard> ampDashboardFundingCombinedXDR = getFundingByCurrency("XDR");

        // Get translations for all fields (once, not per record)
        List<Map<String, String>> translations = getTranslations();

        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_URL);
        assert serverUrl != null;
        serverUrl = serverUrl.endsWith("/") ? serverUrl + "amp-funding/importDonorFunding" : serverUrl + "/amp-funding/importDonorFunding";
        sendReportsToServer(ampDashboardFundingCombined, translations, serverUrl);
    }

    @NotNull
    private List<ReportsDashboard> getFundingByCurrency(String currencyCode) {
        GeneratedReport fundingReport = generateReport(currencyCode);
        List<ReportsDashboard> ampDashboardFunding = processReportData(fundingReport, currencyCode);
        // The ampDashboardFunding data contains objects for commitments and disbursment differently in
        // separate objects. We need to combine them in same object combining commitment and disbursment values.
        //Make year configurable
        //+ "|" + report.getYear()
        return new ArrayList<>(ampDashboardFunding.stream()
                .collect(Collectors.toMap(
                        report -> report.getDonorAgency()
                                + "|" + report.getImplementingAgency()
                                + "|" + report.getPillar()
                                + "|" + report.getLocation()
                                + "|" + report.getImplementationLevel()
                                + "|" + report.getStatus()
                                //+ "|" + report.getYear()
                                + "|" + report.getReportingSystem()
                                + "|" + report.getTypeOfAssistance()
                                + "|" + report.getProcurementSystem()
                                + "|" + report.getResponsibleOrganization()
                                + "|" + report.getSecondarySector(),
                        report -> report,
                        (report1, report2) -> {
                            report1.sumWith(report2);
                            return report1;
                        }))
                .values());
    }

    private List<ReportsDashboard> processReportData(GeneratedReport report, String currencyCode) {
        logger.info("Processing report data for currency: " + currencyCode);
        logger.info("Number of leaf headers: " + report.leafHeaders.size());

        // Find columns by name for robustness (works regardless of order)
        // Note: Indices are based on the order in addColumnsToSpecification:
        // Hierarchies: 0-10, then AMP_ID (11), ACTIVITY_COUNT (12), measures (13-14)
        ReportOutputColumn donorAgency = findColumnByName(report.leafHeaders, ColumnConstants.DONOR_AGENCY, 0);
        ReportOutputColumn implementingAgency = findColumnByName(report.leafHeaders, ColumnConstants.IMPLEMENTING_AGENCY, 1);
        ReportOutputColumn procurementSystemAgency = findColumnByName(report.leafHeaders, ColumnConstants.PROCUREMENT_SYSTEM, 2);
        ReportOutputColumn pilar = findColumnByName(report.leafHeaders, ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, 3);
        ReportOutputColumn implementationLevel = findColumnByName(report.leafHeaders, ColumnConstants.IMPLEMENTATION_LEVEL, 4);
        String location_adm_level = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DONOR_FUNDING_ADM_LEVEL);
        ReportOutputColumn impLocation = findColumnByName(report.leafHeaders, location_adm_level, 5);
        ReportOutputColumn status = findColumnByName(report.leafHeaders, ColumnConstants.STATUS, 6);
        ReportOutputColumn typeOfAssistance = findColumnByName(report.leafHeaders, ColumnConstants.TYPE_OF_ASSISTANCE, 7);
        ReportOutputColumn reportingSystem = findColumnByName(report.leafHeaders, ColumnConstants.PRIMARY_SECTOR, 8); // Also called Forum
        ReportOutputColumn responsibleOrg = findColumnByName(report.leafHeaders, ColumnConstants.RESPONSIBLE_ORGANIZATION, 9);
        ReportOutputColumn secondarySector = findColumnByName(report.leafHeaders, ColumnConstants.SECONDARY_SECTOR, 10);
        ReportOutputColumn ampId = findColumnByName(report.leafHeaders, ColumnConstants.AMP_ID, 11); // AMP_ID is now after hierarchies

        List<ReportsDashboard> ampDashboardFunding = new ArrayList<>();

        for (ReportArea child : report.reportContents.getChildren()) {
            TextCell donorAgencyCell = (TextCell) child.getContents().get(donorAgency);

            if (child.getChildren() != null) {
                for (ReportArea implementingAgencyData : child.getChildren()) {
                    TextCell implementingAgencyCell = (TextCell) implementingAgencyData.getContents().get(implementingAgency);

                    if (implementingAgencyData.getChildren() != null) {
                        for (ReportArea procurementSystemAgencyData : implementingAgencyData.getChildren()) {
                            TextCell procurementSystemAgencyCell = (TextCell) procurementSystemAgencyData.getContents().get(procurementSystemAgency);

                            if (procurementSystemAgencyData.getChildren() != null) {
                                for (ReportArea pilarData : procurementSystemAgencyData.getChildren()) {
                                    TextCell pilarCell = (TextCell) pilarData.getContents().get(pilar);

                                    if (pilarData.getChildren() != null) {
                                        for (ReportArea implLevel : pilarData.getChildren()) {
                                            TextCell implLevelCell = (TextCell) implLevel.getContents().get(implementationLevel);

                                            if (implLevel.getChildren() != null) {
                                                for (ReportArea location : implLevel.getChildren()) {
                                                    TextCell locationCell = (TextCell) location.getContents().get(impLocation);

                                                    if (location.getChildren() != null) {
                                                        for (ReportArea statusData : location.getChildren()) {
                                                            TextCell statusCell = (TextCell) statusData.getContents().get(status);

                                                            if (statusData.getChildren() != null) {
                                                                for (ReportArea typeOfAssistanceData : statusData.getChildren()) {
                                                                    TextCell typeOfAssistanceCell = (TextCell) typeOfAssistanceData.getContents().get(typeOfAssistance);

                                                                    if (typeOfAssistanceData.getChildren() != null) {
                                                                        for (ReportArea primarySectorData : typeOfAssistanceData.getChildren()) {
                                                                            TextCell reportingSystemCell = (TextCell) primarySectorData.getContents().get(reportingSystem);

                                                                            if (primarySectorData.getChildren() != null) {
                                                                                for (ReportArea responsibleOrgData : primarySectorData.getChildren()) {
                                                                                    TextCell responsibleOrgCell = (TextCell) responsibleOrgData.getContents().get(responsibleOrg);

                                                                                    if (responsibleOrgData.getChildren() != null) {
                                                                                        for (ReportArea secondarySectorData : responsibleOrgData.getChildren()) {
                                                                                            TextCell secondarySectorCell = (TextCell) secondarySectorData.getContents().get(secondarySector);

                                                                                            Long activityCount = 0L;
                                                                                            // collect activity count
                                                                                            for (Map.Entry<ReportOutputColumn, ReportCell> content : responsibleOrgData.getContents().entrySet()) {
                                                                                                ReportOutputColumn col = content.getKey();
                                                                                                if (col.originalColumnName.equals(ColumnConstants.ACTIVITY_COUNT)) {
                                                                                                    IntCell amount = (IntCell) content.getValue();
                                                                                                    if (amount != null && amount.value != null) {
                                                                                                        activityCount = (Long) amount.value;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            // gather AMP IDs from children (leaf nodes)
                                                                                            List<String> ampIdsList = new ArrayList<>();
                                                                                            if (responsibleOrgData.getChildren() != null) {
                                                                                                for (ReportArea ampIdData : responsibleOrgData.getChildren()) {
                                                                                                    TextCell ampIdCell = (TextCell) ampIdData.getContents().get(ampId);
                                                                                                    if (ampIdCell != null && ampIdCell.value != null) {
                                                                                                        ampIdsList.add(ampIdCell.value.toString());
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            // fallback if AMP ID directly on this node and no children
                                                                                            if (ampIdsList.isEmpty()) {
                                                                                                TextCell directAmpIdCell = (TextCell) responsibleOrgData.getContents().get(ampId);
                                                                                                if (directAmpIdCell != null && directAmpIdCell.value != null) {
                                                                                                    ampIdsList.add(directAmpIdCell.value.toString());
                                                                                                }
                                                                                            }
                                                                                            String ampIdsJoined = String.join(",", ampIdsList);

                                                                                            // now process measures
                                                                                            for (Map.Entry<ReportOutputColumn, ReportCell> content : responsibleOrgData.getContents().entrySet()) {
                                                                                                ReportOutputColumn col = content.getKey();
                                                                                                if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || col.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                                                                                                    if (col.parentColumn != null && col.parentColumn.originalColumnName.equals("Totals")) {
                                                                                                        ReportsDashboard fundingReport = new ReportsDashboard();
                                                                                                        fundingReport.setDonorAgency(donorAgencyCell != null ? donorAgencyCell.value.toString() : null);
                                                                                                        fundingReport.setImplementingAgency(implementingAgencyCell != null ? implementingAgencyCell.value.toString() : null);
                                                                                                        fundingReport.setPillar(pilarCell != null ? pilarCell.value.toString() : null);
                                                                                                        fundingReport.setLocation(locationCell != null ? locationCell.value.toString() : null);
                                                                                                        fundingReport.setImplementationLevel(implLevelCell != null ? implLevelCell.value.toString() : null);
                                                                                                        fundingReport.setStatus(statusCell != null ? statusCell.value.toString() : null);
                                                                                                        fundingReport.setReportingSystem(reportingSystemCell != null ? reportingSystemCell.value.toString() : null);
                                                                                                        fundingReport.setTypeOfAssistance(typeOfAssistanceCell != null ? typeOfAssistanceCell.value.toString() : null);
                                                                                                        fundingReport.setProcurementSystem(procurementSystemAgencyCell != null ? procurementSystemAgencyCell.value.toString() : null);
                                                                                                        fundingReport.setResponsibleOrganization(responsibleOrgCell != null ? responsibleOrgCell.value.toString() : null);
                                                                                                        fundingReport.setSecondarySector(secondarySectorCell != null ? secondarySectorCell.value.toString() : null);
                                                                                                        fundingReport.setActivityCount(activityCount);
                                                                                                        fundingReport.setCurrency(currencyCode);
                                                                                                        fundingReport.setActivityIds(ampIdsJoined);
                                                                                                        AmountCell amount = (AmountCell) content.getValue();
                                                                                                        if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)) {
                                                                                                            fundingReport.setActualCommitment(amount.extractValue());
                                                                                                        } else {
                                                                                                            fundingReport.setActualDisbursement(amount.extractValue());
                                                                                                        }
                                                                                                        ampDashboardFunding.add(fundingReport);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ampDashboardFunding;
    }

    /**
     * Find a column by name in leaf headers, with fallback to index if name doesn't match
     * This provides robustness in case the order changes
     */
    private ReportOutputColumn findColumnByName(List<ReportOutputColumn> leafHeaders, String columnName, int expectedIndex) {
        if (expectedIndex < leafHeaders.size()) {
            ReportOutputColumn col = leafHeaders.get(expectedIndex);
            // Verify the column name matches (case-insensitive comparison of originalColumnName)
            if (col.originalColumnName != null && col.originalColumnName.equalsIgnoreCase(columnName)) {
                return col;
            }
            if (columnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || columnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                assert col.originalColumnName != null;
                if (col.parentColumn.columnName.equalsIgnoreCase(columnName)) {
                    logger.info("Found Measure" + columnName + " at expected index " + expectedIndex);
                    return col;
                }
            }


            // If index doesn't match, search by name
            logger.warn("Column at index " + expectedIndex + " is " + col.originalColumnName +
                    ", expected " + columnName + ". Searching by name...");
        }

        // Fallback: search by name (works for both columns and measures)
        for (ReportOutputColumn col : leafHeaders) {
            if (col.originalColumnName != null && col.originalColumnName.equalsIgnoreCase(columnName)) {
                logger.info("Found " + columnName + " at different position");
                return col;
            }
            if (columnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || columnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                if (col.parentColumn != null && col.parentColumn.columnName.equalsIgnoreCase(columnName)) {
                    logger.info("Found Measure" + columnName + " at different position");
                    return col;
                }
            }
        }

        // For measures, also check if they're nested under a parent column (like "Totals")
        // This handles cases where measures might be structured differently
//        if (columnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || columnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
//            for (ReportOutputColumn col : leafHeaders) {
//                // Check if parent column exists and is "Totals", and this column's name matches the measure
//                if (col.parentColumn != null && col.parentColumn.originalColumnName != null
//                    && ("Totals".equalsIgnoreCase(col.parentColumn.originalColumnName)
//                        || "TOTALS".equals(col.parentColumn.originalColumnName))
//                    && col.originalColumnName != null && col.originalColumnName.equalsIgnoreCase(columnName)) {
//                    logger.info("Found measure column " + columnName + " under Totals");
//                    return col;
//                }
//            }
//        }

        throw new RuntimeException("Column not found: " + columnName + " in leaf headers. Available columns: " +
                leafHeaders.stream().map(c -> c.originalColumnName != null ? c.originalColumnName : "null").collect(Collectors.joining(", ")));
    }

    private void addFilters(ReportSpecificationImpl spec) {
        if (spec.getFilters() == null) {
            spec.setFilters(new ReportFiltersImpl());
        }
        addCommonFilters(spec, ColumnConstants.TYPE_OF_ASSISTANCE);
        addCommonFilters(spec, ColumnConstants.REPORTING_SYSTEM);

    }

    private static void addCommonFilters(ReportSpecificationImpl spec, String columnName) {
        ReportElement elem = new ReportElement(new ReportColumn(columnName));
        FilterRule filterRule = new FilterRule("-999999999", false);
        ((ReportFiltersImpl) spec.getFilters()).addFilterRule(elem, filterRule);

    }

    private GeneratedReport generateReport(String currencyCode) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", ArConstants.DONOR_TYPE);
        addColumnsToSpecification(spec);

        spec.setSummaryReport(false);
        //TODO broken by year configurable
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setShowOriginalCurrency(false);
        spec.setDisplayEmptyFundingRows(true);
        ReportSettingsImpl reportSettings = new ReportSettingsImpl();
        spec.setSettings(reportSettings);
        //addFilters(spec);
        reportSettings.setCurrencyCode(currencyCode);
        return EndpointUtils.runReport(spec);
    }

    private void addColumnsToSpecification(ReportSpecificationImpl spec) {
        String location_adm_level = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DONOR_FUNDING_ADM_LEVEL);
        // Add hierarchy columns first
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.PROCUREMENT_SYSTEM));
        spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTATION_LEVEL));
        spec.addColumn(new ReportColumn(location_adm_level));
        spec.addColumn(new ReportColumn(ColumnConstants.STATUS));
        spec.addColumn(new ReportColumn(ColumnConstants.TYPE_OF_ASSISTANCE));
        //TODO for GGW this is reporting system, for others it is Sectors
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        spec.addColumn(new ReportColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION));
        spec.addColumn(new ReportColumn(ColumnConstants.SECONDARY_SECTOR));
        // Ensure AMP ID is part of the hierarchy so commitments roll up under each activity

        // Set hierarchies - includes AMP_ID so commitments/disbursements roll up under each activity
        // This is required for summary reports to include all columns in leaf headers
        Set<ReportColumn> hierarchyColumns = new LinkedHashSet<>(spec.getColumns());
        spec.setHierarchies(hierarchyColumns);

        // Add non-hierarchy columns after setHierarchies
        spec.addColumn(new ReportColumn(ColumnConstants.AMP_ID));
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_COUNT));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));

        logger.info("Report columns set for Donor Funding Report" + spec.getColumns().size());
    }

    /**
     * Gets translations for all field labels (returns once, not per record)
     */
    private List<Map<String, String>> getTranslations() {
        List<Map<String, String>> translations = new ArrayList<>();

        try {
            // Get all available locales
            List<String> localeCodes = getAvailableLocaleCodes();
            if (localeCodes.isEmpty()) {
                logger.warn("No available locales found, skipping translations");
                return translations;
            }

            // Get the site
            Site site = TLSUtils.getSite();
            if (site == null) {
                logger.warn("No site found, skipping translations");
                return translations;
            }

            // Get location column name
            String locationAdmLevel = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DONOR_FUNDING_ADM_LEVEL);

            // Define field mappings: field name -> column constant
            Map<String, String> fieldMappings = new HashMap<>();
            fieldMappings.put("donorAgency", ColumnConstants.DONOR_AGENCY);
            fieldMappings.put("implementingAgency", ColumnConstants.IMPLEMENTING_AGENCY);
            fieldMappings.put("pillar", ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1);
            fieldMappings.put("location", locationAdmLevel != null ? locationAdmLevel : ColumnConstants.LOCATION);
            fieldMappings.put("implementationLevel", ColumnConstants.IMPLEMENTATION_LEVEL);
            fieldMappings.put("status", ColumnConstants.STATUS);
            fieldMappings.put("reportingSystem", ColumnConstants.PRIMARY_SECTOR);
            fieldMappings.put("typeOfAssistance", ColumnConstants.TYPE_OF_ASSISTANCE);
            fieldMappings.put("procurementSystem", ColumnConstants.PROCUREMENT_SYSTEM);
            fieldMappings.put("responsibleOrganization", ColumnConstants.RESPONSIBLE_ORGANIZATION);
            fieldMappings.put("currency", "Currency");
            fieldMappings.put("actualCommitment", MeasureConstants.ACTUAL_COMMITMENTS);
            fieldMappings.put("actualDisbursement", MeasureConstants.ACTUAL_DISBURSEMENTS);
            fieldMappings.put("activityCount", ColumnConstants.ACTIVITY_COUNT);
            fieldMappings.put("activityIds", "Activity IDs");
            fieldMappings.put("projectTitle", "Project Title");

            // Generate translations for all fields once
            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                String fieldName = entry.getKey();
                String label = entry.getValue();
                Map<String, String> fieldTranslation = getFieldTranslations(label, localeCodes, site);
                fieldTranslation.put("label", fieldName);
                translations.add(fieldTranslation);
            }

            logger.info("Generated translations for " + translations.size() + " fields with " + localeCodes.size() + " locales");
        } catch (Exception e) {
            logger.error("Error getting translations", e);
        }

        return translations;
    }

    /**
     * Gets all available locale codes from the database
     */
    private List<String> getAvailableLocaleCodes() {
        List<String> localeCodes = new ArrayList<>();
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Query<String> query = session.createQuery("SELECT l.code FROM " + Locale.class.getName() + " l WHERE l.available = true", String.class);
            List<String> codes = query.list();
            localeCodes.addAll(codes);
        } catch (Exception e) {
            logger.error("Error getting available locales", e);
        }
        return localeCodes;
    }

    /**
     * Gets translations for a field label in all available locales
     */
    private Map<String, String> getFieldTranslations(String label, List<String> localeCodes, Site site) {
        Map<String, String> translations = new HashMap<>();
        for (String localeCode : localeCodes) {
            try {
                String translated = TranslatorWorker.translateText(label, localeCode, site);
                translations.put(localeCode, translated != null ? translated : label);
            } catch (Exception e) {
                logger.warn("Error translating label '" + label + "' for locale '" + localeCode + "': " + e.getMessage());
                translations.put(localeCode, label);
            }
        }
        return translations;
    }

    public static void sendReportsToServer(List<ReportsDashboard> ampDashboardFunding, List<Map<String, String>> translations, String serverUrl) {
        try {
            // Create a URL object with the server's endpoint URL
            logger.info("Sending data to amp dashboard at: " + serverUrl);
            logger.info("Number of records to send: " + ampDashboardFunding.size());
            HttpURLConnection connection = getHttpURLConnection(serverUrl);

            // Create a wrapper object with reports and translations
            Map<String, Object> submissionData = new HashMap<>();
            submissionData.put("reports", ampDashboardFunding);
            submissionData.put("translations", translations);

            // Convert to JSON using a JSON library (e.g., Gson)
            Gson gson = new Gson();
            String jsonData = gson.toJson(submissionData);
//            logger.info("JSON data: " + jsonData);

            // Get the output stream of the connection
            try (OutputStream os = connection.getOutputStream()) {
                // Write the JSON data to the output stream
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (e.g., HTTP 200 OK)
            if (responseCode == 200) {
                // The data has been successfully sent to the server
                logger.info("Data sent successfully to amp dashboard. HTTP Response Code: " + responseCode);
            } else {
                // Handle the error condition (e.g., log an error message)
                logger.info("Error sending data to amp dashboard. HTTP Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            logger.error("Exception raised when sending data to dashboard", e);
        }
    }

    @NotNull
    private static HttpURLConnection getHttpURLConnection(String serverUrl) throws IOException {
        URL url = new URL(serverUrl);

        // Open a connection to the server
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the HTTP request method to POST
        connection.setRequestMethod("POST");

        // Set the content type of the request
        connection.setRequestProperty("Content-Type", "application/json");

        // Enable input and output streams for the connection
        connection.setDoOutput(true);
        return connection;
    }
}