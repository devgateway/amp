package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;


public class AmpReportColumn  implements Serializable, Comparable
{
	@ColumnLike
	private AmpColumns column;
	@Order
	private String orderId;
	@Level
	private AmpCategoryValue level;
	
	private static AmpCategoryValue defaultLevel = null;
	
	public AmpReportColumn() {
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public AmpCategoryValue getLevel() {
		return level;
	}
	public void setLevel(AmpCategoryValue level) {
		this.level = level;
	}
	public int compareTo(Object o) {
		try {
			int myOrder	= Integer.parseInt(orderId);
			int oOrder	= Integer.parseInt( ((AmpReportColumn)o).getOrderId() );
			return myOrder-oOrder;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
}