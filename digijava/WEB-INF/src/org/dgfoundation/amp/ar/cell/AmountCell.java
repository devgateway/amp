/**
 * AmountCell.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.cell;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.aim.helper.Currency;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 8, 2006 Cell holding amounts.
 * 
 */
public class AmountCell extends Cell {
	public static DecimalFormat mf = new DecimalFormat("###,###,###,###,###");

	protected double amount;

	protected Set mergedCells;

	public int compareTo(Object o) {
		AmountCell ac = (AmountCell) o;
		return this.getId().compareTo(ac.getId());
	}

	// TODO: implement currency !
	protected Currency currency;

	/**
	 * @return Returns the currency.
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            The currency to set.
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
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
	 * @see org.dgfoundation.amp.ar.cell.Cell#add(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public Cell merge(Cell c) {
		AmountCell ret = new AmountCell();
		AmountCell ac = (AmountCell) c;
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
			return amount;
		Iterator i = mergedCells.iterator();
		while (i.hasNext()) {
			AmountCell element = (AmountCell) i.next();
			ret += element.getAmount();
		}
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

}
