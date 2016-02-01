package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a cell with an amount and an attached metadata
 * @author Dolghier Constantin
 *
 */
public final class CategAmountCell extends Cell implements CategCell, DatedCell, NumberedCell {
		
	public final MonetaryAmount amount;
	public final MetaInfoSet metaInfo;
	public final TranslatedDate translatedDate;
	
	public CategAmountCell(long activityId, MonetaryAmount amount, MetaInfoSet metaInfo, Map<NiDimensionUsage, Coordinate> coos, TranslatedDate translatedDate) {
		super(activityId, -1, coos, Optional.empty());
		this.amount = amount;
		this.metaInfo = metaInfo;
		this.metaInfo.freeze();
		this.translatedDate = translatedDate;
	}

	@Override
	public String toString() {
		return String.format("(actId: %d, amt: %s, coos: {%s}, meta: {%s}", this.activityId, amount, AmpCollections.sortedMap(coordinates, (a, b) -> a.toString().compareTo(b.toString())), metaInfo);
	}

	@Override
	public MetaInfoSet getMetaInfo() {
		return this.metaInfo;
	}

	@Override
	public TranslatedDate getTranslatedDate() {
		return translatedDate;
	}

	@Override
	public int compareTo(Object o) {
		CategAmountCell cac = (CategAmountCell) o;
		return amount.compareTo(cac.amount);
	}

	@Override
	public String getDisplayedValue() {
		return amount.getDisplayable();
	}

	@Override
	public BigDecimal getAmount() {
		return amount.amount;
	}
	
	@Override
	public NiPrecisionSetting getPrecision() {
		return amount.precisionSetting;
	}

	@Override
	public <K> K accept(CellVisitor<K> visitor) {
		return visitor.visit(this);
	}
}
