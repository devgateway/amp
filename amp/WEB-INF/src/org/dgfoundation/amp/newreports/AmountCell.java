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
		super(value, format(formatter, value));
	}
	
	public AmountCell(Double value, DecimalFormat formatter) {
		super(value, format(formatter, value));
	}

	protected static String format(DecimalFormat formatter, Object value) {
		if (formatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return (formatter).format(value instanceof BigDecimal ? ((BigDecimal)value).doubleValue() : (Double)value);
	}
}
