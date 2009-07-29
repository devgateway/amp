package org.digijava.module.widget.dbentity;

import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.widget.helper.WidgetVisitor;

/**
 * Chart widget for indicators.
 * @author Irakli Kobiashvili
 *
 */
public class AmpWidgetIndicatorChart extends AmpWidget {

	private static final long serialVersionUID = 1L;
	
	private IndicatorSector indicator;

	public IndicatorSector getIndicator() {
		return indicator;
	}

	public void setIndicator(IndicatorSector indicator) {
		this.indicator = indicator;
	}
        @Override
        public void accept(WidgetVisitor visitor) {
            visitor.visit(this);
        }
}
