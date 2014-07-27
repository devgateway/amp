package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.hibernate.jdbc.Work;
/*
 * skeleton class for amp_category_value_location
 * 
 * */
public class LocationSkeleton implements Comparable<LocationSkeleton>, HierarchyListable {
	private Long id;//location id	
	private Long cvId; // category value id
	private String name;	//location name
	private Long locParentId; //location parent (country<-region<-... or something)
	private boolean translatable = false; 
	private String code; 
	private Set<LocationSkeleton> childLocations;

	public LocationSkeleton(Long id, String locName, String code, Long parentId, Long cvId) {
		this.id = id;
		this.name = locName;
		this.code = code;		
		this.locParentId = parentId;
		this.cvId = cvId;
		this.childLocations = new HashSet<LocationSkeleton>();
	}
	
	@Override
	/**
	 * hashcode based on id
	 */	
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	/**
	 * equals based on id
	 */
	public boolean equals(Object o) {
		if (!(o instanceof LocationSkeleton)) return false;
		return this.id.equals(((LocationSkeleton) o).id);
	}
	
	public String getCode() {
		return code;
	}
	
	public Long getAmpLocId() {
		return id;
	}
	
	public String getAmpLocName() {
		return this.name;
	}
	

	public Long getAmpLocParentId() {
		return this.locParentId;
	}
	
	public void addChild(LocationSkeleton child) {
		this.childLocations.add(child);
	}
	
	public Set<LocationSkeleton> getChildLocations() {
		return this.childLocations;
	}
	
	@Override
	/**
	 * compareTo based on id
	 */
	public int compareTo(LocationSkeleton o) {
		return this.id.compareTo(o.id);
	}
	
	@Override
	public String toString() { 
		return String.format("%s (id: %d, parent: %d)", this.getAmpLocName(), this.getAmpLocId(), this.getAmpLocParentId());
	}
	
	@Override
	public String getLabel() {
		return this.name;
	}
	
	@Override
	/**
	 * return toString of id
	 */
	public String getUniqueId() {
 
		return String.valueOf(this.getAmpLocId());
	}
	
	@Override
	public String getAdditionalSearchString() {
		return code;
	}
	
	@Override
	public boolean getTranslateable() {
		return translatable;
	}

	@Override
	public void setTranslateable(boolean translatable) {
		this.translatable = translatable;
	}
	
	@Override
	public Collection<? extends HierarchyListable> getChildren() {
		return this.childLocations;
	}
	
	@Override
	public int getCountDescendants() {
		int ret = 1;
		if (this.getChildren() != null) {
			for (HierarchyListable hl : this.getChildren())
				ret += hl.getCountDescendants();
		}
		return ret;
	}

	public Long getCvId() {
		return cvId;
	}
	
    private static Long nullInsteadOfZero(long val) {
    	if (val == 0) {
    		return null;
    	}
    	else return val;
    }
    
    /**
     * 
     * @return a map of all locations from amp_category_value_location, indexed by their id's
     */
    public static Map<Long, LocationSkeleton>  populateSkeletonLocationsList() {
        final Map<Long, LocationSkeleton> locations = new HashMap<Long, LocationSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
				public void execute(Connection conn) throws SQLException {
					ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_category_value_location", 
							"", TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");		
					ResultSet rs = v.fetch(null);
					while (rs.next()) {
						locations.put(rs.getLong("id"), new LocationSkeleton(nullInsteadOfZero(rs.getLong("id")), 
													 	rs.getString("location_name"), 
													 	rs.getString("code"),
													 	nullInsteadOfZero(rs.getLong("parent_location")), 
													 	nullInsteadOfZero(rs.getLong("parent_category_value"))));
					}
					for (Map.Entry<Long, LocationSkeleton> entry : locations.entrySet()) {
					    LocationSkeleton loc = entry.getValue();
					    if (loc.getAmpLocParentId() != null) {
					    	if (locations.get(loc.getAmpLocParentId()) != null)
					    		locations.get(loc.getAmpLocParentId()).addChild(loc);
					    }
					}					
				}
			});
        return locations;
    }
}
