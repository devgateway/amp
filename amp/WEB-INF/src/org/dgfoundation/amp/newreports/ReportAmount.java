package org.dgfoundation.amp.newreports;

import java.math.BigDecimal;

/**
 * a @link {@link ReportCell} containing an amount 
 * @author Dolghier Constantin
 *
 */
public final class ReportAmount extends ReportCell {
	
	public ReportAmount(BigDecimal value) {
		super(value);
	}
	
	public ReportAmount(Double value) {
		super(value);
	}
}
