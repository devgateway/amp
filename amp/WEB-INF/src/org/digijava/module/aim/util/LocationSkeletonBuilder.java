package org.digijava.module.aim.util;

public class LocationSkeletonBuilder {

    private LocationSkeleton locationSkeleton;

    public LocationSkeletonBuilder(Long id, String locName, String code, Long parentId) {
        locationSkeleton = new LocationSkeleton(id, locName, code, parentId);
    }

    public LocationSkeletonBuilder withCvId(Long cvId) {
        locationSkeleton.cvId = cvId;
        return this;
    }

    public LocationSkeletonBuilder withTemplateId(Long templateId) {
        locationSkeleton.templateId = templateId;
        return this;
    }

    public LocationSkeletonBuilder withLat(Double lat) {
        locationSkeleton.lat = lat;
        return this;
    }

    public LocationSkeletonBuilder withLon(Double lon) {
        locationSkeleton.lon = lon;
        return this;
    }

    public LocationSkeletonBuilder withGroup(Long groupId) {
        locationSkeleton.groupId = groupId;
        return this;
    }

    public LocationSkeletonBuilder withDescription(String description) {
        locationSkeleton.description = description;
        return this;
    }

    public LocationSkeleton getLocationSkeleton() {
        return locationSkeleton;
    }
}