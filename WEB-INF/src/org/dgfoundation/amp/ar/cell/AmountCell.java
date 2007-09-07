/**
 * AmountCell.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.MetaInfo;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 8, 2006 Cell holding amounts.82
 * 
 */
public class AmountCell extends Cell {
	public static DecimalFormat mf = new DecimalFormat("###,###,###,###,###.##");

	protected double amount;
	
	protected int percentage=100;

	protected Set mergedCells;

	public int compareTo(Object o) {
		AmountCell ac = (AmountCell) o;
		return this.getId().compareTo(ac.getId());
	}

	protected double fromExchangeRate;
	
	protected double toExchangeRate;
	
	protected String currencyCode;
	
	protected Date currencyDate;
	
	
		/**
	 * @return Returns the toExchangeRate.
	 */
	public double getToExchangeRate() {
		return toExchangeRate;
	}

	/**
	 * @param toExchangeRate The toExchangeRate to set.
	 */
	public void setToExchangeRate(double toExchangeRate) {
		this.toExchangeRate = toExchangeRate;
	}

	/**
	 * 
	 */
	public AmountCell() {
		super();
		mergedCells = new TreeSet();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 */
	public AmountCell(Long id) {
		super(id);
		mergedCells = new TreeSet();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.cell.Cell#getWorker()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.cell.Cell#toString()
	 */
	public String toString() {
		// TODO Auto-generated method stub
		return mf.format(getAmount());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.cell.Cell#getValue()
	 */
	public Object getValue() {
		return new Double(amount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.cell.Cell#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		amount = ((Double) value).doubleValue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
	 */
	protected MetaInfo[] getViewArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.cell.Cell#merge(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public Cell merge(Cell c) {
		AmountCell ret = new AmountCell();
		AmountCell ac = (AmountCell) c;
		ret.setOwnerId(c.getOwnerId());
		if (ac.getId() == null)
			ret.getMergedCells().addAll(ac.getMergedCells());
		else
			ret.getMergedCells().add(ac);

		if (this.getId() == null)
			ret.getMergedCells().addAll(this.getMergedCells());
		else
			ret.getMergedCells().add(this);

		return ret;
	}

	/**
	 * @return Returns the amount.
	 */
	public double getAmount() {
		double ret = 0;
		if (id != null)
			return convert()*percentage/100;
		Iterator i = mergedCells.iterator();
		while (i.hasNext()) {
			AmountCell element = (AmountCell) i.next();
			ret += element.getAmount();
			//logger.info("amount++"+element.getAmount());
		}
		
		//logger.info("******total amount for owner "+this.getOwnerId()+"="+ret);
		return ret;
	}

	/**
	 * @param amount
	 *            The amount to set.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Class getWorker() {
		return null;
	}

	/**
	 * @return Returns the mergedCells.
	 */
	public Set getMergedCells() {
		return mergedCells;
	}

	/**
	 * @return Returns the currencyCode.
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode The currencyCode to set.
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * @return Returns the fromExchangeRate.
	 */
	public double getFromExchangeRate() {
		return fromExchangeRate;
	}

	/**
	 * @param fromExchangeRate The fromExchangeRate to set.
	 */
	public void setFromExchangeRate(double exchangeRate) {
		this.fromExchangeRate = exchangeRate;
	}


	public double convert() {
		double resultDbl = 0.0;
		if (fromExchangeRate != toExchangeRate) {
			double inter = 1 / fromExchangeRate;
			inter = inter * amount;
			resultDbl = inter * toExchangeRate;
		} else {
			resultDbl = amount;
		}
		return Math.round(resultDbl);
	}

	/**
	 * Adds amount directly to the amount property. Do not use this to perform horizontal totals, use merge() instead !
	 * @param amount the amount to be added to the internal property.
	 */
	public void rawAdd(double amount) {
		this.amount+=amount;
	}

	public Date getCurrencyDate() {
		return currencyDate;
	}

	public void setCurrencyDate(Date CurrencyDate) {
		this.currencyDate = CurrencyDate;
	}
	
	public Cell filter(Cell metaCell,Set ids) {
		 AmountCell ret=(AmountCell) super.filter(metaCell,ids);
		 if(ret==null || ret.getMergedCells().size()==0) return ret;
		 //we need to filter the merged cells too...
		 AmountCell realRet=new AmountCell(ret.getOwnerId());
		 Iterator i=ret.getMergedCells().iterator();
		 while (i.hasNext()) {
			AmountCell element = (AmountCell) i.next();
			AmountCell filtered=(AmountCell) element.filter(metaCell,ids);
			if(filtered!=null) realRet.getMergedCells().add(filtered);
		}
		 return realRet;
	}

	public Cell newInstance() {
		return new AmountCell();
	}

	public Comparable comparableToken() {
		return new Double(getAmount());
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
}