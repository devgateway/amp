package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

/**
 * TODO: do we support null percentages?
 * @author Dolghier Constantin
 *
 */
public class PercentageTextCell extends Cell {
	public final BigDecimal percentage;
	
	public PercentageTextCell(String text, long activityId, long entityId, BigDecimal percentage) {
		super(text, String.format("%s (%s %%)", text, percentage == null ? "(n/a)" : String.format("%.2f", percentage.doubleValue())), activityId, entityId);
		this.percentage = percentage;
	}
}
