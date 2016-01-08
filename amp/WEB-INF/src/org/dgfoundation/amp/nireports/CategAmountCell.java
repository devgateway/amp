package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * a cell with an amount and an attached metadata
 * @author Dolghier Constantin
 *
 */
public final class CategAmountCell extends Cell implements CategCell, DatedCell {
		
	public final MonetaryAmount amount;
	public final MetaInfoSet metaInfo;
	public final TranslatedDate translatedDate;
	
	public CategAmountCell(long activityId, MonetaryAmount amount, MetaInfoSet metaInfo, TranslatedDate translatedDate) {
		super(amount, amount.getDisplayable(), activityId, -1);
		this.amount = amount;
		this.metaInfo = metaInfo;
		this.metaInfo.freeze();
		this.translatedDate = translatedDate;
	}

	@Override
	public String toString() {
		return String.format("(actId: %d, %s with meta: {%s}", this.activityId, amount, metaInfo);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return this.metaInfo;
	}

	@Override
	public TranslatedDate getTranslatedDate() {
		return translatedDate;
	}
}
