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
			if(orderId==null) return -1;
			if(o==null) return -1;
			if(((AmpReportMeasures)o).getOrderId()==null)return -1;
			int myOrder	= Integer.parseInt(orderId);
			int oOrder	= Integer.parseInt( ((AmpReportMeasures)o).getOrderId() );
			return myOrder-oOrder;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
}