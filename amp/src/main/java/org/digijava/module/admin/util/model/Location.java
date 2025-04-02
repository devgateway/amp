package org.digijava.module.admin.util.model;

public class Location {
    private Long id;
    private Long location;
    private Double location_percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Double getLocation_percentage() {
        return location_percentage;
    }

    public void setLocation_percentage(Double location_percentage) {
        this.location_percentage = location_percentage;
    }
}
