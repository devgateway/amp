package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * a @link {@link ReportCell} containing an amount 
 * @author Dolghier Constantin, Nadejda Mandrescu
 *
 */
public final class AmountCell extends ReportCell {
	
	public AmountCell(BigDecimal value, String formattedValue) {
		super(value, formattedValue, -1, null);
	}
	
	public AmountCell(Double value, DecimalFormat formatter) {
		super(value, format(formatter, value), -1, null);
	}

	protected static String format(DecimalFormat formatter, Double value) {
		if (formatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return formatter.format(value);
	}
}
