package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.digijava.module.aim.util.Output;

public class AmpAnnualProjectBudget implements Serializable, Versionable, Cloneable{
	
	
	private Long ampAnnualProjectBudgetId;
	private Double amount;
	private Date year;
	private AmpActivityVersion activity;
	
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
		String original = ""+this.getAmount()+this.getYear().getYear();
		String copy = "" + +aux.getAmount()+aux.getYear().getYear();
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { "AnnualProjectBudget" }, new Object[] { this }));
		return out;
	}

	@Override
	public Object getValue() {
		return "" + this.getAmount()+" "+this.getYear().getYear();
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

}
