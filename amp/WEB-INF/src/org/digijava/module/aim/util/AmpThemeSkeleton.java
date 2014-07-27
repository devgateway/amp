package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.ViewFetcher;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.hibernate.jdbc.Work;

/**
 * lightweight alternative to AmpOrganisation, usable in the (vast) majority of places in AMP where a full Org is not needed, but just a name and id
 * @author Dolghier Constantin
 *
 */
public class AmpThemeSkeleton implements Comparable<AmpThemeSkeleton>, HierarchyListable
{
	
	private Long id;
	private String name;
	private Long parentThemeId;
	private String code;
	private Integer level;
	private String description;
	
	

	
	public AmpThemeSkeleton(Long id, String name, Long parentThemeId, String code, Integer level, String description) {
		this.id = id;
		this.name = name;
		this.parentThemeId = parentThemeId;
		this.code = code;
		this.level = level;
		this.description = description;
		this.children = new HashSet<AmpThemeSkeleton>();
		this.translatable = false;
	}	
	
	public Integer getLevel() {
		return this.level;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Long getParentThemeId() {
		return this.parentThemeId;
	}
	
	public String getCode() {
		return this.code;
	}
	

	private final Collection<AmpThemeSkeleton> children;
	private boolean translatable;
	public AmpThemeSkeleton() {
		this.translatable = false;
		this.children = new HashSet<AmpThemeSkeleton>();
	}

	@Override
	public int compareTo(AmpThemeSkeleton org)
	{
		if (this.name == null)
		{
			if (org.name == null)
				return 0; // null == null
			return -1; // null < [anything]
		}
		if (org.name == null)
			return 1; // [anything] > null
		
		return this.name.toLowerCase().trim().compareTo(org.name.toLowerCase().trim());
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (id: %d)", this.getName(), this.getId());
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
		return this.description;
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
		return this.children;
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
	
    private static Long nullInsteadOfZero(long val) {
    	if (val == 0) {
    		return null;
    	}
    	else return val;
    }
    
    private static Integer nullInsteadOfZero(int val) {
    	if (val == 0) {
    		return null;
    	}
    	else return val;
    	
    }
    

    
    
    public static Map<Long, AmpThemeSkeleton>  populateThemesSkeletonList(final boolean includeSubThemes) {
//    	final List<AmpThemeSkeleton> themesList = new ArrayList<AmpThemeSkeleton>();
    	
        final Map<Long, AmpThemeSkeleton> themes = new HashMap<Long, AmpThemeSkeleton>();
        PersistenceManager.getSession().doWork(new Work(){
				public void execute(Connection conn) throws SQLException {
					
					String condition = "WHERE parent_theme_id IS NULL";

					ViewFetcher v = DatabaseViewFetcher.getFetcherForView("amp_theme", 
							condition, TLSUtils.getEffectiveLangCode(), new HashMap<PropertyDescription, ColumnValuesCacher>(), conn, "*");		
					ResultSet rs = v.fetch(null);
					while (rs.next()) {
						Long id = rs.getLong("amp_theme_id");
						themes.put(id, new AmpThemeSkeleton(id, 
															   rs.getString("name"),
															   nullInsteadOfZero(rs.getLong("parent_theme_id")),
															   rs.getString("theme_code"),
															   nullInsteadOfZero(rs.getInt("level_")),
															   rs.getString("description")));
					}
					//linking the hierarchy parents<-children
					for (Map.Entry<Long, AmpThemeSkeleton> sk : themes.entrySet()) {
						Long parentId = sk.getValue().getParentThemeId();
						if (parentId != null) {
							AmpThemeSkeleton parent = themes.get(parentId);
							if (parent != null) {
								parent.children.add(sk.getValue());
							}
									
						}
					}
//					themesList.addAll(themes.values());
				}
			});
        return themes;
    }
}
