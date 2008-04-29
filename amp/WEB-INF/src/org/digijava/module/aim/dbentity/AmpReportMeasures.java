package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.digijava.module.aim.annotations.reports.ColumnLike;
import org.digijava.module.aim.annotations.reports.Level;
import org.digijava.module.aim.annotations.reports.Order;


public class AmpReportMeasures  implements Serializable, Comparable<AmpReportMeasures>{
	private static Logger logger = Logger.getLogger(AmpReportMeasures.class);
	
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
	
	public int compareTo(AmpReportMeasures o) {
		try {
			if(orderId==null) return -1;
			if(o==null) return -1;
			if(o.getOrderId()==null)return -1;
			int myOrder	= getOrder();
			int oOrder	= ((AmpReportMeasures)o).getOrder();
			return myOrder - oOrder;
		}
		catch (NumberFormatException e) {
			logger.error("NumberFormatException:", e);
			return -1;
		}
	}
	
	public Integer getOrder() {
		try{
			if(orderId==null) return new Integer(0);
			return 	Integer.parseInt(orderId);
		}catch (NumberFormatException e) {
			logger.error("NumberFormatException:"+orderId+":", e);
			return 0;
		}		
	}
}