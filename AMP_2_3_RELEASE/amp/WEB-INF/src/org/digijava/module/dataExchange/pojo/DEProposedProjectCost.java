/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.util.Date;

/**
 * @author dan
 *
 */
public class DEProposedProjectCost {

	/**
	 * 
	 */
	
	private Date date 			= null;
	private Double amount 		= new Double(0);
	private String currency 		= null;
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public DEProposedProjectCost() {
		// TODO Auto-generated constructor stub
	}

}
