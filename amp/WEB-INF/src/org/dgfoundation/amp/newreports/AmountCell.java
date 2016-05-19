package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * a @link {@link ReportCell} containing an amount
 * TODO: change to only have BigDecimal as values after Mondrian has been removed from AMP. Change {@link #extractValue()} accordingly 
 * @author Dolghier Constantin, Nadejda Mandrescu
 *
 */
@JsonIgnoreProperties(value = "")
@JsonFilter(EPConstants.JSON_FILTER_AMOUNT_CELL)
public final class AmountCell extends ReportCell {
	
	public AmountCell(BigDecimal value, String formattedValue) {
		super(value, formattedValue);
	}
	
	// NIREPORTS: remove this when cleaning up Mondrian
	public AmountCell(Double value, DecimalFormat formatter) {
		super(value, format(formatter, value));
	}

	public double extractValue() {
		if (value instanceof Double)
			return ((Double) value);
		return ((BigDecimal) value).doubleValue();
	}
	
	protected static String format(DecimalFormat formatter, Double value) {
		if (formatter == null || value == null) 
			return value == null ? "" : String.valueOf(value);
		return formatter.format(value);
	}
}
