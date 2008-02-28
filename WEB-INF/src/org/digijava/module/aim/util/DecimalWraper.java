package org.digijava.module.aim.util;

import java.math.BigDecimal;

import org.digijava.module.aim.helper.FormatHelper;

public class DecimalWraper  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7549949655765988774L;
	private String calculations;
	private BigDecimal value;
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCalculations() {
		return calculations;
	}

	public void setCalculations(String calculations) {
		this.calculations = calculations;
	}

	
	public Double doubleValue(){
		return value.doubleValue();
	}
	
	@Override
	public String toString() {
		if (this.value!=null){
			return FormatHelper.formatNumber(this.value.doubleValue());
		}
		return "";
	}
}
