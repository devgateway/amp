package org.digijava.kernel.entity.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.serializers.GeoCodedActivityVersionSerializer;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an activity that is being geo coded.
 */
public class GeoCodedActivity {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private GeoCodingProcess geoCodingProcess;

    /**
     * Queue Id on geo coding server. Used to retrieve results.
     */
    @JsonIgnore
    private Long queueId;

    @JsonSerialize(using = GeoCodedActivityVersionSerializer.class)
    @JsonUnwrapped
    private AmpActivityVersion activity;

    @ApiModelProperty("Geo coding status for this activity")
    private Status status;

    @ApiModelProperty("Geo coded locations. Populated only when status is complete.")
    private List<GeoCodedLocation> locations = new ArrayList<>();

    public enum Status {
        RUNNING, COMPLETED, ERROR, SAVED;

        public String getName() {
            return name();
        }
    }

    public GeoCodedActivity() {
    }

    public GeoCodedActivity(Long queueId, AmpActivityVersion activity) {
        this.queueId = queueId;
        this.activity = activity;

        status = Status.RUNNING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeoCodingProcess getGeoCodingProcess() {
        return geoCodingProcess;
    }

    public void setGeoCodingProcess(GeoCodingProcess geoCodingProcess) {
        this.geoCodingProcess = geoCodingProcess;
    }

    public Long getQueueId() {
        return queueId;
    }

    public void setQueueId(Long queueId) {
        this.queueId = queueId;
    }

    public AmpActivityVersion getActivity() {
        return activity;
    }

    public void setActivity(AmpActivityVersion activity) {
        this.activity = activity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<GeoCodedLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GeoCodedLocation> locations) {
        this.locations = locations;
    }

    public void addLocation(GeoCodedLocation location) {
        location.setActivity(this);
        locations.add(location);
    }
}
