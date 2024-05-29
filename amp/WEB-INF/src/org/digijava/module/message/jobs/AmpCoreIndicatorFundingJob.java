package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.*;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.services.MeService;
import org.digijava.kernel.ampapi.endpoints.gis.SettingsAndFiltersParameters;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.*;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class AmpCoreIndicatorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpDonorFundingJob.class);

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ReportSpecificationImpl
                spec = new ReportSpecificationImpl("indicator-data", ArConstants.INDICATOR_TYPE);
        spec.addColumn(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.addColumn(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.addColumn(new ReportColumn(ColumnConstants.INDICATOR_DONOR));
        spec.addColumn(new ReportColumn(ColumnConstants.INDICATOR_NAME));

        spec.getHierarchies().add(new ReportColumn(ColumnConstants.LOCATION_ADM_LEVEL_0));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.INDICATOR_DONOR));
        spec.getHierarchies().add(new ReportColumn(ColumnConstants.INDICATOR_NAME));

        spec.addMeasure(new ReportMeasure(MeasureConstants.INDICATOR_ACTUAL_VALUE));
        spec.addMeasure(new ReportMeasure(MeasureConstants.INDICATOR_TARGET_VALUE));
        spec.setSummaryReport(true);
        spec.setGroupingCriteria(GroupingCriteria.GROUPING_YEARLY);
        spec.setShowOriginalCurrency(false);
        spec.setDisplayEmptyFundingRows(false);
        ReportSettingsImpl reportSettings = new ReportSettingsImpl();
        spec.setSettings(reportSettings);

        new MeService().applySettingsAndFilters(new SettingsAndFiltersParameters(), spec);
        GeneratedReport report = EndpointUtils.runReport(spec);
        List<CoreIndicatorProgressDTO> resp = processReportData(report);
        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_URL);
        sendReportsToServer(resp, serverUrl + "/update-core-indicator-progress");
    }

    private List<CoreIndicatorProgressDTO> processReportData(GeneratedReport report) {

        ReportOutputColumn countryData = report.leafHeaders.get(0);
        ReportOutputColumn pilar = report.leafHeaders.get(1);
        ReportOutputColumn donorData = report.leafHeaders.get(2);
        ReportOutputColumn indicatorsData = report.leafHeaders.get(3);

        List<CoreIndicatorProgressDTO> ampDashboardCoreIndicator = new ArrayList<>();
        for (ReportArea child : report.reportContents.getChildren()) {
            TextCell countryDataCell = (TextCell) child.getContents().get(countryData);
            if (child.getChildren() != null) {
                for (ReportArea pilarData : child.getChildren()) {
                    TextCell pilarCell = (TextCell) pilarData.getContents().get(pilar);
                    for (ReportArea donor : pilarData.getChildren()) {
                        TextCell donorCell = (TextCell) donor.getContents().get(donorData);
                        CoreIndicatorProgressDTO fundingReport = new CoreIndicatorProgressDTO();
                        DonorDTO donorDTO = new DonorDTO();
                        CountryDTO countryDTO = new CountryDTO();
                        ProgramDTO programDTO = new ProgramDTO();
                        donorDTO.setName(donorCell.value.toString());
                        countryDTO.setName(countryDataCell.value.toString());
                        programDTO.setName(pilarCell.value.toString());
                        fundingReport.setProgram(programDTO);
                        fundingReport.setCountry(countryDTO);
                        fundingReport.setDonor(donorDTO);
                        List<CoreIndicatorValueDTO> valuesList = new ArrayList<CoreIndicatorValueDTO>();
                        for(ReportArea indicator : donor.getChildren()){
                            CoreIndicatorValueDTO value = new CoreIndicatorValueDTO();
                            TextCell indicatorCell = (TextCell) indicator.getContents().get(indicatorsData);
//                                value.setIndicator(indicatorCell.value.toString());
//                                value.setIndicator_id(indicatorCell.entityId);

                            for (Map.Entry<ReportOutputColumn, ReportCell> entry : indicator.getContents().entrySet()) {
                                ReportOutputColumn col = entry.getKey();

                                if (col.parentColumn != null
                                        && col.originalColumnName.equals(MeasureConstants.INDICATOR_ACTUAL_VALUE)
                                        && col.parentColumn.parentColumn != null
                                        && col.parentColumn.parentColumn.originalColumnName.equals(
                                        NiReportsEngine.FUNDING_COLUMN_NAME)
                                        && col.parentColumn.parentColumn.parentColumn == null) {
                                    AmountCell cell = (AmountCell) entry.getValue();
                                    BigDecimal actualValue = cell.extractValue();
                                    value.setActualValue(actualValue.doubleValue());
                                }
                                if (col.parentColumn != null
                                        && col.originalColumnName.equals(MeasureConstants.INDICATOR_TARGET_VALUE)
                                        && col.parentColumn.parentColumn != null
                                        && col.parentColumn.parentColumn.originalColumnName.equals(
                                        NiReportsEngine.FUNDING_COLUMN_NAME)
                                        && col.parentColumn.parentColumn.parentColumn == null) {
                                    AmountCell cell = (AmountCell) entry.getValue();
                                    BigDecimal targetValue = cell.extractValue();
                                    value.setTargetValue(targetValue.doubleValue());
                                }
                            }

                            // Add category value type for the indicator
                            CoreIndicatorTypeDTO indicatorType = new CoreIndicatorTypeDTO();
                            AmpIndicator existingInd = getIndicatorById(indicatorCell.entityId);
                            if (existingInd != null && existingInd.getIndicatorsCategory() != null){
                                indicatorType.setName(existingInd.getIndicatorsCategory().getValue());
                                if(indicatorType.getName().contains("Hectares of land under restoration")){
                                    indicatorType.setUnit("M ha");
                                    indicatorType.setCoreType("ha_under_restoration");
                                } else if(indicatorType.getName().contains("Tonnes of Co2EQ sequestered")){
                                    indicatorType.setUnit("M mt");
                                    indicatorType.setCoreType("t_co2eq_sequestered");
                                } else if(indicatorType.getName().contains("No of employment opportunities")){
                                    indicatorType.setUnit("M");
                                    indicatorType.setCoreType("no_employments");
                                } else if(indicatorType.getName().contains("Quantity of renewable energy consumed annually in MWH")){
                                    indicatorType.setUnit("M mwh");
                                    indicatorType.setCoreType("r_energy_consumed");
                                } else if(indicatorType.getName().contains("Number of beneficiaries")){
                                    indicatorType.setUnit("M");
                                    indicatorType.setCoreType("no_beneficiaries");
                                } else {
                                    // Adding unknown here fo easy debug
                                    indicatorType.setUnit("Unknown");
                                    indicatorType.setCoreType("Unknown");
                                }
                            } else {
                                indicatorType.setName("Unknown");
                                indicatorType.setUnit("Unknown");
                                indicatorType.setCoreType("Unknown");
                            }
                            value.setCoreIndicatorType(indicatorType);
                            valuesList.add(value);
                        }
                        fundingReport.setValues(valuesList);
                        ampDashboardCoreIndicator.add(fundingReport);
                    }
                }
            }
        }

        return ampDashboardCoreIndicator;
    }

    private AmpIndicator getIndicatorById(Long indicatorId){
        try {
            return IndicatorUtil.getIndicator(indicatorId);
        } catch (DgException e) {
            throw new RuntimeException("Failed to load indicator");
        }
    }
    public static void sendReportsToServer(List<CoreIndicatorProgressDTO> ampCoreIndicatorCoreData, String serverUrl) {
        try {
            // Create a URL object with the server's endpoint URL
            HttpURLConnection connection = getHttpURLConnection(serverUrl);
            // Create a Gson instance with custom serializer and Convert the ampDashboardFunding to JSON using a JSON library (e.g., Gson)
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CoreIndicatorValueDTO.class, new CoreIndicatorValueDTOSerializer())
                    .create();

            String jsonData = gson.toJson(ampCoreIndicatorCoreData);

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
        String indicatorCoreServerUsername = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_USERNAME);
        String indicatorCoreServerPassword = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_PASSWORD);


        // Encode username and password
        String auth = indicatorCoreServerUsername + ":" + indicatorCoreServerPassword; // Replace with actual username and password
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeaderValue = "Basic " + encodedAuth;

        // Set the Authorization header
        connection.setRequestProperty("Authorization", authHeaderValue);

        // Set the HTTP request method to POST
        connection.setRequestMethod("POST");

        // Set the content type of the request
        connection.setRequestProperty("Content-Type", "application/json");

        // Enable input and output streams for the connection
        connection.setDoOutput(true);
        return connection;
    }
}
