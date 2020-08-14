package org.digijava.kernel.entity.geocoding;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

/**
 * @author Octavian Ciubotaru
 */
public class GeoCodedLocation {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private GeoCodedActivity activity;

    @ApiModelProperty("True if location was accepted, false if rejected. "
            + "If null, then the location is neither rejected or accepted.")
    private Boolean accepted;

    @ApiModelProperty("Location Id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("amp_category_value_location_id")
    private AmpCategoryValueLocations location;

    @ApiModelProperty("Fields that refer to this location")
    private List<GeoCodedField> fields = new ArrayList<>();

    public GeoCodedLocation() {
    }

    public GeoCodedLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeoCodedActivity getActivity() {
        return activity;
    }

    public void setActivity(GeoCodedActivity activity) {
        this.activity = activity;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public AmpCategoryValueLocations getLocation() {
        return location;
    }

    public void setLocation(AmpCategoryValueLocations location) {
        this.location = location;
    }

    public List<GeoCodedField> getFields() {
        return fields;
    }

    public void setFields(List<GeoCodedField> fields) {
        this.fields = fields;
    }

    public void addField(GeoCodedField field) {
        field.setLocation(this);
        fields.add(field);
    }
}
