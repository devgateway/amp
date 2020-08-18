package org.digijava.kernel.entity.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Geo coded field. Can map to 0, 1 or more locations.
 *
 * @author Octavian Ciubotaru
 */
public class GeoCodedField {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private GeoCodedLocation location;

    private String name;

    private String value;

    public GeoCodedField() {
    }

    public GeoCodedField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeoCodedLocation getLocation() {
        return location;
    }

    public void setLocation(GeoCodedLocation location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
