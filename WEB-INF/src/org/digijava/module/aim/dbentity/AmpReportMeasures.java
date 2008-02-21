package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;


public class AmpReportMeasures  implements Serializable, Comparable
{
	@ColumnLike
	private AmpMeasures measure;
	@Order
	private String orderId;
	@Level
	private AmpCategoryValue level;
	
	public AmpMeasures getMeasure() {
		return measure;
	}
	public void setMeasure(AmpMeasures measure) {
		this.measure = measure;
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
			int myOrder	=(orderId!=null)?Integer.parseInt(orderId):0;
			int oOrder	= (((AmpReportMeasures)o).getOrderId()!=null)?Integer.parseInt( ((AmpReportMeasures)o).getOrderId() ):0;
			return myOrder-oOrder;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
}