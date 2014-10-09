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
    public static Map<Long, AmpThemeSkeleton> populateThemesTree(boolean includeSubThemes) {
    	String condition = includeSubThemes ? "" : "where parent_theme_id is null";
    	return HierEntitySkeleton.fetchTree("amp_theme", condition, new EntityFetcher<AmpThemeSkeleton>() {
    		@Override public AmpThemeSkeleton fetch(ResultSet rs) throws SQLException {
    			return new AmpThemeSkeleton(nullInsteadOfZero(rs.getLong("amp_theme_id")), 
					 	rs.getString("name"), 
					 	rs.getString("theme_code"),
					 	nullInsteadOfZero(rs.getLong("parent_theme_id")));
    		}});
    	};
    	
}
