package org.digijava.module.gis.dbentity;

import org.digijava.module.aim.dbentity.IndicatorSector;

/**
 * Chart widget for indicators.
 * @author Irakli Kobiashvili
 *
 */
public class AmpIndicatorChartWidget extends AmpWidget {

	private static final long serialVersionUID = 1L;
	
	private IndicatorSector indicator;

	public IndicatorSector getIndicator() {
		return indicator;
	}

	public void setIndicator(IndicatorSector indicator) {
		this.indicator = indicator;
	}

}
