package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * TODO: do we support null percentages?
 * @author Dolghier Constantin
 *
 */
public final class PercentageTextCell extends Cell {
	public final BigDecimal percentage;
	
	public PercentageTextCell(String text, long activityId, long entityId, BigDecimal percentage) {
		super(text, String.format("%s (%s %%)", text, percentage == null ? "(n/a)" : String.format("%.2f", percentage.doubleValue())), activityId, entityId);
		this.percentage = percentage;
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return MetaInfoSet.empty();
	}
}
