package org.digijava.module.aim.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/*
 * skeleton class for AmpTheme
 * 
 * */
public class AmpThemeSkeleton extends HierEntitySkeleton<AmpThemeSkeleton> {

	public AmpThemeSkeleton(Long id, String themeName, String code, Long parentId) {
		super(id, themeName, code, parentId);
	}
	
    
    /**
     * 
     * @return a map of all programs from amp_theme, indexed by their ids
     */
    public static Map<Long, AmpThemeSkeleton> populateThemesTree(long root) {
    	String condition = root <= 0 ? "" : "where amp_theme_id IN (select amp_theme_id from all_programs_with_levels where id0 = " + root + ")";
    	return HierEntitySkeleton.fetchTree("amp_theme", condition, new EntityFetcher<AmpThemeSkeleton>() {
    		@Override public AmpThemeSkeleton fetch(ResultSet rs) throws SQLException {
    			return new AmpThemeSkeleton(nullInsteadOfZero(rs.getLong("amp_theme_id")), 
					 	rs.getString("name"), 
					 	rs.getString("theme_code"),
					 	nullInsteadOfZero(rs.getLong("parent_theme_id")));
    		}
    	
    		@Override public String[] getNeededColumnNames() {
    			return new String[] {"amp_theme_id", "name", "theme_code", "parent_theme_id"};
    		}
    	});
    	};
    	
}
