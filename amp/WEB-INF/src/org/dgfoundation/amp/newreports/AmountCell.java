package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * a @link {@link ReportCell} containing an amount 
 * @author Dolghier Constantin, Nadejda Mandrescu
 *
 */
public final class AmountCell extends ReportCell {
	
	public AmountCell(BigDecimal value, DecimalFormat formatter) {
		super(value, formatter);
	}
	
	public AmountCell(Double value, DecimalFormat formatter) {
		super(value, formatter);
	}
	
	@Override
	protected String getFormattedValue() {
		if (formatter == null) 
			return String.valueOf(value);
		return ((DecimalFormat)formatter).format(value instanceof BigDecimal ? ((BigDecimal)value).doubleValue() : (Double)value);
	}
}
