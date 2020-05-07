package org.digijava.module.aim.util;

public class LocationSkeletonBuilder {
    private Long id;
    private String locName;
    private String code;
    private Long parentId;
    private Long cvId;
    private Long templateId;
    private Double lat;
    private Double lon;

    public LocationSkeletonBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public LocationSkeletonBuilder setLocName(String locName) {
        this.locName = locName;
        return this;
    }

    public LocationSkeletonBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public LocationSkeletonBuilder setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public LocationSkeletonBuilder setCvId(Long cvId) {
        this.cvId = cvId;
        return this;
    }

    public LocationSkeletonBuilder setTemplateId(Long templateId) {
        this.templateId = templateId;
        return this;
    }

    public LocationSkeletonBuilder setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public LocationSkeletonBuilder setLon(Double lon) {
        this.lon = lon;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getLocName() {
        return locName;
    }

    public String getCode() {
        return code;
    }

    public Long getParentId() {
        return parentId;
    }

    public Long getCvId() {
        return cvId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public LocationSkeleton createLocationSkeleton() {
        return new LocationSkeleton(this);
    }
}