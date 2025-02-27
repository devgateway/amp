package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AmpDonorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpDonorFundingJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<ReportsDashboard> ampDashboardFundingCombinedUSD = getFundingByCurrency("USD");
        List<ReportsDashboard> ampDashboardFundingCombinedEUR = getFundingByCurrency("EUR");
        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_URL);
        ampDashboardFundingCombinedUSD.addAll(ampDashboardFundingCombinedEUR);

        sendReportsToServer(ampDashboardFundingCombinedUSD, serverUrl);
    }

    @NotNull
    private List<ReportsDashboard> getFundingByCurrency(String currencyCode) {
        GeneratedReport fundingReport = generateReport(currencyCode);
        List<ReportsDashboard> ampDashboardFunding = processReportData(fundingReport, currencyCode);
        // The ampDashboardFunding data contains objects for commitments and disbursment differently in
        // separate objects. We need to combine them in same object combining commitment and disbursment values.
        List<ReportsDashboard> ampDashboardFundingCombined = ampDashboardFunding.stream()
                .collect(Collectors.toMap(
                        report -> report.getDonorAgency() + "|" + report.getPillar() + "|" +
                                report.getCountry() + "|" + report.getImplementationLevel() + "|" +
                                report.getStatus() + "|" + report.getYear(),
                        report -> report,
                        (report1, report2) -> {
                            report1.sumWith(report2);
                            return report1;
                        }))
                .values()
                .stream()
                .collect(Collectors.toList());
        return ampDashboardFundingCombined;
    }

    private List<ReportsDashboard> processReportData(GeneratedReport report, String currencyCode) {

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
                                    if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || col.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                                        if (!col.parentColumn.originalColumnName.equals("Totals")) {
                                            ReportsDashboard fundingReport = new ReportsDashboard();
                                            fundingReport.setDonorAgency(donorAgencyCell.value.toString());
                                            fundingReport.setPillar(pilarCell.value.toString());
                                            fundingReport.setCountry(countryCell.value.toString());
                                            fundingReport.setImplementationLevel(implLevelCell.value.toString());
                                            fundingReport.setStatus(statusCell.value.toString());
                                            fundingReport.setYear(col.parentColumn.originalColumnName);
                                            AmountCell amount = (AmountCell) content.getValue();
                                            if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)) {
                                                fundingReport.setActualCommitment(amount.extractValue());
                                            } else {
                                                fundingReport.setActualDisbursement(amount.extractValue());
                                            }
                                            fundingReport.setCurrency(currencyCode);
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

    private GeneratedReport generateReport(String currencyCode) {
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", ArConstants.DONOR_TYPE);
        addColumnsToSpecification(spec);
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setShowOriginalCurrency(false);
        spec.setDisplayEmptyFundingRows(true);
        ReportSettingsImpl reportSettings = new ReportSettingsImpl();
        spec.setSettings(reportSettings);

        reportSettings.setCurrencyCode(currencyCode);
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

    public static void sendReportsToServer(List<ReportsDashboard> ampDashboardFunding, String serverUrl) {
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
                logger.debug("Data sent successfully to amp dashboard. HTTP Response Code: " + responseCode);
            } else {
                // Handle the error condition (e.g., log an error message)
                logger.debug("Error sending data to amp dashboard. HTTP Response Code: " + responseCode);
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
