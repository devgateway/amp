package org.digijava.kernel.entity.geocoding;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.dbentity.AmpTeamMember;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the geocoding process.
 *
 * @author Octavian Ciubotaru
 */
import org.digijava.module.aim.dbentity.AmpTeamMember;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "AMP_GEO_CODING")
public class GeoCodingProcess {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geoCodingProcessSeqGen")
    @SequenceGenerator(name = "geoCodingProcessSeqGen", sequenceName = "AMP_GEO_CODING_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_MEMBER_ID", nullable = false)
    @ApiModelProperty("Team member running the current geo coding process")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampTeamMemId")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("amp_team_member_id")
    private AmpTeamMember teamMember;

    @OneToMany(mappedBy = "geoCodingProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<GeoCodedActivity> activities = new ArrayList<>();


    @JsonIgnore

    public GeoCodingProcess() {
    }

    public GeoCodingProcess(AmpTeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpTeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(AmpTeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public List<GeoCodedActivity> getActivities() {
        return activities;
    }

    @JsonGetter(value = "activities")
    public List<GeoCodedActivity> getNonNullActivities() {
        return activities.stream().filter(act -> act != null).collect(Collectors.toList());
    }

    public void setActivities(List<GeoCodedActivity> activities) {
        this.activities = activities;
    }

    public void addActivity(GeoCodedActivity activity) {
        activity.setGeoCodingProcess(this);
        activities.add(activity);
    }
}
