package org.digijava.module.esrigis.helpers;

/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * Simplified structure object to manage locations in JSON, see if can be replaced by AmpStructure 
 *
 */

public class Structure {
    private Long id;
    private String name;
    private String description;
    private String lat;
    private String lon;
    private String shape;
    private String type;
    private Long typeId;
    private Boolean hasImage;
    
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLat() {
        return lat;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getLon() {
        return lon;
    }
    public void setShape(String shape) {
        this.shape = shape;
    }
    public String getShape() {
        return shape;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    public Long getTypeId() {
        return typeId;
    }
    public Boolean getHasImage() {
        return hasImage;
    }
    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }
}
