package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static org.digijava.kernel.ampapi.endpoints.common.EPConstants.REPORT_TYPE_ID_MAP;

public class AmpDonorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpDonorFundingJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        GeneratedReport report = generateReport();
        List<ReportsDashboard> ampDashboardFunding = processReportData(report);
        // The ampDashboardFunding data contains objects for commitments and disbursment differently in
        // separate objects. We need to combine them in same object combining commitment and disbursment values.
        List<Map<String, Object>> combinedData = combineObjects(ampDashboardFunding);
        // Specify the server's endpoint URL
        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_URL);
        sendReportsToServer(combinedData, serverUrl);
    }

    private List<ReportsDashboard> processReportData(GeneratedReport report) {

        ReportOutputColumn donorAgency = report.leafHeaders.get(0);
        ReportOutputColumn pilar = report.leafHeaders.get(1);
        ReportOutputColumn implementationLevel = report.leafHeaders.get(2);
        ReportOutputColumn country = report.leafHeaders.get(3);
        ReportOutputColumn status = report.leafHeaders.get(4);

        List<ReportsDashboard> ampDashboardFunding = new ArrayList<>();
        for (ReportArea child : report.reportContents.getChildren()) {
            TextCell donorAgencyCell = (TextCell) child.getContents().get(donorAgency);
            if (child.getChildren() != null) {
                for (ReportArea pilarData : child.getChildren()) {
                    TextCell pilarCell = (TextCell) pilarData.getContents().get(pilar);
                    for (ReportArea implLevel : pilarData.getChildren()) {
                        TextCell implLevelCell = (TextCell) implLevel.getContents().get(implementationLevel);
                        for (ReportArea location : implLevel.getChildren()) {
                            TextCell countryCell = (TextCell) location.getContents().get(country);
                            for (ReportArea statusData : location.getChildren()) {
                                TextCell statusCell = (TextCell) statusData.getContents().get(status);
                                for (Map.Entry<ReportOutputColumn, ReportCell> content : statusData.getContents().entrySet()) {
                                    ReportOutputColumn col = content.getKey();
                                    System.out.println(statusData.getContents().get(status));
                                    if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || col.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                                        if (!col.parentColumn.originalColumnName.equals("Totals")) {
                                            ReportsDashboard fundingReport = new ReportsDashboard();
                                            fundingReport.setDonorAgency(donorAgencyCell.value.toString());
                                            fundingReport.setPillar(pilarCell.value.toString());
                                            fundingReport.setCountry(countryCell.value.toString());
                                            fundingReport.setImplimentationLevel(implLevelCell.value.toString());
                                            fundingReport.setStatus(statusCell.value.toString());
                                            fundingReport.setYear(col.parentColumn.originalColumnName);
                                            AmountCell amount = (AmountCell) content.getValue();
                                            if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)) {
                                                fundingReport.setActualCommitment(amount.extractValue());
                                            } else {
                                                fundingReport.setActualDisbursment(amount.extractValue());

                                            }
                                            fundingReport.setCurrency("USD");
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

        return ampDashboardFunding;
    }

    private GeneratedReport generateReport() {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", ArConstants.DONOR_TYPE);
        addColumnsToSpecification(spec);
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setShowOriginalCurrency(false);
        spec.setDisplayEmptyFundingRows(true);
        return EndpointUtils.runReport(spec);
    }

    private void addColumnsToSpecification(ReportSpecificationImpl spec) {
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTATION_LEVEL));
        spec.addColumn(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.addColumn(new ReportColumn(ColumnConstants.STATUS));
        spec.setHierarchies(spec.getColumns());
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_COMMITMENTS));
        spec.addMeasure(new ReportMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS));
    }

    public static List<Map<String, Object>> combineObjects(List<ReportsDashboard> ampDashboardFunding) {
        List<Map<String, Object>> data = new ArrayList<>();

        for (ReportsDashboard reportsDashboard : ampDashboardFunding) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("donorAgency", reportsDashboard.getDonorAgency());
            obj.put("pillar", reportsDashboard.getPillar());
            obj.put("country", reportsDashboard.getCountry());
            obj.put("year", reportsDashboard.getYear());
            obj.put("implimentationLevel", reportsDashboard.getImplimentationLevel());
            obj.put("actualCommitment", reportsDashboard.getActualCommitment());
            obj.put("actualDisbursment", reportsDashboard.getActualDisbursment());
            obj.put("status", reportsDashboard.getStatus());
            obj.put("currency", reportsDashboard.getCurrency());
            // Add the converted object to the list
            data.add(obj);
        }

        return resolveSimilarObjects(data);
    }

    private static List<Map<String, Object>> resolveSimilarObjects(List<Map<String, Object>> data) {
        Map<String, Map<String, Object>> groupedData = new HashMap<>();

        for (Map<String, Object> obj : data) {
            // Create a key based on the fields that should be the same
            String key = String.valueOf(obj.get("donorAgency")) +
                    String.valueOf(obj.get("pillar")) +
                    String.valueOf(obj.get("country")) +
                    String.valueOf(obj.get("year")) +
                    String.valueOf(obj.get("implimentationLevel")) +
                    String.valueOf(obj.get("status"));

            // If the key is already present, merge the values
            if (groupedData.containsKey(key)) {
                mergeValues(groupedData.get(key), obj);
            } else {
                // If the key is not present, add the object to the map
                groupedData.put(key, new HashMap<>(obj));
            }
        }

        // Convert the map values back to a list of combined objects
        return new ArrayList<>(groupedData.values());
    }

    private static void mergeValues(Map<String, Object> existingObj, Map<String, Object> newObj) {
        // Merge the fields "actualCommitment" and "actualDisbursment"
        Object existingCommitment = existingObj.get("actualCommitment");
        Object newCommitment = newObj.get("actualCommitment");
        existingObj.put("actualCommitment", existingCommitment != null ? existingCommitment : newCommitment);

        Object existingDisbursment = existingObj.get("actualDisbursment");
        Object newDisbursment = newObj.get("actualDisbursment");
        existingObj.put("actualDisbursment", existingDisbursment != null ? existingDisbursment : newDisbursment);
    }

    public static void sendReportsToServer(List<Map<String, Object>> ampDashboardFunding, String serverUrl) {
        try {
            // Create a URL object with the server's endpoint URL
            HttpURLConnection connection = getHttpURLConnection(serverUrl);
            // Convert the ampDashboardFunding to JSON using a JSON library (e.g., Gson)
            Gson gson = new Gson();
            String jsonData = gson.toJson(ampDashboardFunding);

            // Get the output stream of the connection
            try (OutputStream os = connection.getOutputStream()) {
                // Write the JSON data to the output stream
                os.write(jsonData.getBytes("UTF-8"));
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (e.g., HTTP 200 OK)
            if (responseCode == 200) {
                // The data has been successfully sent to the server
                System.out.println("Data sent successfully");
                logger.error("Data sent successfully to amp dashboard. HTTP Response Code: " + responseCode);
            } else {
                // Handle the error condition (e.g., log an error message)
                logger.error("Error sending data to amp dashboard. HTTP Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            logger.error("Exception raised when sending data to dashboard", e);
            e.printStackTrace();
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
