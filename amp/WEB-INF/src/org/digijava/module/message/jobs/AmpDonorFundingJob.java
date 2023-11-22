package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.reports.saiku.export.SaikuReportHtmlRenderer;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.reports.ReportFormParameters;
import org.digijava.kernel.ampapi.endpoints.reports.ReportsUtil;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.FilterUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.digijava.kernel.ampapi.endpoints.common.EPConstants.REPORT_TYPE_ID_MAP;

@Component
public class AmpDonorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    @Value("${ampDashboard.api}")
    private String ampDashboardUrl;
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ReportFormParameters formParams = new ReportFormParameters();
        List<String> additionalColumns = Arrays.asList("Donor Agency", "National Planning Objectives Level 1");
        List<String> additionalHierarchies = Arrays.asList("Donor Agency", "National Planning Objectives Level 1");
        List<String> additionalMeasures = Arrays.asList("Actual Commitments");
        formParams.setAdditionalColumns(additionalColumns);
        formParams.setAdditionalHierarchies(additionalHierarchies);
        formParams.setAdditionalMeasures(additionalMeasures);
        formParams.setGroupingOption("A");
        formParams.setReportType("D");
        formParams.setShowEmptyRows(true);
        formParams.setShowOriginalCurrency(false);
        formParams.setSummary(true);
        int reportType = ArConstants.DONOR_TYPE;
        if (formParams.getReportType() != null) {
            reportType = REPORT_TYPE_ID_MAP.get(formParams.getReportType());
        }
        ReportSpecificationImpl spec = new ReportSpecificationImpl("preview report", reportType);
        spec.setSummaryReport(Boolean.TRUE.equals(formParams.getSummary()));
        String groupingOption = formParams.getGroupingOption();
        ReportsUtil.setGroupingCriteria(spec, groupingOption);
        ReportsUtil.update(spec, formParams);
        SettingsUtils.applySettings(spec, formParams.getSettings(), true);
        FilterUtils.applyFilterRules(formParams.getFilters(), spec, null);
        GeneratedReport report = EndpointUtils.runReport(spec);

        List<ReportsDashboard> ampDashboardFunding = new ArrayList<ReportsDashboard>();
        for (ReportArea child: report.reportContents.getChildren()){
            if(child.getChildren() != null){
                for (ReportArea donorData: child.getChildren()){
                    Map<ReportOutputColumn, ReportCell> contents =donorData.getContents();

                    for (Map.Entry<ReportOutputColumn, ReportCell> content : donorData.getContents().entrySet()) {
                        ReportOutputColumn col = content.getKey();
                        ReportsDashboard fundingReport = new ReportsDashboard();

                        if (col.parentColumn != null && col.parentColumn.originalColumnName != null
                                && !col.parentColumn.originalColumnName.equals("Totals")) {
                            BigDecimal commitment = (BigDecimal) content.getValue().value;
                            fundingReport.setDonorAgency(child.getOwner().debugString);
                            fundingReport.setPillar(donorData.getOwner().debugString);
                            fundingReport.setYear(col.parentColumn.originalColumnName);
                            fundingReport.setActualCommitment(commitment.setScale(2, RoundingMode.HALF_UP));
                            ampDashboardFunding.add(fundingReport);
                        }
                    }
                }
            }
        }

        // Specify the server's endpoint URL
        String serverUrl = "http://localhost:8081/importDonorFunding";
        sendReportsToServer(ampDashboardFunding, ampDashboardUrl);
    }

    public void sendReportsToServer(List<ReportsDashboard> ampDashboardFunding, String serverUrl) {
        try {
            // Create a URL object with the server's endpoint URL
            URL url = new URL(serverUrl);

            // Open a connection to the server
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP request method to POST
            connection.setRequestMethod("POST");

            // Set the content type of the request
            connection.setRequestProperty("Content-Type", "application/json");

            // Enable input and output streams for the connection
            connection.setDoOutput(true);

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
            } else {
                // Handle the error condition (e.g., log an error message)
                System.out.println("Error sending data. HTTP Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
