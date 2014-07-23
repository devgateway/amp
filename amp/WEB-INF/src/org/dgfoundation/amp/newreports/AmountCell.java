package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;

/**
 * a @link {@link ReportCell} containing an amount 
 * @author Dolghier Constantin
 *
 */
public final class AmountCell extends ReportCell {
	
	public AmountCell(BigDecimal value) {
		super(value);
	}
	
	public AmountCell(Double value) {
		super(value);
	}
}
