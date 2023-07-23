package org.digijava.kernel.entity.geocoding;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.serializers.GeoCodedActivityVersionSerializer;
import org.digijava.module.aim.dbentity.AmpActivityVersion;

/**
 * Represents an activity that is being geo coded.
 */

import org.digijava.module.aim.dbentity.AmpActivityVersion;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "AMP_GEO_CODED_ACTIVITY")
public class GeoCodedActivity {
    @Id
    @JsonIgnore

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geoCodedActivitySeqGen")
    @SequenceGenerator(name = "geoCodedActivitySeqGen", sequenceName = "AMP_GEO_CODED_ACTIVITY_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "GEO_CODING_ID", nullable = false)
    private GeoCodingProcess geoCodingProcess;

    @Column(name = "QUEUE_ID", nullable = false)
    @JsonIgnore

    private Long queueId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_VERSION_ID", unique = true, nullable = false)
    private AmpActivityVersion activity;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("Geo coding status for this activity")
    private Status status;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "INDEX")
    @ApiModelProperty("Geo coded locations. Populated only when status is complete.")
    private List<GeoCodedLocation> locations;


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
