package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;


public class AmpReportHierarchy implements Serializable, Comparable
{
	@ColumnLike
	private AmpColumns column;
	@Order
	private String levelId;
	@Level
	private AmpCategoryValue level;
	
	private static AmpCategoryValue defaultLevel = null;
	
	public AmpReportHierarchy(){
		if ( defaultLevel==null )
			defaultLevel=CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ACTIVITY_LEVEL_KEY, (long)0);
		level	= defaultLevel;
	}

	public AmpColumns getColumn() {
		return column;
	}
	public void setColumn(AmpColumns column) {
		this.column = column;
	}
	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}
	public AmpCategoryValue getLevel() {
		return level;
	}
	public void setLevel(AmpCategoryValue level) {
		this.level = level;
	}
	
	public int compareTo(Object o) {
		try {
			int myOrder	= Integer.parseInt(levelId);
			int oOrder	= Integer.parseInt( ((AmpReportHierarchy)o).getLevelId() );
			return myOrder-oOrder;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}