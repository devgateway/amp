package org.digijava.kernel.entity.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.digijava.kernel.geocoding.service.GeoCodingService;

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

    private String entity;

    public GeoCodedField() {
    }

    public GeoCodedField(String name, String value, String entity) {
        this.name = name;
        this.value = value;
        this.entity = entity;
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

    public String getEntity() {
        return entity;
    }

    public void setEntity(final String entity) {
        this.entity = entity;
    }

    public String getLabel() {
        return GeoCodingService.getClient().getGeoCodingFieldLabel(name);
    }
}
