/**
 * AmountCell.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.AmountColWorker;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 8, 2006 Cell holding amounts.82
 * 
 */
public class AmountCell extends Cell {
	//public static DecimalFormat mf = new DecimalFormat("###,###,###,###.##");

	protected double amount;
	
	protected double percentage=100;

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
	 * We apply percentage only if there were no other percentages applied
	 */
	protected Set<String> percentageSourceColumnName;
	
	
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
		mergedCells = new HashSet();
		percentageSourceColumnName=new TreeSet<String>();
		// TODO Auto-generated constructor stub
	}

	public AmountCell(int ensureCapacity) {
		super();
		mergedCells = new HashSet(ensureCapacity);
		percentageSourceColumnName=new TreeSet<String>();
	}

	
	/**
	 * @param id
	 */
	public AmountCell(Long id) {
		super(id);
		mergedCells = new HashSet();
		percentageSourceColumnName=new TreeSet<String>();
		// TODO Auto-generated constructor stub
	}


	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.cell.Cell#toString()
	 */
	public String toString() {
		//mf.setMaximumFractionDigits(2);
		double am = getAmount();
		if (am == 0)
			return "";
		else
			return FormatHelper.formatNumberUsingCustomFormat(getAmount());
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

    public String getWrappedAmount() {
	if (id != null)
	    return FormatHelper.formatNumberUsingCustomFormat(convert() * percentage / 100);
	else
	    return "";
	}

	/**
	 * @param amount
	 *            The amount to set.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Class getWorker() {
		return AmountColWorker.class;
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
		return resultDbl;//Math.round(resultDbl);
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
		 AmountCell realRet=(AmountCell) this.newInstance();
		 realRet.setOwnerId(ret.getOwnerId());
		 //AmountCell realRet=new AmountCell(ret.getOwnerId());
		 Iterator i=ret.getMergedCells().iterator();
		 while (i.hasNext()) {
			AmountCell element = (AmountCell) i.next();
			AmountCell filtered=(AmountCell) element.filter(metaCell,ids);
			if(filtered!=null) 
				realRet.merge(realRet,filtered);
		}
		 return realRet;
	}

	public Cell newInstance() {
		return new AmountCell();
	}

	public Comparable comparableToken() {
		return new Double(getAmount());
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage, CellColumn source) {
		//never apply percentage coming from a column twice
	//	if(percentageSourceColumnName.contains(source.getName())) return;
		percentageSourceColumnName.add(source.getName());
		
		
		if(percentageSourceColumnName.contains(ArConstants.COLUMN_ANY_SECTOR) && source.getName().endsWith(ArConstants.COLUMN_SUB_SECTOR))  {
			//we forget the sector percentage, and apply the sub-sector percentage:
			this.percentage=percentage;
			return;
		}
		if(this.percentage>0){
			this.percentage = this.percentage * percentage / 100;
		}
		else this.percentage = percentage;
	}

	
	 @Override
	    public void merge(Cell c1, Cell c2) {
		AmountCell ac1 = (AmountCell) c1;
		AmountCell ac2 = (AmountCell) c2;
		if (this.getOwnerId() == null)
		    if(ac1.getOwnerId()!=null) this.setOwnerId(ac1.getOwnerId()); else this.setOwnerId(ac2.getOwnerId());
		
		//merge with c1 only if this is different than c1
		if (!this.equals(c1)) {
		    if (ac1.getId() == null)
			this.getMergedCells().addAll(ac1.getMergedCells());
		    else
			this.getMergedCells().add(ac1);
		}

		//merge with c2 only if this is different than c2
		if (!this.equals(c2)) {
		    if (ac2.getId() == null)
			this.getMergedCells().addAll(ac2.getMergedCells());
		    else
			this.getMergedCells().add(ac2);
		}

	    }

	
}