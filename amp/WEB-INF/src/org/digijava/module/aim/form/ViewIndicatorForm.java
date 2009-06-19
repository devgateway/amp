package org.digijava.module.aim.form;

import java.util.Collection;

public class ViewIndicatorForm extends MainProjectDetailsForm {
	
	private Collection indicators;

	/**
	 * @return Returns the indicators.
	 */
	public Collection getIndicators() {
		return indicators;
	}

	/**
	 * @param indicators The indicators to set.
	 */
	public void setIndicators(Collection indicators) {
		this.indicators = indicators;
	}
}