package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.hibernate.jdbc.Work;
/*
 * skeleton class for amp_sector
 * 
 * */
public class SectorSkeleton implements Comparable<SectorSkeleton>, HierarchyListable, Identifiable {
	private String name;
	private Long id;
	private Long parentSectorId;
	private String code;
	private Collection<SectorSkeleton> childSectors;
	
	boolean translatable;
	
	
	public SectorSkeleton(Long id, String name, Long parentSectorId, String code) {
		this.id = id;
		this.name = name;
		this.parentSectorId = parentSectorId;
		this.code = code;
		this.childSectors = new TreeSet<SectorSkeleton>();
	}


	
    private static Long nullInsteadOfZero(long val) {
    	if (val == 0) {
    		return null;
    	}
    	else return val;
    }
    
	public void addChild(SectorSkeleton child) {
		this.childSectors.add(child);
	}
	
    /**
     * 
     * @return a list of ALL sectors
     */
	public static Map<Long, SectorSkeleton> getAllSectors(final Map<Long, SectorSkeleton> parents) {
        final Map<Long, SectorSkeleton> sectors = new HashMap<Long, SectorSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
			public void execute(Connection conn) throws SQLException {
				String parentIdsSubstring = Util.toCSStringForIN(parents.values());
				String condition = "where (deleted is null or deleted = false) and (parent_sector_id in (" + parentIdsSubstring +  " ))";
				ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_sector", 
						condition, TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");		
				ResultSet rs = v.fetch(null);
				while (rs.next()) {
					sectors.put(rs.getLong("amp_sector_id"), new SectorSkeleton(rs.getLong("amp_sector_id"), 
							 	rs.getString("name"), 
							 	nullInsteadOfZero(rs.getLong("parent_sector_id")),
							 	rs.getString("sector_code")));
				}
			}
		});
        return sectors;
    }
	
	/**
     * 
     * @return a list of all parent sectors
     */
	public static Map<Long, SectorSkeleton> getParentSectors(final Long secSchemeId) {
        final Map<Long, SectorSkeleton> sectors = new TreeMap<Long, SectorSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
				public void execute(Connection conn) throws SQLException {
					String condition = "where amp_sec_scheme_id = " + secSchemeId
							+ " and parent_sector_id is null and (deleted is null or deleted = false)";
					ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_sector", 
							condition, TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");		
					ResultSet rs = v.fetch(null);
					while (rs.next()) {
						sectors.put(rs.getLong("amp_sector_id"), new SectorSkeleton(rs.getLong("amp_sector_id"), 
													 	rs.getString("name"), 
													 	nullInsteadOfZero(rs.getLong("parent_sector_id")),
													    rs.getString("sector_code")));
					}
					
				}
			});
        return sectors;
    }

	@Override
	public String getLabel() {
		return this.name;
	}

	@Override
	public String getUniqueId() {
		return String.valueOf(this.id);
	}

	@Override
	public String getAdditionalSearchString() {
		return this.getCode();
	}

	@Override
	public boolean getTranslateable() {
		return this.translatable;
	}

	@Override
	public void setTranslateable(boolean translatable) {
		this.translatable = translatable;
		
	}

	@Override
	public Collection<? extends HierarchyListable> getChildren() {
		return this.childSectors;
	}
	public void setChildren(Collection<SectorSkeleton> sec) {
		this.childSectors = sec;
	}

	@Override
	public int getCountDescendants() {
		if (this.childSectors.isEmpty())
			return 0;
		int count = 0;
		for (SectorSkeleton sk : this.childSectors) {
			count += 1 + sk.getCountDescendants();
		}
		return count;
	}

	@Override
	public int compareTo(SectorSkeleton arg0) {
		return this.id.compareTo(arg0.id);
	}

	public Long getParentSectorId() {
		return parentSectorId;
	}

	public String getName() {
		return this.name;
	}
	
	public Long getId() {
		return this.id;
	}

	public String getCode() {
		return code;
	}

	@Override public String toString() {
		return String.format("%s (id: %d)", this.getName(), this.id);
	}

	@Override public Object getIdentifier() {
		return this.id;
	}
}
