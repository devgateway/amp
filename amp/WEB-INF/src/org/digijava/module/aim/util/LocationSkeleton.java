package org.digijava.module.aim.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/*
 * skeleton class for AmpCategoryValueLocation
 *
 * */
public class LocationSkeleton extends HierEntitySkeleton<LocationSkeleton> {
    protected Long cvId; // category value id
    protected Long templateId;
    protected Double lat;
    protected Double lon;

    public LocationSkeleton(LocationSkeletonBuilder locationSkeletonBuilder) {
        super(locationSkeletonBuilder.getId(),
                locationSkeletonBuilder.getLocName(),
                locationSkeletonBuilder.getCode(),
                locationSkeletonBuilder.getParentId());
        this.cvId = locationSkeletonBuilder.getCvId();
        this.templateId = locationSkeletonBuilder.getTemplateId();
        this.lat = locationSkeletonBuilder.getLat();
        this.lon = locationSkeletonBuilder.getLon();
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

    /**
     * @return a map of all locations from amp_category_value_location, indexed by their id'sstatic
     */
    public static Map<Long, LocationSkeleton> populateSkeletonLocationsList() {
        String condition = " WHERE deleted IS NOT TRUE";
        return HierEntitySkeleton.fetchTree("amp_category_value_location", condition, new EntityFetcher<LocationSkeleton>() {
            @Override
            public LocationSkeleton fetch(ResultSet rs) throws SQLException {
                return new LocationSkeletonBuilder()
                        .setId(nullInsteadOfZero(rs.getLong("id")))
                        .setLocName(rs.getString("location_name"))
                        .setCode(rs.getString("code"))
                        .setParentId(nullInsteadOfZero(rs.getLong("parent_location")))
                        .setCvId(nullInsteadOfZero(rs.getLong("parent_category_value")))
                        .setTemplateId(nullInsteadOfZero(rs.getLong("template_id")))
                        .setLat(nullInsteadOfZero(rs.getDouble("gs_lat")))
                        .setLon(nullInsteadOfZero(rs.getDouble("gs_long")))
                        .createLocationSkeleton();
            }

            @Override
            public String[] getNeededColumnNames() {
                return new String[]{"id", "location_name", "code", "parent_location", "parent_category_value",
                        "template_id", "gs_lat", "gs_long"};
            }
        });
    }
}
