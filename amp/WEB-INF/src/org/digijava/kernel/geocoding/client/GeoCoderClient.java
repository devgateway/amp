package org.digijava.kernel.geocoding.client;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Octavian Ciubotaru
 */
public class GeoCoderClient {

    private final Client client;

    private static final List<String> GEO_FIELDS = ImmutableList.of(
            "amp_id",
            "project_title",
            "objective",
            "description",
            "project_comments",
            "results",
            "program_description");

    private static final String PATH_ACTIVITY_PROCESS = "activity/process";
    private static final String PATH_QUEUE = "queue";
    private static final String PATH_AMP_LOCATIONS = "amp/locations";

    public GeoCoderClient() {
        client = Client.create();
    }

    public String getBaseUrl() {
        return FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GEO_CODER_URL);
    }

    /**
     * Send activities for geo coding.
     *
     * @param activities activities to be geo coded
     * @return queue ids, the list will have the same size as activities parameter
     */
    public List<Long> processActivities(List<AmpActivityVersion> activities) {
        List<Map<String, Object>> jsonActivities = new ArrayList<>();

        for (AmpActivityVersion activity : activities) {
            jsonActivities.add(getActivityAsJson(activity));
        }

        URI uri = UriBuilder.fromUri(getBaseUrl()).path(PATH_ACTIVITY_PROCESS).build();

        List<Map<String, Object>> response = client.resource(uri)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(new GenericType<List<Map<String, Object>>>() { }, jsonActivities);

        List<Long> queueIds = new ArrayList<>();
        for (Map<String, Object> queue : response) {
            queueIds.add(Long.valueOf(queue.get("queue_id").toString()));
        }
        return queueIds;
    }

    private Map<String, Object> getActivityAsJson(AmpActivityVersion activity) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(ActivityEPConstants.FILTER_FIELDS, GEO_FIELDS);

        return ActivityInterchangeUtils.getActivity(activity, filter);
    }

    /**
     *
     * @param queueId
     * @return geo coded fields with their corresponding locations
     * @throws GeoCodingNotFoundException unknown geo coding
     * @throws GeoCodingNotProcessedException repeat later to find out the result of the geo coding
     */
    public List<GeoCodingResult> getGeoCodingResults(Long queueId) {
        try {
            URI uri = UriBuilder.fromUri(getBaseUrl()).path(PATH_QUEUE)
                    .queryParam("id", queueId)
                    .build();

            GeoCodingOperation operation = client.resource(uri)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get(new GenericType<GeoCodingOperation>() { });

            if (operation.state.equals("PENDING") || operation.state.equals("PROCESSING")) {
                throw new GeoCodingNotProcessedException();
            }

            if (operation.state.equals("ERROR") || !operation.state.equals("PROCESSED")) {
                throw new GeoCodingErrorException();
            }

            return operation.extractData;
        } catch (UniformInterfaceException e) {
            if (e.getResponse().getStatus() == ClientResponse.Status.NOT_FOUND.getStatusCode()) {
                throw new GeoCodingNotFoundException();
            } else {
                throw new RuntimeException("Failed to obtain geo coding results", e);
            }
        }
    }

    /**
     * Used to denote when geo coding server does not know of the requested geo coding process.
     */
    public static class GeoCodingNotFoundException extends RuntimeException {
    }

    /**
     * Geo coding is available but not yet ready.
     */
    public static class GeoCodingNotProcessedException extends RuntimeException {
    }

    /**
     * An error occured during the geo coding.
     */
    public static class GeoCodingErrorException extends RuntimeException {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoCodingOperation {

        private String state;

        @JsonProperty("extract_data")
        private List<GeoCodingResult> extractData;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<GeoCodingResult> getExtractData() {
            return extractData;
        }

        public void setExtractData(List<GeoCodingResult> extractData) {
            this.extractData = extractData;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoCodingResult {

        @JsonProperty("field_name")
        private String field;

        @JsonProperty("text")
        private String value;

        @JsonProperty("amp_location_id")
        private Set<Long> acvlIds;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Set<Long> getAcvlIds() {
            return acvlIds;
        }

        public void setAcvlIds(Set<Long> acvlIds) {
            this.acvlIds = acvlIds;
        }
    }

    public void sendLocations(List<AmpCategoryValueLocations> acvLocations) {
        List<Map<?, ?>> locations = acvLocations.stream()
                .map(this::toGeoCoderLocation)
                .collect(toList());

        client.resource(UriBuilder.fromUri(getBaseUrl()).path(PATH_AMP_LOCATIONS).build())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .put(locations);
    }

    private Map<?, ?> toGeoCoderLocation(AmpCategoryValueLocations acvl) {
        Map<Object, Object> loc = new HashMap<>();

        loc.put("amp_location_id", acvl.getId());
        loc.put("name", acvl.getName());

        int lvl = 0;
        AmpCategoryValueLocations i = acvl;
        while (i.getParentLocation() != null) {
            i = i.getParentLocation();
            lvl++;
        }
        loc.put("admin_level_code", "ADM" + lvl);

        loc.put("description", acvl.getDescription());
        loc.put("geo_code", acvl.getGeoCode());
        loc.put("gs_lat", acvl.getGsLat() != null ? Double.parseDouble(acvl.getGsLat()) : null);
        loc.put("gs_long", acvl.getGsLong() != null ? Double.parseDouble(acvl.getGsLong()) : null);

        return loc;
    }
}
