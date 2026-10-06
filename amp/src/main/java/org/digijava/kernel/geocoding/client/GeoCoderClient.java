package org.digijava.kernel.geocoding.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityInterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.*;

import static org.digijava.kernel.translator.util.TrnUtil.DEFAULT;

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
        client = ClientBuilder.newClient();
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

        List<Map<String, Object>> response = client.target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(jsonActivities, MediaType.APPLICATION_JSON_TYPE), new GenericType<List<Map<String, Object>>>() {});

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

            Invocation.Builder requestBuilder = client.target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE);

            GeoCodingOperation operation = requestBuilder.get(new GenericType<GeoCodingOperation>() {});

            if (operation.state.equals("PENDING") || operation.state.equals("PROCESSING")) {
                throw new GeoCodingNotProcessedException();
            }

            if (operation.state.equals("ERROR") || !operation.state.equals("PROCESSED")) {
                throw new GeoCodingErrorException();
            }

            return operation.extractData;
        } catch (javax.ws.rs.NotFoundException e) {
            throw new GeoCodingNotFoundException();
        } catch (Exception e) {
            throw new RuntimeException("Failed to obtain geo coding results", e);
        }
    }

    public String getGeoCodingFieldLabel(final String name) {
        APIField f = AmpFieldsEnumerator.getEnumerator().getActivityFields().stream()
                .filter(Objects::nonNull)
                .filter(field -> field.getFieldName().equals(name))
                .findFirst().orElse(null);
        return f == null ? null : f.getFieldLabel().get(DEFAULT, TLSUtils.getEffectiveLangCode());
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

        @JsonProperty("entity")
        private String entity;

        @JsonProperty("location")
        private GeoCodingLocation location;

        private Set<Long> acvlIds = new HashSet<>();

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

        public GeoCodingLocation getLocation() {
            return location;
        }

        public void setLocation(final GeoCodingLocation location) {
            this.location = location;
        }

        public void setAcvlIds(final Set<Long> acvlIds) {
            this.acvlIds = acvlIds;
        }

        public Set<Long> getAcvlIds() {
            return acvlIds;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(final String entity) {
            this.entity = entity;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoCodingLocation {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("geoname_id")
        private Long geoCode;

        @JsonProperty("lat")
        private Double lat;

        @JsonProperty("long")
        private Double lng;

        @JsonProperty("fcode")
        private String levelCode;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getGeoCode() {
            return geoCode;
        }

        public void setGeoCode(Long geoCode) {
            this.geoCode = geoCode;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public String getLevelCode() {
            return levelCode;
        }

        public void setLevelCode(String levelCode) {
            this.levelCode = levelCode;
        }

        @Override
        public String toString() {
            return "GeoCodingLocation{"
                    + "id=" + id
                    + ", name='" + name + '\''
                    + ", geoCode=" + geoCode
                    + ", lat=" + lat
                    + ", lng=" + lng
                    + ", levelCode='" + levelCode + '\''
                    + '}';
        }
    }
}