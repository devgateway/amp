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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AmpDonorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpDonorFundingJob.class);
    private List<String> currencies = new ArrayList<>(java.util.Arrays.asList("USD", "EUR"));
    /*private String reportName = "preview report";
    private Integer reportType = ArConstants.DONOR_TYPE;*/

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //TODO make currency configurable
        List<ReportsDashboard> ampDashboardFundingCombined = new ArrayList<>();
        currencies.forEach(currency -> ampDashboardFundingCombined.addAll(getFundingByCurrency(currency)));
        //List<ReportsDashboard> ampDashboardFundingCombinedXDR = getFundingByCurrency("XDR");

        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_URL);
        sendReportsToServer(ampDashboardFundingCombined, serverUrl);
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
                                + "|" + report.getCountry()
                                + "|" + report.getImplementationLevel()
                                + "|" + report.getStatus()
                                //+ "|" + report.getYear()
                                + "|" + report.getReportingSystem()
                                + "|" + report.getTypeOfAssistance()
                                + "|" + report.getProcurementSystem(),
                        report -> report,
                        (report1, report2) -> {
                            report1.sumWith(report2);
                            return report1;
                        }))
                .values());
    }

    private List<ReportsDashboard> processReportData(GeneratedReport report, String currencyCode) {

        ReportOutputColumn donorAgency = report.leafHeaders.get(0);
        ReportOutputColumn implementingAgency = report.leafHeaders.get(1);
        ReportOutputColumn procurementSystemAgency = report.leafHeaders.get(2);
        ReportOutputColumn pilar = report.leafHeaders.get(3);
        ReportOutputColumn implementationLevel = report.leafHeaders.get(4);
        ReportOutputColumn country = report.leafHeaders.get(5);
        ReportOutputColumn status = report.leafHeaders.get(6);
        ReportOutputColumn typeOfAssistance = report.leafHeaders.get(7);
        ReportOutputColumn reportingSystem = report.leafHeaders.get(8); // Also called Forum


        List<ReportsDashboard> ampDashboardFunding = new ArrayList<>();
        for (ReportArea child : report.reportContents.getChildren()) {
            TextCell donorAgencyCell = (TextCell) child.getContents().get(donorAgency);
            if (child.getChildren() != null) {
                for (ReportArea implementingAgencyData : child.getChildren()) {
                    TextCell implementingAgencyCell = (TextCell) implementingAgencyData.getContents().get(implementingAgency);


                    for (ReportArea procurementSystemAgencyData : implementingAgencyData.getChildren()) {
                        TextCell procurementSystemAgencyCell = (TextCell) procurementSystemAgencyData.getContents().get(procurementSystemAgency);
                        for (ReportArea pilarData : procurementSystemAgencyData.getChildren()) {
                            TextCell pilarCell = (TextCell) pilarData.getContents().get(pilar);
                            for (ReportArea implLevel : pilarData.getChildren()) {
                                TextCell implLevelCell = (TextCell) implLevel.getContents().get(implementationLevel);
                                for (ReportArea location : implLevel.getChildren()) {
                                    TextCell countryCell = (TextCell) location.getContents().get(country);
                                    for (ReportArea statusData : location.getChildren()) {
                                        TextCell statusCell = (TextCell) statusData.getContents().get(status);
                                        for (ReportArea typeOfAssistanceData : statusData.getChildren()) {
                                            TextCell typeOfAssistanceCell = (TextCell) typeOfAssistanceData.getContents().get(typeOfAssistance);
                                            for (ReportArea reportSystemData : typeOfAssistanceData.getChildren()) {
                                                TextCell reportSystemCell = (TextCell) reportSystemData.getContents().get(reportingSystem);
                                                Long activityCount = 0L;
                                                for (Map.Entry<ReportOutputColumn, ReportCell> content : reportSystemData.getContents().entrySet()) {

                                                    ReportOutputColumn col = content.getKey();
                                                    if (col.originalColumnName.equals(ColumnConstants.ACTIVITY_COUNT)) {
                                                        IntCell amount = (IntCell) content.getValue();
                                                        activityCount = (Long) amount.value;
                                                        System.out.println("Activity Count: " + amount.value);
                                                    }
                                                    if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS) || col.originalColumnName.equals(MeasureConstants.ACTUAL_DISBURSEMENTS)) {
                                                        //if (!col.parentColumn.originalColumnName.equals("Totals")) {
                                                        if (col.parentColumn.originalColumnName.equals("Totals")) {
                                                            ReportsDashboard fundingReport = new ReportsDashboard();
                                                            fundingReport.setDonorAgency(donorAgencyCell.value.toString());
                                                            fundingReport.setImplementingAgency(implementingAgencyCell.value.toString());
                                                            fundingReport.setPillar(pilarCell.value.toString());
                                                            fundingReport.setCountry(countryCell.value.toString());
                                                            fundingReport.setImplementationLevel(implLevelCell.value.toString());
                                                            fundingReport.setStatus(statusCell.value.toString());
                                                            fundingReport.setReportingSystem(reportSystemCell.value.toString());
                                                            fundingReport.setTypeOfAssistance(typeOfAssistanceCell.value.toString());
                                                            fundingReport.setProcurementSystem(procurementSystemAgencyCell.value.toString());
                                                            //fundingReport.setYear(col.parentColumn.originalColumnName);
                                                            AmountCell amount = (AmountCell) content.getValue();
                                                            if (col.originalColumnName.equals(MeasureConstants.ACTUAL_COMMITMENTS)) {
                                                                fundingReport.setActualCommitment(amount.extractValue());
                                                            } else {
                                                                fundingReport.setActualDisbursement(amount.extractValue());
                                                            }
                                                            fundingReport.setActivityCount(activityCount);
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
                    }
                }
            }
        }
        return ampDashboardFunding;
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

        spec.setSummaryReport(true);
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
        spec.addColumn(new ReportColumn(ColumnConstants.DONOR_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTING_AGENCY));
        spec.addColumn(new ReportColumn(ColumnConstants.PROCUREMENT_SYSTEM));
        spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.addColumn(new ReportColumn(ColumnConstants.IMPLEMENTATION_LEVEL));
        spec.addColumn(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.addColumn(new ReportColumn(ColumnConstants.STATUS));
        spec.addColumn(new ReportColumn(ColumnConstants.TYPE_OF_ASSISTANCE));
        //TODO for GGW this is reporting system, for others it is Sectors
        spec.addColumn(new ReportColumn(ColumnConstants.PRIMARY_SECTOR));
        //spec.addColumn(new ReportColumn(ColumnConstants.REPORTING_SYSTEM));

        spec.setHierarchies(spec.getColumns());
        spec.addColumn(new ReportColumn(ColumnConstants.ACTIVITY_COUNT));
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
