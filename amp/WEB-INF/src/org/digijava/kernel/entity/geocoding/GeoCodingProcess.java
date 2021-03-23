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
public class GeoCodingProcess {

    @JsonIgnore
    private Long id;

    @ApiModelProperty("Team member running the current geo coding process")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampTeamMemId")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("amp_team_member_id")
    private AmpTeamMember teamMember;

    @JsonIgnore
    private List<GeoCodedActivity> activities = new ArrayList<>();

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
