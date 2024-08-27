package org.digijava.module.message.jobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.jdbc.Work;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmpCoreIndicatorFundingJob extends ConnectionCleaningJob implements StatefulJob {

    protected static final Logger logger = Logger.getLogger(AmpCoreIndicatorFundingJob.class);

    private static final Map<String, CoreIndicatorTypeDTO> indicatorMap = new HashMap<>();

    static {
        indicatorMap.put("Hectares of land under restoration",
                new CoreIndicatorTypeDTO("M ha", "ha_under_restoration",
                        "Hectares of land under restoration", 1000000D));
        indicatorMap.put("Tonnes of Co2EQ sequestered",
                new CoreIndicatorTypeDTO("M mt", "t_co2eq_sequestered",
                        "Tonnes of Co2EQ sequestered", 1000000D));
        indicatorMap.put("No of employment opportunities",
                new CoreIndicatorTypeDTO("M", "no_employments",
                        "No of employment opportunities", 1000000D));
        indicatorMap.put("Quantity of renewable energy consumed annually in MWH",
                new CoreIndicatorTypeDTO("M mwh", "r_energy_consumed",
                        "Quantity of renewable energy consumed annually in MWH", 1000000D));
        indicatorMap.put("Number of beneficiaries",
                new CoreIndicatorTypeDTO("M", "no_beneficiaries", "Number of beneficiaries", 1000000D));
    }

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //TODO move to MEService
        List<CoreIndicatorProgressDTO> resp = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("        select cv.id core_type_id,  ");
        query.append("        cv.category_value core_type_name,  ");
        query.append("        al.location_id country_id,  ");
        query.append("        cvl.location_name country_name,  ");
        query.append("        org.amp_org_id donor_id,  ");
        query.append("        org.name donor_name,  ");
        query.append("        i.program_id program_id,  ");
        query.append("        t.name program_name,  ");
        query.append("       ROUND(CAST(SUM(CASE WHEN iv.value_type = 0 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_target,  ");
        query.append("       ROUND(CAST(SUM(CASE WHEN iv.value_type = 1 THEN iv.value ELSE 0 END) AS NUMERIC), 2) AS value_type_actual  ");
        query.append("FROM amp_indicator i  ");
        query.append("         JOIN amp_indicator_connection ic ON ic.indicator_id = i.indicator_id  ");
        query.append("         JOIN amp_indicator_values iv ON iv.ind_connect_id = ic.id  ");
        query.append("         JOIN amp_category_value cv ON i.indicators_category = cv.id  ");
        query.append("         JOIN amp_activity_location al ON ic.activity_location = al.amp_activity_location_id  ");
        query.append("         JOIN amp_category_value_location cvl ON cvl.id = al.location_id  ");
        query.append("         JOIN amp_org_role oro ON oro.activity = oro.activity  ");
        query.append("         JOIN amp_organisation org ON oro.organisation = org.amp_org_id  ");
        query.append("         JOIN amp_theme t on t.amp_theme_id = i.program_id  ");
        query.append("         JOIN amp_activity aa on aa.amp_activity_id = oro.activity  ");
        query.append("WHERE ic.sub_clazz = 'a'  ");
        query.append("  AND iv.value_type IN (0, 1)  ");
        //    public static final int TARGET = 0;
        //    public static final int ACTUAL = 1;
        query.append("  AND oro.role = 1              "); //-- donor  ");
        query.append("GROUP BY cv.id,  ");
        query.append("         cv.category_value,  ");
        query.append("         al.location_id,  ");
        query.append("         cvl.location_name,  ");
        query.append("         org.amp_org_id,  ");
        query.append("         org.name  ,  ");
        query.append("         i.program_id,  ");
        query.append("         t.name ");
        query.append("ORDER BY cv.id,  ");
        query.append("         cv.category_value,  ");
        query.append("         al.location_id,  ");
        query.append("         cvl.location_name,  ");
        query.append("         org.amp_org_id,  ");
        query.append("         org.name,  ");
        query.append("         i.program_id,  ");
        query.append("         t.name");

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection connection) throws SQLException {
                RsInfo rsi = SQLUtils.rawRunQuery(connection, query.toString(), null);
                ResultSet rs = rsi.rs;
                while (rs.next()) {
                    Long coreTypeId = rs.getLong("core_type_id");
                    String coreTypeName = rs.getString("core_type_name");
                    Long countryId = rs.getLong("country_id");
                    String countryName = rs.getString("country_name");
                    Long donorId = rs.getLong("donor_id");
                    String donorName = rs.getString("donor_name");
                    String programName = rs.getString("program_name");
                    Long programId = rs.getLong("program_id");
                    Double actualValue = rs.getDouble("value_type_actual");
                    Double targetValue = rs.getDouble("value_type_target");

                    CoreIndicatorProgressDTO coreIndicatorProgressDTO = new CoreIndicatorProgressDTO();
                    coreIndicatorProgressDTO.setCountry(new CountryDTO(countryId, countryName));
                    coreIndicatorProgressDTO.setDonor(new DonorDTO(donorId, donorName));
                    coreIndicatorProgressDTO.setProgram(new ProgramDTO(programId, programName));
                    CoreIndicatorValueDTO coreIndicatorValueDTO = new CoreIndicatorValueDTO();
                    coreIndicatorValueDTO.setCoreIndicatorType(indicatorMap.get(coreTypeName));
                    coreIndicatorValueDTO.setActualValue(actualValue);
                    coreIndicatorValueDTO.setTargetValue(targetValue);
                    List<CoreIndicatorValueDTO> coreIndicatorValues = new ArrayList<>();
                    coreIndicatorValues.add(coreIndicatorValueDTO);
                    coreIndicatorProgressDTO.setValues(coreIndicatorValues);
                    resp.add(coreIndicatorProgressDTO);
                }
            }
        });
        String serverUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMP_DASHBOARD_CORE_INDICATOR_URL);
        sendReportsToServer(resp, serverUrl + "/update-core-indicator-progress");
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
