package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.Output;

public class AmpAnnualProjectBudget implements Serializable, Versionable, Cloneable, Comparable {

	private static final Logger logger = Logger.getLogger(AmpAnnualProjectBudget.class);
	private Long ampAnnualProjectBudgetId;
	private Double amount;
	private Date year;
	private AmpActivityVersion activity;
	protected AmpCurrency ampCurrencyId;

	public Long getAmpAnnualProjectBudgetId() {
		return ampAnnualProjectBudgetId;
	}

	public void setAmpAnnualProjectBudgetId(Long ampAnnualProjectBudgetId) {
		this.ampAnnualProjectBudgetId = ampAnnualProjectBudgetId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public AmpActivityVersion getActivity() {
		return activity;
	}

	public void setActivity(AmpActivityVersion activity) {
		this.activity = activity;
	}

	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpAnnualProjectBudget aux = (AmpAnnualProjectBudget) obj;
		String original = "" + this.getAmount() + this.getYear().getYear();
		String copy = "" + +aux.getAmount() + aux.getYear().getYear();
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(new Output(null, new String[] { "AnnualProjectBudget" }, new Object[] { this }));
		return out;
	}

	@Override
	public Object getValue() {
		return "" + this.getAmount() + " " + this.getYear().getYear();
	}

	@Override
	public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
		AmpAnnualProjectBudget aux = (AmpAnnualProjectBudget) clone();
		aux.activity = newActivity;
		aux.ampAnnualProjectBudgetId = null;
		return aux;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int compareTo(Object other) {
		AmpAnnualProjectBudget oth = (AmpAnnualProjectBudget) other;
		return AmpAnnualProjectBudgerComparator.staticCompare(this, oth);
		
	}

	@Override
	public boolean equals(Object other) {
		return this.compareTo(other) == 0;
	}
	
	public static class AmpAnnualProjectBudgerComparator implements Comparator<AmpAnnualProjectBudget>{
		@Override
		public int compare(AmpAnnualProjectBudget o1, AmpAnnualProjectBudget o2) {
			return staticCompare(o1, o2);
		}
		
		public static int staticCompare(AmpAnnualProjectBudget o1, AmpAnnualProjectBudget o2) {
			int cmpClass = o1.getClass().getName().compareTo(o2.getClass().getName());
			if (cmpClass != 0)
				return cmpClass; // normally we shouldn't be getting entries of
									// different classes
			if (o2.getAmount() == null || o2.getYear() == null)
				return -1;
			if (o1.getAmount() == null || o1.getAmount() == null)
				return 1;
			return o1.getYear().compareTo(o2.getYear());
		}
	}

	public AmpCurrency getAmpCurrencyId() {
		return ampCurrencyId;
	}

	public void setAmpCurrencyId(AmpCurrency ampCurrencyId) {
		this.ampCurrencyId = ampCurrencyId;
	}

}