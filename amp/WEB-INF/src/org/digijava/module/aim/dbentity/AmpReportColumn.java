package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.hibernate.Session;


public class AmpReportColumn  implements Serializable, Comparable
{
	private static Logger logger = Logger.getLogger(AmpReportColumn.class);
	@ColumnLike
	private AmpColumns column;
	@Order
	private String orderId;
	@Level
	private AmpCategoryValue level;
	
	private boolean generated;
	
	private static AmpCategoryValue defaultLevel = null;
	
	public AmpReportColumn() {
		
		try {
			Session dbSession = PersistenceManager.getRequestDBSession();
			if ( defaultLevel==null )
				defaultLevel=CategoryManagerUtil.getAmpCategoryValueFromDb(CategoryConstants.ACTIVITY_LEVEL_KEY, (long)0);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
		
		
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