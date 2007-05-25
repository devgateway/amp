package org.digijava.module.aim.dbentity ;

import java.io.Serializable;


public class AmpReportColumn  implements Serializable
{
	private AmpColumns column;
	private String orderId;

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
}