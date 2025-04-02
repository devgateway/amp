package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
 * skeleton class for AmpSector
 * 
 * */
public class SectorSkeleton extends HierEntitySkeleton<SectorSkeleton> {
    
    public SectorSkeleton(Long id, String name, Long parentSectorId, String code) {
        super(id, name, code, parentSectorId);
    }
    
    /**
     * 
     * @return a list of all sectors beneath a given list of parents
     */
    public static Map<Long, SectorSkeleton> getAllSectors(final Map<Long, SectorSkeleton> parents) {
        final String parentIdsSubstring = Util.toCSStringForIN(parents.values());
        return HierEntitySkeleton.fetchTree("amp_sector", "where (deleted is null or deleted = false) and (parent_sector_id in (" + parentIdsSubstring +  " ))", sectorsFetcher);
    }
    
    /**
     * 
     * @return a list of all parent sectors
     */
    public static Map<Long, SectorSkeleton> getParentSectors(final Long secSchemeId) {
        final String where = "where amp_sec_scheme_id = " + secSchemeId + " and parent_sector_id is null and (deleted is null or deleted = false)";
        return HierEntitySkeleton.fetchTree("amp_sector", where, sectorsFetcher);
    }
    
    private static EntityFetcher<SectorSkeleton> sectorsFetcher = new EntityFetcher<SectorSkeleton>() {
        @Override public SectorSkeleton fetch(ResultSet rs) throws SQLException {
            return new SectorSkeleton(rs.getLong("amp_sector_id"), 
                    rs.getString("name"), 
                    nullInsteadOfZero(rs.getLong("parent_sector_id")),
                    rs.getString("sector_code"));
        }
        
        @Override public String[] getNeededColumnNames() {
            return new String[] {"amp_sector_id", "name", "parent_sector_id", "sector_code"};
        }
    };
}
