package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Objects;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * TODO: do we support null percentages?
 * @author Dolghier Constantin
 *
 */
public final class PercentageTextCell extends Cell {
	public final BigDecimal percentage;
	public final String text;
	
	public PercentageTextCell(String text, long activityId, long entityId, BigDecimal percentage) {
		super(activityId, entityId);
		Objects.requireNonNull(text);
		this.percentage = percentage;
		this.text = text == null ? "" : text;
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return MetaInfoSet.empty();
	}

	@Override
	public int compareTo(Object o) {
		PercentageTextCell ptc = (PercentageTextCell) o;
		return text.compareTo(ptc.text);
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s %%)", text, percentage == null ? "(n/a)" : String.format("%.2f", percentage.doubleValue()));
	}

	@Override
	public String getDisplayedValue() {
		return text;
	}
}
