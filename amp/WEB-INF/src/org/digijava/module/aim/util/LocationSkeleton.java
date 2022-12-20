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
    protected Long groupId;
    protected String description;

    protected LocationSkeleton(Long id, String locName, String code, Long parentId) {
        super(id, locName, code, parentId);
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

    public Long getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @return a map of all locations from amp_category_value_location, indexed by their id'sstatic
     */
    public static Map<Long, LocationSkeleton> populateSkeletonLocationsList() {
        String condition = " WHERE deleted IS NOT TRUE";
        return HierEntitySkeleton.fetchTree("amp_category_value_location", condition, new EntityFetcher<LocationSkeleton>() {
            @Override
            public LocationSkeleton fetch(ResultSet rs) throws SQLException {
                return new LocationSkeletonBuilder(nullInsteadOfZero(rs.getLong("id")), rs.getString("location_name"),
                        rs.getString("code"), nullInsteadOfZero(rs.getLong("parent_location")))
                        .withCvId(nullInsteadOfZero(rs.getLong("parent_category_value")))
                        .withTemplateId(nullInsteadOfZero(rs.getLong("template_id")))
//                        .withLat(nullInsteadOfZero(rs.getDouble("gs_lat")))
//                        .withLon(nullInsteadOfZero(rs.getDouble("gs_long")))
                        .withGroup(nullInsteadOfZero(rs.getLong("amp_category_value_group_id")))
                        .withDescription(rs.getString("description"))
                        .getLocationSkeleton();
            }

            @Override
            public String[] getNeededColumnNames() {
                return new String[]{"id", "location_name", "code", "parent_location", "parent_category_value",
                        "template_id", "gs_lat", "gs_long", "amp_category_value_group_id", "description"};
            }
        });
    }
}
