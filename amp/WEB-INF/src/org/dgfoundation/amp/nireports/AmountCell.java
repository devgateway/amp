package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * a cell with an amount and an attached metadata
 * @author Dolghier Constantin
 *
 */
public final class AmountCell extends Cell implements CategCell, NumberedCell {
		
	public final MonetaryAmount amount;
	
	public AmountCell(long activityId, MonetaryAmount amount) {
		super(activityId);
		this.amount = amount;
	}
	
	@Override
	public MonetaryAmount getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return String.format("(actId: %d, %s", this.activityId, amount);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return MetaInfoSet.empty();
	}

	@Override
	public int compareTo(Object o) {
		AmountCell cac = (AmountCell) o;
		return amount.compareTo(cac.amount);
	}

	@Override
	public String getDisplayedValue() {
		return amount.getDisplayable();
	}
}
