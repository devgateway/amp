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

	public LocationSkeleton(Long id, String locName, String code, Long parentId, Long cvId) {
		super(id, locName, code, parentId);
		this.cvId = cvId;
	}
	   
	public Long getCvId() {
		return cvId;
	}
	
    /**
     * 
     * @return a map of all locations from amp_category_value_location, indexed by their id'sstatic
     */
    public static Map<Long, LocationSkeleton> populateSkeletonLocationsList() {
    	return HierEntitySkeleton.fetchTree("amp_category_value_location", "", new EntityFetcher<LocationSkeleton>() {
    		@Override public LocationSkeleton fetch(ResultSet rs) throws SQLException {
    			return new LocationSkeleton(nullInsteadOfZero(rs.getLong("id")), 
					 	rs.getString("location_name"), 
					 	rs.getString("code"),
					 	nullInsteadOfZero(rs.getLong("parent_location")), 
					 	nullInsteadOfZero(rs.getLong("parent_category_value")));
    		}
    		
    		@Override public String[] getNeededColumnNames() {
    			return new String[] {"id", "location_name", "code", "parent_location", "parent_category_value"};
    		}
    	});
    	};
}
