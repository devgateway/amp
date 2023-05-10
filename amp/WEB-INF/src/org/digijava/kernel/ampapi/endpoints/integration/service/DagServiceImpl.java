package org.digijava.kernel.ampapi.endpoints.integration.service;

import com.sun.jersey.api.client.ClientResponse;
import org.digijava.kernel.ampapi.endpoints.integration.IntegraionUploadsDirectoryConfig;
import org.digijava.kernel.ampapi.endpoints.integration.dto.DagRunsRequestDTO;
import org.digijava.kernel.ampapi.endpoints.integration.dto.DagRunsResponseDTO;
import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;
import org.digijava.kernel.restclient.RestClient;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static org.digijava.module.aim.helper.GlobalSettingsConstants.EXEMPT_ORGANIZATION_DOCUMENTS;

@Component
public class DagServiceImpl implements DagService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DagService.class.getName());

    private static DagServiceImpl dagService;
    private static IntegraionUploadsDirectoryConfig uploadsDirectoryConfig;


    private DagServiceImpl() {
    }


    public static DagServiceImpl getInstance(){
        if (dagService == null){
            dagService = new DagServiceImpl();
            uploadsDirectoryConfig = IntegraionUploadsDirectoryConfig.getInstance();
        }
        FeaturesUtil.getGlobalSettingValueLong(EXEMPT_ORGANIZATION_DOCUMENTS);
        return dagService;
    }

    //    @Value("${airflow.api.url}")
    private String endpointUrl =FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INTEGRATION_AIRFLOW_URL);
    private String airflowUsername = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INTEGRATION_AIRFLOW_USERNAME);
    private String airflowUserPassword = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INTEGRATION_AIRFLOW_PASSWORD);
    private String dagRunsUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.INTEGRATION_AIRFLOW_DAG_RUN_URL);

    public DagRunsResponseDTO dagRuns(DagRunsRequestDTO dagRunsRequest) {
        String auth = airflowUsername + ":" + airflowUserPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        now.plusMinutes(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String executionDateString = now.format(formatter);

        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, "application/json");
        headers.add(AUTHORIZATION, authHeader);

        Map<String, Object> conf = new HashMap<>();
        conf.put("username", dagRunsRequest.getUsername());
        conf.put("password", dagRunsRequest.getPassword());
        conf.put("workspaceId", dagRunsRequest.getWorkspaceId());
        conf.put("fileName", dagRunsRequest.getFileName());
        conf.put("uploadDir", uploadsDirectoryConfig.getUploadsDir());

        Map<String, Object> payload = new HashMap<>();
        payload.put("conf", conf);
        payload.put("execution_date", executionDateString);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        RestClient client = RestClient.getInstance(RestClient.Type.JSON);
        ClientResponse response = client.requestPOST(endpointUrl + dagRunsUrl.replace("#template", dagRunsRequest.getDagId()), requestEntity);
        if (response.getStatus() == HttpStatus.OK.value()) {
            return ObjectMapperUtils.readValueFromString(response.getEntity(String.class), DagRunsResponseDTO.class);
        } else {
            LOGGER.error("DAG run failed with status code: {}", response.getStatus());
            throw new RuntimeException("Failed to execute DAG run");
        }
    }
}
