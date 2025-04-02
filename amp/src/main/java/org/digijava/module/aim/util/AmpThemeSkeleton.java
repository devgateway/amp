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
    
    private static String getCondition(long root) {
        String condition = "WHERE ((deleted IS NULL) OR (deleted = false)) ";
        if (root > 0) {
            condition += String.format(" AND amp_theme_id IN (SELECT amp_theme_id FROM all_programs_with_levels WHERE id0 = %d)", root); 
        }
        return condition;
    }

    /**
     * Builds a map of ampthemeskeletons, indexed by ids
     * @param root id of the root whose children are fetched, -1 for no such limit
     * @return
     */
    public static Map<Long, AmpThemeSkeleton> populateThemesTree(long root) {
        return HierEntitySkeleton.fetchTree("amp_theme", getCondition(root), new EntityFetcher<AmpThemeSkeleton>() {
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
