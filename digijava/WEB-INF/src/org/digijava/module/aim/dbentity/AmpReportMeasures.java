package org.digijava.module.aim.dbentity ;

import java.io.Serializable;


public class AmpReportMeasures  implements Serializable
{
	private AmpMeasures measure;
	private String orderId;

	
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
}